# SampleForTracing

Набор тестовых REST-микросервисов для экспериментов с трассировкой.

## Структура

- `services/api-gateway` - внешняя точка входа для checkout-запросов.
- `services/order-service` - оркестрация полного checkout-сценария.
- `services/inventory-service` - резервирование товарных остатков.
- `services/pricing-service` - расчёт суммы, скидок и налога.
- `services/payment-service` - симуляция обработки оплаты.

## Проверка

```powershell
./gradlew.bat test
```

Построить отчёт покрытия JaCoCo:

```powershell
./gradlew.bat jacocoRootReport
```

HTML-отчёт будет создан в `build/reports/jacoco/jacocoRootReport/html/index.html`.

## Observability

Поднять локальный Jaeger all-in-one и сразу открыть UI:

```powershell
./infrastructure/start-jaeger.ps1
```

Docker Compose конфигурация лежит в `infrastructure/docker-compose.yml`, кастомный образ Jaeger описан в `infrastructure/jaeger/Dockerfile`.

Поднять весь локальный стенд одной командой:

```powershell
./infrastructure/start-demo.ps1
```

Скрипт:
- запускает Jaeger all-in-one через Docker Compose;
- запускает Redpanda как локальный Kafka-compatible broker;
- стартует все 5 сервисов в отдельных окнах PowerShell;
- автоматически открывает:
  - `http://localhost:8080`
  - `http://localhost:16686`

Остановить стенд:

```powershell
./infrastructure/stop-demo.ps1
```

## Запуск

Запускайте сервисы в отдельных терминалах:

```powershell
./gradlew.bat :inventory-service:bootRun
./gradlew.bat :pricing-service:bootRun
./gradlew.bat :payment-service:bootRun
./gradlew.bat :order-service:bootRun
./gradlew.bat :api-gateway:bootRun
```

Пример checkout-запроса:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/checkout -ContentType 'application/json' -Body '{"customerId":"customer-1","itemId":"SKU-CHAIR-01","quantity":2,"paymentScenario":"SUCCESS"}'
```

Панель управления доступна по адресу:

```text
http://localhost:8080
```

На странице есть:
- кнопки запуска типовых checkout-сценариев;
- просмотр статуса всех сервисов;
- загрузка последнего заказа;
- быстрые ссылки на actuator endpoints и Jaeger UI.

## Сценарии оплаты

- `SUCCESS` - успешная оплата.
- `DECLINED` - платёж отклонён с ответом `422`.
- `CONFLICT` - конфликт/повторная попытка с ответом `409`.
- `ERROR` - внутренняя ошибка платёжного сервиса с ответом `500`.
- `DELAYED` - успешная оплата с искусственной задержкой для длинных span.

## Kafka Flow

В checkout flow добавлена Kafka side-chain для проверки propagation через Kafka:

- `order-service` публикует `checkout.order-created` после создания заказа.
- `inventory-service` и `pricing-service` читают `checkout.order-created`.
- `order-service` публикует `checkout.order-completed` после успешной оплаты.
- `payment-service` читает `checkout.order-completed`.

REST-цепочка при этом остаётся основной, Kafka используется как параллельная ветка для проверки producer/consumer spans.
Trace context для Kafka прокидывается автоматически через `tracing-common`: producer пишет headers при обычном `KafkaTemplate.send(...)`, consumers открывают span обработки вокруг обычных `@KafkaListener` методов.

## Результаты Проверок Tracing

### REST и Async

Проверено на `org.nextbi.dataflow:tracing-common:0.0.4`.

Что подтверждено:

- сервисы работают без локальных `JaegerTracerConfiguration`;
- HTTP propagation работает через обычный `RestClient`;
- async propagation работает через `CompletableFuture.supplyAsync(..., ExecutorService)`;
- `inventory-service` и `pricing-service` не становятся отдельными root traces;
- checkout trace содержит все 5 сервисов;
- dependency graph строится.

Проверенный REST/async flow:

```text
api-gateway -> order-service
order-service -> inventory-service
order-service -> pricing-service
order-service -> payment-service
```

### Kafka

Проверено на `org.nextbi.dataflow:tracing-common:0.0.5`.

Что подтверждено:

- Kafka propagation работает без ручных вызовов `KafkaTracingService`;
- producer использует обычный `KafkaTemplate.send(topic, key, value)`;
- consumers используют обычные `@KafkaListener` методы;
- Kafka consumer spans попадают в общий checkout trace;
- consumer groups находятся в состоянии `Stable`;
- consumer lag равен `0`.

Проверенные Kafka spans:

```text
kafka checkout.order-created consume
kafka checkout.order-completed consume
```

Ожидаемые Kafka tags в Jaeger:

```text
component = kafka
span.kind = consumer
kafka.topic = checkout.order-created / checkout.order-completed
kafka.consumer.group = inventory-service / pricing-service / payment-service
message_bus.destination = checkout.order-created / checkout.order-completed
```

Проверенный Kafka flow:

```text
order-service -> checkout.order-created -> inventory-service
order-service -> checkout.order-created -> pricing-service
order-service -> checkout.order-completed -> payment-service
```

Контрольные команды:

```powershell
./gradlew.bat test
./infrastructure/start-demo.ps1
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/checkout -ContentType 'application/json' -Body '{"customerId":"customer-1","itemId":"SKU-CHAIR-01","quantity":2,"paymentScenario":"SUCCESS"}'
```

Проверка consumer groups:

```powershell
docker exec sample-for-tracing-redpanda rpk group describe inventory-service
docker exec sample-for-tracing-redpanda rpk group describe pricing-service
docker exec sample-for-tracing-redpanda rpk group describe payment-service
```

## TODO

- проверить error spans для Kafka consumer сценариев
- решить, нужен ли отдельный producer span или достаточно автоматического inject headers
- после проверки в реальном `dataflow` актуализировать `TRACING_COMMON_KAFKA_AUTOWRAP_INSTRUCTIONS.txt`

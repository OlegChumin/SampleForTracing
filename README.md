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

## TODO

- проверить propagation активного span через `ExecutorService`/`CompletableFuture`
- сейчас синхронный вызов `payment-service` попадает в общий trace, а асинхронные вызовы `inventory-service` и `pricing-service` становятся отдельными root traces
- при необходимости доработать `tracing-common`, чтобы starter автоматически оборачивал Spring `Executor`/`ExecutorService` или давал готовый tracing-aware executor
- после доработки повторить проверку Jaeger: единый trace, spans всех 5 сервисов и dependency graph

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

## Сценарии оплаты

- `SUCCESS` - успешная оплата.
- `DECLINED` - платёж отклонён с ответом `422`.
- `CONFLICT` - конфликт/повторная попытка с ответом `409`.
- `ERROR` - внутренняя ошибка платёжного сервиса с ответом `500`.
- `DELAYED` - успешная оплата с искусственной задержкой для длинных span.

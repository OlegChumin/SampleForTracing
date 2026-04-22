# SampleForTracing

Workspace with sample REST microservices for tracing experiments.

## Structure

- `services/api-gateway` - entry point for checkout requests.
- `services/order-service` - orchestrates the checkout flow.
- `services/inventory-service` - reserves items.
- `services/pricing-service` - calculates totals, discounts and taxes.
- `services/payment-service` - simulates payment execution.

## Run

```powershell
./gradlew.bat test
```

Start services in separate terminals:

```powershell
./gradlew.bat :inventory-service:bootRun
./gradlew.bat :pricing-service:bootRun
./gradlew.bat :payment-service:bootRun
./gradlew.bat :order-service:bootRun
./gradlew.bat :api-gateway:bootRun
```

Checkout request example:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/checkout -ContentType 'application/json' -Body '{"customerId":"customer-1","itemId":"SKU-CHAIR-01","quantity":2,"paymentScenario":"SUCCESS"}'
```

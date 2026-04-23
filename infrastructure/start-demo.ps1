$ErrorActionPreference = "Stop"

$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDirectory

function Start-ServiceWindow {
    param(
        [Parameter(Mandatory = $true)]
        [string]$ServiceName,
        [Parameter(Mandatory = $true)]
        [string]$GradleTask
    )

    $command = "Set-Location '$projectRoot'; ./gradlew.bat $GradleTask"
    Start-Process powershell -ArgumentList @(
        "-NoExit",
        "-Command",
        $command
    ) -WindowStyle Normal | Out-Null

    Write-Host "Started $ServiceName using task $GradleTask"
}

Push-Location $projectRoot
try {
    & "$scriptDirectory\start-jaeger.ps1"

    Start-ServiceWindow -ServiceName "admin-server" -GradleTask ":admin-server:bootRun"
    Start-ServiceWindow -ServiceName "inventory-service" -GradleTask ":inventory-service:bootRun"
    Start-ServiceWindow -ServiceName "pricing-service" -GradleTask ":pricing-service:bootRun"
    Start-ServiceWindow -ServiceName "payment-service" -GradleTask ":payment-service:bootRun"
    Start-ServiceWindow -ServiceName "order-service" -GradleTask ":order-service:bootRun"
    Start-ServiceWindow -ServiceName "api-gateway" -GradleTask ":api-gateway:bootRun"

    Start-Sleep -Seconds 12

    Start-Process "http://localhost:16686"
    Start-Process "http://localhost:9091"
    Start-Process "http://localhost:9090"
    Start-Process "http://localhost:3000"
    Start-Process "http://localhost:8080"

    Write-Host "Control panel should be available on http://localhost:8080"
    Write-Host "Spring Boot Admin should be available on http://localhost:9090"
    Write-Host "Grafana dashboard should be available on http://localhost:3000"
    Write-Host "Prometheus should be available on http://localhost:9091"
    Write-Host "Jaeger UI should be available on http://localhost:16686"
}
finally {
    Pop-Location
}

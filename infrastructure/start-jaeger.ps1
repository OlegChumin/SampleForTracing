$ErrorActionPreference = "Stop"

$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path

Push-Location $scriptDirectory
try {
    docker compose up -d --build
    Start-Sleep -Seconds 3
    Write-Host "Observability stack is starting:"
    Write-Host "  Jaeger: http://localhost:16686"
    Write-Host "  Prometheus: http://localhost:9091"
    Write-Host "  Grafana: http://localhost:3000"
}
finally {
    Pop-Location
}

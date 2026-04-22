$ErrorActionPreference = "Stop"

$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path

Push-Location $scriptDirectory
try {
    docker compose up -d --build
    Start-Sleep -Seconds 3
    Write-Host "Jaeger is starting on http://localhost:16686"
}
finally {
    Pop-Location
}

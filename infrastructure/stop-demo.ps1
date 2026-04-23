$ErrorActionPreference = "SilentlyContinue"

$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path

Push-Location $scriptDirectory
try {
    docker compose down
}
finally {
    Pop-Location
}

Get-CimInstance Win32_Process |
    Where-Object {
        (
            $_.Name -match 'java(.exe)?' -or
            $_.Name -match 'powershell(.exe)?' -or
            $_.Name -match 'pwsh(.exe)?'
        ) -and
        $_.CommandLine -match ':admin-server:bootRun|:api-gateway:bootRun|:order-service:bootRun|:inventory-service:bootRun|:pricing-service:bootRun|:payment-service:bootRun|admin-server|api-gateway|order-service|inventory-service|pricing-service|payment-service'
    } |
    ForEach-Object {
        Stop-Process -Id $_.ProcessId -Force
        Write-Host "Stopped process $($_.ProcessId)"
    }

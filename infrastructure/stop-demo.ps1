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
        $_.Name -match 'java(.exe)?' -and
        $_.CommandLine -match 'api-gateway|order-service|inventory-service|pricing-service|payment-service'
    } |
    ForEach-Object {
        Stop-Process -Id $_.ProcessId -Force
        Write-Host "Stopped process $($_.ProcessId)"
    }

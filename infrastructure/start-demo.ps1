$ErrorActionPreference = "Stop"

$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDirectory

$requiredPorts = @(18080, 18081, 18082, 18083, 18084, 19090, 19091, 9092, 16686, 13000)

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

function Assert-RequiredPortsAvailable {
    param(
        [Parameter(Mandatory = $true)]
        [int[]]$Ports
    )

    $busyPorts = foreach ($port in $Ports) {
        $connection = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue |
            Select-Object -First 1

        if (-not $connection) {
            continue
        }

        $process = Get-CimInstance Win32_Process -Filter "ProcessId = $($connection.OwningProcess)" -ErrorAction SilentlyContinue

        [PSCustomObject]@{
            Port = $port
            ProcessId = $connection.OwningProcess
            ProcessName = $process.Name
            CommandLine = $process.CommandLine
        }
    }

    if ($busyPorts) {
        $details = $busyPorts |
            Sort-Object Port |
            ForEach-Object { "Port $($_.Port) is busy by PID $($_.ProcessId) [$($_.ProcessName)] $($_.CommandLine)" }

        throw "Cannot start demo because required ports are already busy.`n$($details -join [Environment]::NewLine)"
    }
}

Push-Location $projectRoot
try {
    & "$scriptDirectory\stop-demo.ps1"
    Start-Sleep -Seconds 2
    Assert-RequiredPortsAvailable -Ports $requiredPorts

    & "$scriptDirectory\start-jaeger.ps1"

    Start-ServiceWindow -ServiceName "admin-server" -GradleTask ":admin-server:bootRun"
    Start-ServiceWindow -ServiceName "inventory-service" -GradleTask ":inventory-service:bootRun"
    Start-ServiceWindow -ServiceName "pricing-service" -GradleTask ":pricing-service:bootRun"
    Start-ServiceWindow -ServiceName "payment-service" -GradleTask ":payment-service:bootRun"
    Start-ServiceWindow -ServiceName "order-service" -GradleTask ":order-service:bootRun"
    Start-ServiceWindow -ServiceName "api-gateway" -GradleTask ":api-gateway:bootRun"

    Start-Sleep -Seconds 12

    Start-Process "http://localhost:16686"
    Start-Process "http://localhost:19091"
    Start-Process "http://localhost:19090"
    Start-Process "http://localhost:13000"
    Start-Process "http://localhost:18080"

    Write-Host "Control panel should be available on http://localhost:18080"
    Write-Host "Spring Boot Admin should be available on http://localhost:19090"
    Write-Host "Grafana dashboard should be available on http://localhost:13000"
    Write-Host "Prometheus should be available on http://localhost:19091"
    Write-Host "Jaeger UI should be available on http://localhost:16686"
}
finally {
    Pop-Location
}

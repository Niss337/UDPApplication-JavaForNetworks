# Script to send UDP (replace  netcat )
param(
    [string]$message = "Test from PowerShell"
)

$ip = "127.0.0.1"
$port = 8080

try {
    $endpoint = New-Object System.Net.IPEndPoint([System.Net.IPAddress]::Parse($ip), $port)
    $udpclient = New-Object System.Net.Sockets.UdpClient
    $bytes = [System.Text.Encoding]::UTF8.GetBytes($message)
    $sent = $udpclient.Send($bytes, $bytes.Length, $endpoint)
    $udpclient.Close()
    
    Write-Host " Message sent ($sent bytes): $message" -ForegroundColor Green
} catch {
    Write-Host "Error : $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " Iniciando VVV System (Backend + Front) " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "Iniciando Backend (e Docker) em uma nova janela..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend; .\start.ps1"

Write-Host "Iniciando Frontend em uma nova janela..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd frontend; npm run dev"

Write-Host "Servicos iniciados! Feche as janelas para parar os servidores." -ForegroundColor Green

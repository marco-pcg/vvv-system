#!/bin/bash

echo "========================================"
echo " Iniciando VVV System (Backend + Front) "
echo "========================================"

# Funcao para matar os processos ao sair
cleanup() {
    echo "Encerrando servicos..."
    kill 0
}

# Trap catch Ctrl+C
trap cleanup SIGINT SIGTERM

echo "Iniciando Backend (Spring Boot e Docker)..."
cd backend
chmod +x start.sh
./start.sh &
cd ..

echo "Iniciando Frontend (Vite/React)..."
cd frontend
npm run dev &
cd ..

echo "Servicos rodando em background. Pressione [CTRL+C] para encerrar."
wait

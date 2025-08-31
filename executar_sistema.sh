#!/bin/bash

# Script para executar o Sistema de Controle de Estoque com Login
# Autor: Sistema de Controle de Estoque
# Data: $(date)

# Configurações do banco de dados
DB_HOST="localhost"
DB_USER="root"
DB_PASS="123"
DB_NAME="SistemaEstoque"

echo "=========================================="
echo "  Sistema de Controle de Estoque"
echo "=========================================="
echo ""

# Verificar se o arquivo JAR do MySQL existe
if [ ! -f "mysql-connector-java.jar" ]; then
    echo "❌ Erro: Arquivo mysql-connector-java.jar não encontrado!"
    echo "📁 Copiando o driver MySQL do sistema..."
    cp /usr/share/java/mysql-connector-j-9.3.0.jar mysql-connector-java.jar
    if [ $? -eq 0 ]; then
        echo "✅ Driver MySQL copiado com sucesso!"
    else
        echo "❌ Erro ao copiar o driver MySQL!"
        echo "💡 Instale o MySQL Connector: sudo apt install mysql-connector-java"
        exit 1
    fi
fi

# Verificar se o diretório bin existe
if [ ! -d "bin" ]; then
    echo "📁 Compilando o projeto..."
    mkdir -p bin
    javac -cp ".:mysql-connector-java.jar" -d bin src/model/*.java src/controller/*.java src/view/*.java
    if [ $? -eq 0 ]; then
        echo "✅ Projeto compilado com sucesso!"
    else
        echo "❌ Erro na compilação!"
        exit 1
    fi
fi

#Verificar se há conexão com o bd
echo "🔍 Testando conexão com o banco de dados..."
if command -v mysql &> /dev/null; then
    # Executar comando MySQL e capturar saída de erro
    ERROR_OUTPUT=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" -e "USE $DB_NAME; SELECT 1;" 2>&1 >/dev/null)
    MYSQL_EXIT_CODE=$?
    
    if [ $MYSQL_EXIT_CODE -eq 0 ]; then
        echo "✅ Conexão com o banco de dados '$DB_NAME' estabelecida!"
    else
        echo "❌ Erro: Não foi possível conectar ao banco de dados '$DB_NAME'!"
        echo "💡 Detalhes do erro:"
        echo "   $ERROR_OUTPUT"
        echo "Encerrando tentativa de conexão..."
        exit 1
    fi
else
    echo "⚠️  mysql client não encontrado, pulando teste de conexão..."
    echo "💡 Instale o cliente MySQL: sudo apt install mysql-client"
fi

echo "🚀 Iniciando o sistema..."
echo ""
echo "📋 Credenciais de teste:"
echo "   Administrador: admin / admin123"
echo "   Gerente: gerente1 / gerente123"
echo ""

# Executar o sistema
java -cp ".:mysql-connector-java.jar:bin" view.LoginView

echo ""
echo "👋 Sistema encerrado." 
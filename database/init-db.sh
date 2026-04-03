#!/bin/bash

# Script para inicializar la base de datos

echo "Esperando a que SQL Server esté listo..."
sleep 15

SA_PASSWORD=${SQL_SA_PASSWORD:-MyPassword123!}
SQLSERVER_HOST=${SQLSERVER_HOST:-sqlserver}

echo "Ejecutando seed-data.sql..."
/opt/mssql-tools18/bin/sqlcmd \
  -S $SQLSERVER_HOST \
  -U sa \
  -P $SA_PASSWORD \
  -i /seed-data.sql

if [ $? -eq 0 ]; then
    echo "✓ Base de datos inicializada correctamente"
else
    echo "✗ Error al inicializar la base de datos"
    exit 1
fi

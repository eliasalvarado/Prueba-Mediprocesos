#!/bin/bash

echo "Esperando a que SQL Server esté listo..."
sleep 20

echo "Ejecutando script de inicialización..."
/opt/mssql-tools18/bin/sqlcmd -S sqlserver -U sa -P ${SA_PASSWORD} -i /docker-entrypoint-initdb.d/01-init.sql

echo "Script de inicialización completado"

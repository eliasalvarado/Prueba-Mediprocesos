#!/bin/bash
# wait-for-sqlserver.sh
# Script para esperar a que SQL Server esté disponible

host="$1"
port="$2"
shift 2
cmd="$@"

echo "Esperando a que SQL Server esté disponible en $host:$port..."

until nc -z "$host" "$port"; do
  echo "SQL Server aún no está disponible, esperando..."
  sleep 2
done

echo "SQL Server está disponible, iniciando aplicación..."
exec $cmd

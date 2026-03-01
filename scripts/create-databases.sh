#!/usr/bin/env bash
set -e

MYSQL_HOST="${MYSQL_HOST:-localhost}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_ROOT_USER="${MYSQL_ROOT_USER:-root}"
MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD:-}"

echo "==> Criando bancos 'crm' e 'sign'"

if [ -n "$MYSQL_ROOT_PASSWORD" ]; then
  export MYSQL_PWD="$MYSQL_ROOT_PASSWORD"
fi

mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_ROOT_USER" <<EOF

CREATE DATABASE IF NOT EXISTS crm
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE DATABASE IF NOT EXISTS sign
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'hinovaUser'@'%' IDENTIFIED BY 'hinovaPassword';

GRANT ALL PRIVILEGES ON crm.*  TO 'hinovaUser'@'%';
GRANT ALL PRIVILEGES ON sign.* TO 'hinovaUser'@'%';

FLUSH PRIVILEGES;

EOF

echo "==> Bancos criados com sucesso."
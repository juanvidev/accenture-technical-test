#!/bin/sh
set -e

echo "⏳ Esperando a DynamoDB Local..."

until aws dynamodb list-tables \
  --endpoint-url http://dynamodb-accenture:8000 \
  --region us-east-1 > /dev/null 2>&1
do
  echo "DynamoDB aún no está listo, reintentando..."
  sleep 2
done

echo "✅ DynamoDB listo. Creando tabla franchises..."

aws dynamodb create-table \
  --table-name franchises \
  --attribute-definitions \
    AttributeName=id,AttributeType=S \
    AttributeName=name,AttributeType=S \
  --key-schema \
    AttributeName=id,KeyType=HASH \
  --global-secondary-indexes '[
    {
      "IndexName": "franchise-name-index",
      "KeySchema": [
        { "AttributeName": "name", "KeyType": "HASH" }
      ],
      "Projection": { "ProjectionType": "ALL" },
      "ProvisionedThroughput": {
        "ReadCapacityUnits": 5,
        "WriteCapacityUnits": 5
      }
    }
  ]' \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --endpoint-url http://dynamodb-accenture:8000 \
  --region us-east-1 \
|| echo "⚠️ La tabla ya existe"

echo "🎉 Inicialización de DynamoDB completada"

#!/bin/sh
set -e

echo "⏳ Waiting for DynamoDB..."
until curl -s http://dynamodb-accenture:8000 > /dev/null; do
  sleep 2
done

echo "✅ DynamoDB is up. Creating tables..."

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
      "Projection": {
        "ProjectionType": "ALL"
      },
      "ProvisionedThroughput": {
        "ReadCapacityUnits": 5,
        "WriteCapacityUnits": 5
      }
    }
  ]' \
  --provisioned-throughput \
    ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --endpoint-url http://dynamodb-accenture:8000 \
  --region us-east-1|| echo "⚠️ Table Franchise already exists"

echo "🎉 DynamoDB tables created"

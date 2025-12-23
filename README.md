# prueba-accenture — Prueba técnica (Accenture)

Resumen
-------
Proyecto creado como prueba técnica para Accenture. Está diseñado siguiendo la arquitectura hexagonal (ports & adapters) 
para mostrar la separación clara entre dominio, lógica de aplicación y detalles de infraestructura.

Arquitectura (Hexagonal)
------------------------
- Domain: entidad y reglas de negocio puras. No depende de frameworks ni de I/O.
- Application (o Service): casos de uso que orquestan el dominio.
- Ports (puertos): interfaces que define el dominio/aplicación para acceder a recursos externos (repositorios, mensajería, etc.).
- Adapters (adaptadores / Infrastructure): implementaciones concretas de los puertos (BD, HTTP, files, drivers).

Estructura principal (mapeo a hexagonal)
----------------------------------------
- `domain/` — Modelos de dominio y lógica core (puertos, entidades, value objects).
- `applications/app-service/` — Casos de uso y orquestación de la aplicación.
- `infrastructure/` — Implementaciones concretas de repositorios/adapters.
- `deployment/` — Dockerfile y recursos para desplegar la aplicación.
- `gradle/`, `build/`, `build-cache/` — Configuración y artefactos de build.

Requisitos
---------
- JDK 11+ (o la versión que requiera el proyecto).
- Gradle wrapper incluido: usa `./gradlew`.
- Docker (opcional) para construir y probar imagenes.

Comandos básicos
----------------
Desde la raíz del repositorio:

- Compilar todo:

    ./gradlew build

- Ejecutar tests:

    ./gradlew test

- Limpiar artefactos:

    ./gradlew clean

- Construir imagen Docker:

    docker build -f deployment/Dockerfile -t prueba-accenture:latest .

Notas de ejecución
------------------
- Para trabajar en el módulo `app-service` puedes ejecutar desde la raíz con un task por path, por ejemplo:

    ./gradlew :applications:app-service:build

Base de datos (DynamoDB local)
------------------------------
Esta prueba utiliza Amazon DynamoDB en modo local para facilitar las pruebas. Puedes arrancar una instancia local usando la imagen oficial `amazon/dynamodb-local` y luego crear la tabla `Franchises`.

1) Ejecutar DynamoDB local con Docker:

```bash
# Descargar y ejecutar DynamoDB local en el puerto 8000
docker run -d --name dynamodb-local -p 8000:8000 amazon/dynamodb-local
```

2) Crear la tabla `Franchises` (opción A: si tienes AWS CLI instalado en tu host):

```bash
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
  --endpoint-url http://localhost:8000 \
  --region us-east-1
```

3) Crear la tabla `Franchises` (opción B: usando `amazon/aws-cli` en Docker, útil si no tienes AWS CLI local):

```bash
# En macOS, usamos host.docker.internal para que el contenedor aws-cli alcance el DynamoDB que corre en el host
docker run --rm -e AWS_ACCESS_KEY_ID=dummy -e AWS_SECRET_ACCESS_KEY=dummy amazon/aws-cli \
  dynamodb create-table \
    --table-name franchises \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
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
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://host.docker.internal:8000 \
    --region us-east-1
```

4) Verificar tablas existentes (AWS CLI local):

```bash
aws dynamodb list-tables --endpoint-url http://localhost:8000 --region us-east-1
```

Notas sobre el esquema de la tabla `Franchises`:
- Clave primaria: `id` (String).
- Ajusta atributos y throughput según las necesidades del ejercicio. Para pruebas locales, los valores provisionales son suficientes.

Criterios de evaluación (sugeridos)
-----------------------------------
1. Separación clara entre dominio y detalles de infraestructura (puertos vs adaptadores).
2. Cobertura mínima de tests para casos de uso críticos.
3. Código legible, bien estructurado y con principios SOLID.
4. Capacidad de ejecutar build y tests con `./gradlew` sin errores.
5. Documentación mínima en README y comentarios importantes.


# Prueba técnica (Accenture)

Resumen
-------
Proyecto creado como prueba técnica para Accenture. Está diseñado siguiendo la arquitectura limpia/hexagonal (ports & adapters) 
para mostrar la separación clara entre dominio, lógica de aplicación y detalles de infraestructura, basado en el [plugin](https://bancolombia.github.io/scaffold-clean-architecture/docs/intro) 
de Bancolombia.

Arquitectura Limpia (Hexagonal)
------------------------
- Domain: Casos de uso, entidad y reglas de negocio puras. No depende de frameworks ni de I/O.
- Application (o Service): Orquestacion.
- Ports (puertos): interfaces que define el dominio para acceder a recursos externos (repositorios, mensajería, etc.).
- Adapters (adaptadores / Infraestructure): implementaciones concretas de los puertos (BD, HTTP, files, drivers).

Estructura principal (mapeo a hexagonal)
----------------------------------------
- `domain/` — Modelos de dominio, casos de uso y lógica core (entidades, value objects, use case).
- `applications/app-service/` — Orquestación de la aplicación.
- `infrastructure/` —  Implementaciones concretas de repositorios/adapters.
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


## Docker

En el archivo `docker-compose.yml` esta la configuración para levantar la aplicación en modo Local.

Desde la raíz del repositorio ejecutar el siguiente comando para construir la imagen y levantas el contenedor:

```bash
# Construir imágenes (si es necesario) y levantar servicios en primer plano
docker compose up --build
```

Una vez desplegado el contenedor la url para pruebas es http://localhost:8081/api/v1

Crear la tabla `Franchises` (opciones si el .sh no funciona):

Opción A — desde el host con AWS CLI instalado (usa localhost):

```bash
aws dynamodb create-table \
  --table-name franchises \
  --attribute-definitions AttributeName=id,AttributeType=S AttributeName=name,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --global-secondary-indexes '[{"IndexName":"franchise-name-index","KeySchema":[{"AttributeName":"name","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"},"ProvisionedThroughput":{"ReadCapacityUnits":5,"WriteCapacityUnits":5}}]' \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --endpoint-url http://localhost:8000 \
  --region us-east-1
```

Opción B — usando `amazon/aws-cli` en Docker (útil si no tienes AWS CLI local):

```bash
# En macOS usa host.docker.internal para que el contenedor aws-cli alcance DynamoDB que corre en el host
docker run --rm -e AWS_ACCESS_KEY_ID=dummy -e AWS_SECRET_ACCESS_KEY=dummy amazon/aws-cli \
  dynamodb create-table \
    --table-name franchises \
    --attribute-definitions AttributeName=id,AttributeType=S AttributeName=name,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --global-secondary-indexes '[{"IndexName":"franchise-name-index","KeySchema":[{"AttributeName":"name","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"},"ProvisionedThroughput":{"ReadCapacityUnits":5,"WriteCapacityUnits":5}}]' \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://host.docker.internal:8000 \
    --region us-east-1
```

## Swagger Docs

```
URL para swagger http://localhost:8081/swagger-ui/index.html
```

Resumen del esquema de la tabla `Franchises`:
- Clave primaria: `id` (String).
- Índice secundario: `franchise-name-index` (hash sobre `name`), proyección `ALL`.

Test unitarios
-----------------------------------

Se incluyen tests unitarios para el domain. Los tests están ubicados en los módulos correspondientes bajo `src/test/java`.
Para ejecutar los tests, usa el comando:

    ./gradlew test
y para un reporte con Jacoco usa el comando:

    ./gradlew test jacocoTestReport

Postman
-----------------------------------
Para facilitar las pruebas de los endpoints, se anexa el URL de una colección de Postman que puedes importar en tu entorno de Postman:
[Postman Collection - Accenture Technical Test](https://documenter.getpostman.com/view/14774609/2sBXVZoEKT)

Criterios de evaluación (sugeridos)
-----------------------------------
1. Separación clara entre dominio y detalles de infraestructura (puertos vs adaptadores).
2. Cobertura mínima de tests para casos de uso críticos.
3. Código legible, bien estructurado y con principios SOLID.
4. Capacidad de ejecutar build y tests con `./gradlew` sin errores.
5. Documentación mínima en README y comentarios importantes.

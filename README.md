Prueba de concepto para entusiastas.

Compilación con PODMAN:
mvn clean package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman -Dquarkus.native.builder-image=registry.access.redhat.com/quarkus/mandrel-22-rhel8:latest -DskipTests=true

Java version utilizado desde SKDMAN:
22.3.5.r17-mandrel

Crear la BD de ORACLE con PODMAN:
podman run --name oracle_11_2_0_2 -d -p 1521:1521 --tz=America/Bogota -e ORACLE_PASSWORD=oracle11202 docker.io/gvenzl/oracle-xe:11.2.0.2

Query para crear la tabla:
CREATE TABLE ALGUNATABLA (ID NUMBER, NOMBRE VARCHAR(50))

Consideraciones:
Ajustar la variable SQL_QUERY_PRODUCER si acaso el query apunta a un esquema diferente creado.
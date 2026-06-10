docker compose down

docker compose up -d --build

./mvnw dependency:purge-local-repository clean test-compile

./mvnw compile

./mvnw spring-boot:run

./mvnw clean test
    
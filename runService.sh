## Runs Service
echo "starting containers"
docker-compose -f docker-compose.yml up -d --remove-orphans
sleep 3

echo "running all tests"
./gradlew clean test
if [ $? -ne 0 ]; then
    echo "Tests Failed"
    docker-compose -f docker-compose.yml down --remove-orphans
    exit 1
fi

echo "Building jar"
./gradlew clean build

echo "importing environment arguments"
SERVER_PORT="8187"
DB_URL="jdbc:mysql://127.0.0.1:3307/demo"
DB_USER="root"
DB_PASSWORD="root"

echo "starting the service"
java -jar build/libs/demo-0.0.1.jar

echo "stopping containers"
docker-compose -f docker-compose.yml down --remove-orphans
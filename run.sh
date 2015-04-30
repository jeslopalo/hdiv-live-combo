#!/bin/sh

echo "Running the application..."
mvn package && java -jar target/hdiv-live-combo-0.0.1-SNAPSHOT.jar

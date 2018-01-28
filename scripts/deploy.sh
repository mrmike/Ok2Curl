#!/bin/sh

# Go to root directory
cd ..

# Call bintrayUpload task
./gradlew :ok2curl:bintrayUpload


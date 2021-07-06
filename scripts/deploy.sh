#!/bin/sh

./gradlew clean ok2curl:build
./gradlew ok2curl:publishReleasePublicationToSonatypeRepository
./gradlew closeAndReleaseSonatypeStagingRepository
d d .#!/bin/bash

# Script untuk menjalankan mTLS demo
# Usage: ./run-mtls-demo.sh

echo "Running mTLS Socket Example..."
echo "Make sure you have generated certificates first with: cd mtls && ./generate_mtls_certs.sh"
echo ""

# Navigate to project root and run Java program
cd code/java
./mvnw -f pom.xml exec:java -Dexec.mainClass="org.harvanir.security.example.mtls.MtlsSocketExample"
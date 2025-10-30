#!/bin/bash
set -euo pipefail

# Configurable passwords (ganti sesuai kebutuhan)
CA_PASS="capass"
SERVER_PASS="serverpass"
CLIENT_PASS="clientpass"
STORE_PASS="storepass"   # untuk PKCS12 keystore dan truststore

OUTDIR="./mtls-certs"

# Hapus file-file yang sebelumnya di-copy ke parent directory
echo "Membersihkan file-file lama..."
rm -f server.p12 client.p12 server-trust.p12 client-trust.p12 ca.crt.pem

# Hapus direktori mtls-certs beserta isinya untuk memulai dari awal
rm -rf "$OUTDIR"

mkdir -p "$OUTDIR"
cd "$OUTDIR"

echo "1) Membuat CA (private key + self-signed cert)"
openssl genpkey -algorithm RSA -out ca.key.pem -pkeyopt rsa_keygen_bits:4096
openssl req -x509 -new -nodes -key ca.key.pem -sha256 -days 3650 \
  -subj "/CN=Test-Root-CA" -out ca.crt.pem

echo "2) Membuat Server key + CSR"
openssl genpkey -algorithm RSA -out server.key.pem -pkeyopt rsa_keygen_bits:2048
openssl req -new -key server.key.pem -subj "/CN=localhost" -out server.csr.pem

echo "3) Sign server CSR with CA"
openssl x509 -req -in server.csr.pem -CA ca.crt.pem -CAkey ca.key.pem -CAcreateserial \
  -out server.crt.pem -days 825 -sha256

echo "4) Membuat Client key + CSR"
openssl genpkey -algorithm RSA -out client.key.pem -pkeyopt rsa_keygen_bits:2048
openssl req -new -key client.key.pem -subj "/CN=client-001" -out client.csr.pem

echo "5) Sign client CSR with CA"
openssl x509 -req -in client.csr.pem -CA ca.crt.pem -CAkey ca.key.pem -CAcreateserial \
  -out client.crt.pem -days 825 -sha256

# Create PKCS12 keystores (server.p12 includes server key + cert chain)
echo "6) Membuat PKCS#12 keystore untuk server (server.p12)"
openssl pkcs12 -export \
  -in server.crt.pem -inkey server.key.pem -certfile ca.crt.pem \
  -name server \
  -passout pass:"$SERVER_PASS" \
  -out server.p12

echo "7) Membuat PKCS#12 keystore untuk client (client.p12)"
openssl pkcs12 -export \
  -in client.crt.pem -inkey client.key.pem -certfile ca.crt.pem \
  -name client \
  -passout pass:"$CLIENT_PASS" \
  -out client.p12

# Create truststores (PKCS12) containing CA only, used to verify peer certs
echo "8) Membuat truststore untuk server (server-trust.p12) berisi CA"
# first create a temporary PEM file with CA
cp ca.crt.pem ca-only.crt.pem
keytool -importcert -noprompt \
  -alias root-ca -file ca-only.crt.pem \
  -keystore server-trust.p12 -storetype PKCS12 -storepass "$STORE_PASS"

echo "9) Membuat truststore untuk client (client-trust.p12) berisi CA"
keytool -importcert -noprompt \
  -alias root-ca -file ca-only.crt.pem \
  -keystore client-trust.p12 -storetype PKCS12 -storepass "$STORE_PASS"

# Copy artifacts to top-level for convenience
cp server.p12 client.p12 server-trust.p12 client-trust.p12 ca.crt.pem ../
cd ..
echo "Selesai. File yang dibuat:"
ls -la mtls-certs server.p12 client.p12 server-trust.p12 client-trust.p12 ca.crt.pem

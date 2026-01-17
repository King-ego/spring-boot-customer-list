FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
WORKDIR /tests_attack

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    procps \
    && curl -fsSL https://dl.k6.io/key.gpg | gpg --dearmor -o /usr/share/keyrings/k6-archive-keyring.gpg \
    && echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" \
       | tee /etc/apt/sources.list.d/k6.list \
    && apt-get update && apt-get install -y k6 \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY . .

ENV EXPECTED_AUTH="muda_essa_senha"

CMD ["tail", "-f", "/dev/null"]

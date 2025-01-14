FROM adoptopenjdk:17-jdk-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl gcc g++ make \
    linux-perf \
    ca-certificates \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://github.com/JetBrains/kotlin/releases/download/v1.9.0/kotlin-compiler-1.9.0.zip -o kotlin-compiler.zip && \
    apt-get install -y unzip && \
    unzip kotlin-compiler.zip -d /opt/kotlin && \
    rm kotlin-compiler.zip && \
    ln -s /opt/kotlin/bin/kotlinc /usr/bin/kotlinc && \
    ln -s /opt/kotlin/bin/kotlin /usr/bin/kotlin

WORKDIR /app

COPY <<'EOF' /usr/local/bin/entrypoint.sh
#!/bin/bash
set -e
kernel_version=$(uname -r)
ln -sf /usr/lib/linux-tools/$kernel_version/perf /usr/bin/perf || true
exec "$@"
EOF

RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

CMD ["tail", "-f", "/dev/null"]

FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    linux-tools-generic \
    linux-tools-common \
    && apt-get clean

RUN kernel_version=$(uname -r) && \
    ln -sf /usr/lib/linux-tools/*/perf /usr/bin/perf_${kernel_version} && \
    ln -sf /usr/bin/perf_${kernel_version} /usr/bin/perf

WORKDIR /app
RUN mkdir -p /app/submits

COPY <<'EOF' /usr/local/bin/entrypoint.sh
#!/bin/bash
kernel_version=$(uname -r)
ln -sf /usr/lib/linux-tools/*/perf /usr/bin/perf_${kernel_version}
ln -sf /usr/bin/perf_${kernel_version} /usr/bin/perf
exec "$@"
EOF

RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["tail", "-f", "/dev/null"]
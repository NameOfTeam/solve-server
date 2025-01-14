FROM ubuntu:22.04

# Install OpenJDK 17 and perf
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    linux-tools-generic \
    linux-tools-common \
    && apt-get clean

# LinuxKit 커널 버전용 symbolic link 생성
RUN kernel_version=$(uname -r) && \
    ln -sf /usr/lib/linux-tools/*/perf /usr/bin/perf_${kernel_version} && \
    ln -sf /usr/bin/perf_${kernel_version} /usr/bin/perf

WORKDIR /app
RUN mkdir -p /app/submits

# 컨테이너 실행 시 커널 버전 확인 및 perf 설정을 위한 entry point 스크립트
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
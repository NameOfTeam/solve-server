# 베이스 이미지: Debian
FROM debian:bullseye

# 필수 패키지 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl gcc g++ make \
    linux-perf \
    ca-certificates \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Node.js 설치 (최신 LTS 버전)
RUN curl -fsSL https://deb.nodesource.com/setup_lts.x | bash - && \
    apt-get install -y --no-install-recommends nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 디렉토리 설정
WORKDIR /app

# entrypoint 스크립트 복사 및 실행 권한 설정
COPY <<'EOF' /usr/local/bin/entrypoint.sh
#!/bin/bash
set -e
kernel_version=$(uname -r)
ln -sf /usr/lib/linux-tools/$kernel_version/perf /usr/bin/perf || true
exec "$@"
EOF

RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

# 기본 실행 명령
CMD ["tail", "-f", "/dev/null"]

FROM ubuntu:22.04

# 필수 패키지 설치
RUN apt-get update && apt-get install -y \
    gcc g++ make \
    linux-tools-generic \
    linux-tools-common \
    && apt-get clean

# 디렉토리 설정
WORKDIR /app

# entrypoint 스크립트 복사 및 실행 권한 설정
COPY <<'EOF' /usr/local/bin/entrypoint.sh
#!/bin/bash
kernel_version=$(uname -r)
ln -sf /usr/lib/linux-tools/$kernel_version/perf /usr/bin/perf || true
exec "$@"
EOF

RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

CMD ["tail", "-f", "/dev/null"]

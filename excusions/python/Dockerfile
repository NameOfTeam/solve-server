FROM python:3.11
RUN apt-get update && apt-get install -y \
    linux-perf \
    && apt-get clean
RUN chmod +s /usr/bin/perf || true
WORKDIR /app
RUN mkdir -p /app/submits
CMD ["python3"]
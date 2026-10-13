
FROM python:3.12-slim

ENV PIP_DISABLE_PIP_VERSION_CHECK=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
    && apt-get install -y --no-install-recommends docker-cli \
    && command -v docker \
    && pip install --no-cache-dir ansible-core \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace

CMD ["ansible-playbook", "-i", "ansible/inventory.ini", "ansible/playbook.yml"]

FROM mcr.microsoft.com/devcontainers/java:21

# Instalações adicionais
RUN apt-get update && apt-get install -y \
    git \
    maven \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*
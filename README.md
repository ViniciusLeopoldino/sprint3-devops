# Mottu Control - Projeto FIAP (Java, Docker & Azure)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)
![Docker](https://img.shields.io/badge/Docker-blue.svg)
![Azure](https://img.shields.io/badge/Azure-ACI%20%26%20ACR-blue)

## 1. Descrição da Solução

O **Mottu Control** é uma API RESTful desenvolvida em Java com o framework Spring Boot, projetada para gerenciar o cadastro de motocicletas da empresa Mottu. A solução permite a realização de um CRUD completo (Criação, Leitura, Atualização e Exclusão) sobre uma tabela de motos.

Toda a infraestrutura da aplicação, incluindo o banco de dados PostgreSQL, é containerizada com Docker e implantada na nuvem da Microsoft Azure, utilizando o Azure Container Registry (ACR) para armazenamento de imagens e o Azure Container Instances (ACI) para a execução dos containers.

## 2. Benefícios para o Negócio

Esta solução foi projetada para resolver problemas de controle de inventário manual e descentralizado, trazendo os seguintes benefícios:
* **Centralização e Acuracidade dos Dados:** Garante que as informações da frota sejam consistentes, confiáveis e acessíveis a partir de um único ponto.
* **Agilidade Operacional:** Permite que as equipes consultem, adicionem ou removam motocicletas do sistema de forma rápida e programática.
* **Escalabilidade:** Por ser baseada em nuvem e containers, a solução pode escalar facilmente para suportar o crescimento da frota da Mottu.
* **Fundação para o Futuro:** Serve como a base tecnológica para o desenvolvimento de novas funcionalidades, como sistemas de aluguel, agendamento de manutenção e rastreamento.

## 3. Arquitetura da Solução

O fluxo da solução segue as práticas modernas de DevOps e Cloud Computing:

```
+---------------+      +----------------+      +------------------+
|               |      |                |      |                  |
|  Desenvolvedor|----->|     GitHub     |----->|      Azure       |
|               |      | (Código Fonte) |      | (Nuvem/Cloud)    |
+---------------+      +----------------+      +------------------+
      |                                                |
      | 1. Desenvolve o código e o Dockerfile          | 5. Executa os scripts de deploy (ACI)
      | 2. Envia para o GitHub (push)                  |
      |                                                V
      |------------------------------------------------+
      | 3. Clona o repo localmente                     |
      | 4. Constrói a imagem e envia para o ACR        |
      |    (docker build/push)                         |
      +------------------------------------------------+

      -------------------------- Nuvem Azure ---------------------------
      |                                                                 |
      |  +---------------------------+       +------------------------+ |
      |  |  Azure Container Registry |       |   Grupo de Recursos    | |
      |  |      (ACR)                |<------|                        | |
      |  |  [Armazena imagem da App] |       | +--------------------+ | |
      |  +---------------------------+       | | App Java (ACI)     | | |
      |                                      | | [Container]        | | |
      |                                      | +--------+-----------+ | |
      |                                      |          |             | |
      |                                      |          V             | |
      |                                      | +--------+-----------+ | |
      |                                      | | Banco PG (ACI)     | | |
      |                                      | | [Container]        | | |
      |                                      | +--------------------+ | |
      |                                      +------------------------+ |
      |                                                                 |
      -------------------------------------------------------------------
                                       ^
                                       |
                                +------+------+
                                |             |
                                |   Usuário   |
                                |  (Testes)   |
                                +-------------+
```
**Funcionamento:** O código-fonte, versionado no GitHub, é usado para construir uma imagem Docker que é armazenada no ACR. Em seguida, via scripts de linha de comando, dois containers são provisionados no ACI: um para o banco de dados PostgreSQL e outro para a aplicação Java, que se conecta ao banco.

## 4. Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot 3, Spring Data JPA
* **Banco de Dados:** PostgreSQL 15
* **Build:** Apache Maven
* **Containerização:** Docker
* **Cloud:** Microsoft Azure (Azure CLI, ACR, ACI)
* **Controle de Versão:** Git & GitHub

## 5. Pré-requisitos

Para realizar o deploy desta solução, você precisará ter as seguintes ferramentas instaladas e configuradas em sua máquina local:
* Git
* JDK 17 ou superior
* Docker Desktop (em execução)
* Azure CLI (logado com `az login`)

## 6. Guia de Deploy Passo a Passo

Siga os passos abaixo no seu terminal local (PowerShell recomendado) para implantar a solução completa na Azure.

### Passo 1: Clone o Repositório
```powershell
git clone https://github.com/ViniciusLeopoldino/sprint3-devops.git
cd mottu-control
```

### Passo 2: Execute o Script de Deploy Completo
O script a seguir automatiza todo o processo. Copie o bloco inteiro, cole no seu terminal e execute.

**Atenção:** Lembre-se de trocar o valor da variável `$env:POSTGRES_PASSWORD` por uma senha forte de sua escolha.

```powershell
# ===================================================================
# ROTEIRO COM POSTGRESQL
# ===================================================================

# ----- Bloco de Variáveis (AJUSTE AS CONFIGURAÇÕES DE NOME UNICO E SENHA) -----
$env:RESOURCE_GROUP="rg-mottu-fiap"
$env:LOCATION="brazilsouth"
$env:ACR_NAME="acrmottu<seu-nome-unico>" # Ex: acrmottu2025
$env:APP_CONTAINER_NAME="java-app-mottu"
$env:POSTGRES_CONTAINER_NAME = "postgres-db-mottu"
$env:POSTGRES_DB = "mottudb"
$env:POSTGRES_USER = "mottuadmin"
$env:POSTGRES_PASSWORD = "SuaSenhaForte123" # <-- INCLUIR A SENHA AQUI

# ----- PASSO 1: Criar Recursos Base -----
Write-Host "Criando Grupo de Recursos e Azure Container Registry..."
az group create --name $env:RESOURCE_GROUP --location $env:LOCATION
az acr create --resource-group $env:RESOURCE_GROUP --name $env:ACR_NAME --sku Basic --admin-enabled true

# ----- PASSO 2: Fazer Build e Push da Imagem da Aplicação -----
Write-Host "Fazendo login, build e push da imagem Docker..."
az acr login --name $env:ACR_NAME
docker build -t "$($env:ACR_NAME).azurecr.io/mottu-control:v1" .
docker push "$($env:ACR_NAME).azurecr.io/mottu-control:v1"

# ----- PASSO 3: Criar o Container do PostgreSQL -----
Write-Host "Criando o container do PostgreSQL..."
az container create --resource-group $env:RESOURCE_GROUP --name $env:POSTGRES_CONTAINER_NAME --image postgres:15 --os-type Linux --ports 5432 --cpu 1 --memory 1.5 --environment-variables "POSTGRES_DB=$($env:POSTGRES_DB)" "POSTGRES_USER=$($env:POSTGRES_USER)" "POSTGRES_PASSWORD=$($env:POSTGRES_PASSWORD)" --dns-name-label postgres-mottu-$($env:ACR_NAME)

# ----- PASSO 4: Aguardar e Obter o IP do Banco de Dados -----
Write-Host "Aguardando 60 segundos para o PostgreSQL iniciar..."
Start-Sleep -Seconds 60
$POSTGRES_IP = $(az container show --resource-group $env:RESOURCE_GROUP --name $env:POSTGRES_CONTAINER_NAME --query ipAddress.ip --output tsv)
Write-Host "IP do PostgreSQL obtido: $POSTGRES_IP"

# ----- PASSO 5: Criar o Container da Aplicação -----
$DB_URL = "jdbc:postgresql://$($POSTGRES_IP):5432/$($env:POSTGRES_DB)"
$ACR_PASSWORD = $(az acr credential show --name $env:ACR_NAME --query "passwords[0].value" --output tsv)

Write-Host "Criando o container da aplicação Java..."
az container create --resource-group $env:RESOURCE_GROUP --name $env:APP_CONTAINER_NAME --image "$($env:ACR_NAME).azurecr.io/mottu-control:v1" --os-type Linux --ports 8080 --cpu 1 --memory 1.5 --registry-username $env:ACR_NAME --registry-password $ACR_PASSWORD --environment-variables "DB_URL=$($DB_URL)" "DB_USER=$($env:POSTGRES_USER)" "DB_PASSWORD=$($env:POSTGRES_PASSWORD)" --dns-name-label app-mottu-$($env:ACR_NAME)

# ----- PASSO 6: Obter IP da Aplicação e Finalizar -----
Write-Host "Aguardando 90 segundos para a aplicação iniciar (incluindo o 'sleep' interno)..."
Start-Sleep -Seconds 90
$APP_IP = $(az container show --resource-group $env:RESOURCE_GROUP --name $env:APP_CONTAINER_NAME --query ipAddress.ip --output tsv)
Write-Host "🚀 Aplicação pronta! Acesse em: http://$APP_IP:8080/api/motos"
```

## 7. Como Testar a API

Após a execução do script de deploy, a URL da sua API será exibida no final. Use essa URL para realizar os testes abaixo com `curl` ou Postman.

(Substitua `<IP_DA_SUA_APP>` pelo IP real da sua aplicação)

### CREATE (POST)
Cria uma nova moto.
```bash
curl -X POST -H "Content-Type: application/json" -d '{"modelo": "Honda Pop 110i", "placa": "BRA2E19", "ano": 2025}' http://<IP_DA_SUA_APP>:8080/api/motos
```

### READ (GET)
Lista todas as motos.
```bash
curl http://<IP_DA_SUA_APP>:8080/api/motos
```
Busca uma moto pelo ID.
```bash
curl http://<IP_DA_SUA_APP>:8080/api/motos/1
```

### UPDATE (PUT)
Atualiza a moto com o ID especificado.
```bash
curl -X PUT -H "Content-Type: application/json" -d '{"modelo": "Honda Pop 110i EX", "placa": "BRA2E19", "ano": 2026}' http://<IP_DA_SUA_APP>:8080/api/motos/1
```

### DELETE
Remove a moto com o ID especificado.
```bash
curl -X DELETE http://<IP_DA_SUA_APP>:8080/api/motos/1
```

## 8. Autor

* **[Seu Nome Completo]** - RM [Seu RM]

```

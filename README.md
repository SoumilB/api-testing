# Generalized Data API

## APIs

### 1. Get Databases:
- Get the available databases

### 2. Get Datasets:
- Get all the datasets for a give database. Schema of the datasets will also be included

### 3. Get a Dataset Schema
- Get schema for a give dataset

### 4. Basic Query
- Query dataset using simple GET method

### 5. Composite Query
- Query dataset using POST method and building more complicated query using json structure


## Steps to deploy to Azure App Service
1. Create and Azure App Service with linux os and java11 runtime stack

2. Configure maven Azure App Service configurations with following command:

    - `mvn com.microsoft.azure:azure-webapp-maven-plugin:1.11.0:config`

3. Re-Package the application into jar using following command:
    - `mvn clean package`
4. Deploy to Azure App Service using following command:
    - `mvn azure-webapp:deploy`
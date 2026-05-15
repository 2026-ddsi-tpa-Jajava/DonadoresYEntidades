# Diagrama de despliegue

````mermaid
flowchart LR

    %% Cliente
    subgraph NodoCliente["Nodo: Cliente"]
        Cliente[Cliente]
    end

    %% API Gateway
    subgraph NodoAPIGateway["Nodo: API Gateway"]
        APIGateway[API Gateway]
    end

    %% Servicios
    subgraph NodoDonadoresYEntidades["Nodo: Servicio de Donadores y Entidades"]
        DonadoresYEntidades[Servicio de Donadores y Entidades]
    end

    subgraph NodoDonaciones["Nodo: Servicio de Donaciones"]
        Donaciones[Servicio de Donaciones]
    end

    subgraph NodoIncentivos["Nodo: Servicio de Incentivos"]
        Incentivos[Servicio de Incentivos]
    end

    subgraph NodoLogistica["Nodo: Servicio de Logística"]
        Logistica[Servicio de Logística]
    end

    %% Conexiones
    Cliente --> APIGateway
    APIGateway --> Donaciones
    APIGateway --> Logistica
    APIGateway --> Incentivos
    APIGateway --> DonadoresYEntidades
    DonadoresYEntidades  --> Incentivos
````

# Diagrama de componentes

```mermaid
flowchart TB

    %% Servicio Donadores y Entidades - componentes internos
    subgraph ServicioDonadoresYEntidades["Diagrama: Donadores y Entidades"]
        direction TB
        FachadaDonadores["Fachada: DonadoresYEntidades"]
        subgraph Repositorios["InMemory Repositories (persistencia en memoria)"]
            DonadorRepo["InMemoryDonadoresRepo<br>(InMemoryRepo<Donador>)"]
            EntidadRepo["InMemoryEntidadesRepo<br>(InMemoryRepo<Entidad>)"]
            NecesidadRepo["InMemoryNecesidadesRepo<br>(InMemoryRepo<Necesidad>)"]
            QuejaRepo["InMemoryQuejasRepo<br>(InMemoryRepo<Queja>)"]
        end
        IncentivosFacade["Fachada: Incentivos<br>(interface)"]
    end

    %% Conexiones: quién usa a quién (métodos relevantes entre paréntesis)
    FachadaDonadores -->|usa / guarda| DonadorRepo
    FachadaDonadores -->|usa / guarda| EntidadRepo
    FachadaDonadores -->|usa / guarda| NecesidadRepo
    FachadaDonadores -->|usa / guarda| QuejaRepo
    FachadaDonadores -->|consulta / provee| IncentivosFacade

    %% Notas visuales
    classDef repo fill:#15f,stroke:#333,stroke-width:1px;
    class DonadorRepo,EntidadRepo,NecesidadRepo,QuejaRepo repo;

    %% Agrupar explicación rápida

```
# Prueba técnica - Mediprocesosr

Prueba técnica **Mediprocesos**.

# Requisitos Previos

- Docker y Docker Compose
- Git
- PowerShell (en Windows) o terminal equivalente

# Estructura del Proyecto

```
Prueba-Mediprocesos/
├── frontend/                 # Aplicación Angular
│   ├── src/
│   ├── public/
│   ├── Dockerfile
│   ├── package.json
│   └── angular.json
├── backend/                  # API Spring Boot
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── mvnw
│   └── mvnw.cmd
├── database/                 # Scripts SQL Server
│   ├── init.sql
│   └── seed-data.sql
├── docker-compose.yml        # Orquestación de servicios
└── README.md
```

# Configuración e Instalación

## 1. Clonar el repositorio

```bash
git clone https://github.com/eliasalvarado/Prueba-Mediprocesos
cd Prueba-Mediprocesos
```

## 2. Montar los servicios con Docker Compose

```powershell
docker-compose down -v
docker-compose up -d --build
```

## 3. Verificar que los servicios están corriendo

```powershell
docker-compose ps
```

# Backend

## Base de Datos

La base de datos se inicializa automáticamente con:

- 2 roles: ADMIN, EMPLEADO
- 1 usuario admin (username: admin, password: admin)
- 1 empleado de prueba (Juan Pérez García)

## Endpoints Disponibles

### Autenticación

POST /api/auth/login
- Body: {"username": "admin", "password": "admin"}
- Retorna token JWT y datos del usuario

POST /api/auth/logout
- Requiere autenticación
- Invalida la sesión

### Empleados (CRUD)

GET /api/empleados
- Lista todos los empleados
- Requiere autenticación

GET /api/empleados/{id}
- Obtiene un empleado por ID
- Requiere autenticación

POST /api/empleados
- Crea un nuevo empleado (requiere rol ADMIN)
- Body: nombre, apellidos, numeroIdentificacion, fechaNacimiento, genero, username, password
- Crea automáticamente un usuario asociado

PUT /api/empleados/{id}
- Actualiza un empleado (requiere rol ADMIN)
- Body: nombre, apellidos, numeroIdentificacion, fechaNacimiento, genero

DELETE /api/empleados/{id}
- Elimina un empleado (requiere rol ADMIN)
- También elimina el usuario asociado

---

# Parte teórica
1. ¿Qué es un API REST y cómo se diferencia de SOAP ?
Una API en sí es la forma en la que dos sistemas puedan comunicarse bajo contratos (conjunto de reglas) para que ambas partes saben qué envíar y qué recibir. Esta filosifía apoya y da la independencia/autonomía de que cualquiera de las dos partes evolucionen o cambien y que la otra parte le sea indiferente esto, siempre y cuando se respete el protocolo y los contratos definidos. 
Una API REST es una forma de comunicación, pero utilizando el protocolo HTTP, esto implica el uso de los métodos estándar GET, POST, PUT, DELETE. 

2. ¿Qué son y para que son útiles los hilos? De un ejemplo.
Un hilo es un proceso, que en escencia siempre se asocia a la ejecución concurrente. Son útiles para poder ejecutar tareas que pueden llegar a seguir ejecutadas paralelamente. Los hilos apoyan a esta gestión, ya que permiten la ejecución concurrente siendo capaz de entregar casi un paralelismo en tareas dentro de un procesamiento general. Por lo que sirven en la mejora de rendimiento y capacidad de respuesta de ciertas tareas que permiten ser divididas en estos hilos.

Un ejemplo podría ser si se tiene una operación que no importa cómo llegue a ser ejecutada, su resultado terminará siendo el mismo. Como una multiplicación, el orden de los productos no afecta el resultado.
Por lo que si tenemos una multiplicación que por alguna razón es una iteración secuencial como multiplicar los números de 1-100. Se pueden tener 2 hilos que hagan la multiplicación de los primeros 50 números y otro hilo que haga la multiplicación del restante. Haciendo posible que el tiempo de respuesta se reduzca entregando un resultado correcto.

3. ¿Qué estructuras de datos se pueden utilizar para almacenar y manipular colecciones de elementos en Java?
Array, List, Stack, Queues.

4. ¿Para manejo de base de datos que herramienta se puede utilizar en Spring boot?
Depende de qué base de datos, pero para SQL Server por ejemplo se puede optar por la herramienta Spring Data JPA.

5. ¿En qué se basa la autenticación por JWT y en que se diferencia de la autenticación básica?
JWT ofrece la facilidad de la autenticación y autorización, se basa en la generación de un Token capaz de almacenar información crucial, así como firmar electrónicamente el objeto para asegurar su integridad. La principal diferencia podría caer en que JWT es stateless, mientras que la autenticación básica es stateful. Además, de que una autenticación básica puede llegar a requerir validar credenciales en cada solicitud.

6. De forma general ¿Qué es y para qué sirven los pipeline en CI/CD?
Los pipeline en CI/CD sirven para definir un flujo. Una serie de pasos a seguir que permiten automatizar un ciclo. Esto hace que una serie de pasos puedan ser orquestados y eviten la fatiga y posible error humano al ser pasos automatizados.

# Documentación del Proyecto Bancario con Spring Boot y Spring Security

## Índice
1. [Introducción](#introducción)
2. [Estructura del Proyecto](#estructura-del-proyecto)
   - [Entities (Entidades)](#entities)
   - [Repositories (Repositorios)](#repositories)
   - [Services (Servicios) y ServicesImpl](#services)
   - [Controllers (Controladores)](#controllers)
   - [Security (Seguridad)](#security)
   - [Validation (Validación)](#validation)
3. [Flujo de Autenticación y Autorización](#autenticación)
4. [Manejo de Roles](#roles)
5. [Interacción entre Componentes](#interacción)
6. [Consideraciones de Seguridad](#seguridad-consideraciones)
7. [Conclusión](#conclusión)

---

## 1. Introducción <a name="introducción"></a>

Este proyecto es una aplicación bancaria desarrollada utilizando Spring Boot como framework principal y Spring Security para gestionar la autenticación y autorización. La aplicación está diseñada para manejar diferentes tipos de usuarios, como administradores, gerentes, ejecutivos, cajeros y clientes, cada uno con niveles de acceso y permisos específicos. Además, se implementa JWT (JSON Web Tokens) para asegurar las comunicaciones y proteger los endpoints de la API.

---

## 2. Estructura del Proyecto <a name="estructura-del-proyecto"></a>

El proyecto está organizado en varios paquetes que segregan las distintas responsabilidades y funcionalidades. A continuación, se detalla cada uno de estos paquetes y sus componentes principales.

### 2.1 Entities (Entidades) <a name="entities"></a>

Las entidades representan las tablas de la base de datos y son la base para la persistencia de datos en la aplicación. Las principales entidades incluyen:

- **BanamexOaxaca**: Representa la sucursal bancaria y actúa como la entidad principal del proyecto.
  
- **Persona**: Entidad base que modela a una persona en el sistema. Sirve como plantilla para otras entidades más específicas.
  
- **Personal**: Extiende de `Persona` y representa al personal del banco. Se subdivide en roles específicos como Gerente, Cajero y Ejecutivo.
  
- **Cliente**: También extiende de `Persona` y representa a los clientes del banco, quienes tienen acceso a sus cuentas y transacciones.
  
- **Cuenta**: Modela las cuentas bancarias de los clientes, incluyendo detalles como el saldo y el tipo de cuenta.
  
- **Tarjeta**: Representa las tarjetas de crédito o débito asociadas a las cuentas bancarias.
  
- **Transaccion**: Modela las transacciones financieras realizadas en las cuentas de los clientes.
  
- **User**: Utilizada para gestionar las credenciales de los usuarios que acceden a la aplicación. El nombre de usuario (`username`) corresponde al RFC de la persona.
  
- **Role**: Enumera los diferentes roles que puede tener un usuario en la aplicación, como ADMIN, GERENTE, EJECUTIVO, CAJERO y CLIENTE.

### 2.2 Repositories (Repositorios) <a name="repositories"></a>

Los repositorios son interfaces que extienden de `JpaRepository` y facilitan las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre las entidades. Cada entidad tiene su propio repositorio, lo que permite una interacción eficiente con la base de datos.

Algunos de los repositorios clave incluyen:

- **BanamexOaxacaRepository**: Gestiona las operaciones relacionadas con la entidad `BanamexOaxaca`.
- **GerenteRepository**: Maneja las operaciones para los gerentes.
- **ClienteRepository**: Gestiona las operaciones para los clientes.
- **CuentaRepository**: Encargado de las operaciones relacionadas con las cuentas bancarias.
- **TransaccionRepository**: Maneja las transacciones financieras.

Estos repositorios permiten abstraer la lógica de acceso a datos, facilitando el mantenimiento y la escalabilidad del proyecto.

### 2.3 Services (Servicios) y ServicesImpl <a name="services"></a>

La capa de servicios encapsula la lógica de negocio de la aplicación. Cada entidad principal cuenta con su propio servicio, que define las operaciones disponibles, y una implementación (`ServiceImpl`) que realiza las acciones correspondientes utilizando los repositorios.

Por ejemplo:

- **CuentaService**: Define operaciones como abrir una cuenta, consultar el saldo y realizar transacciones.
- **CuentaServiceImpl**: Implementa las operaciones definidas en `CuentaService`, interactuando con `CuentaRepository` para persistir y recuperar datos.

Esta separación permite una mejor organización del código y facilita las pruebas unitarias y de integración.

### 2.4 Controllers (Controladores) <a name="controllers"></a>

Los controladores exponen los endpoints de la API REST y gestionan las solicitudes HTTP entrantes. Cada entidad o grupo de entidades tiene su propio controlador que maneja las operaciones relacionadas, como crear, actualizar, eliminar o consultar datos.

Características principales de los controladores:

- **Manejo de Solicitudes**: Gestionan los distintos tipos de solicitudes HTTP (GET, POST, PUT, DELETE) y las dirigen a los servicios correspondientes.
- **Validación de Datos**: Antes de procesar una solicitud, se valida la información recibida para asegurar la integridad de los datos.
- **Seguridad**: Cada controlador está protegido mediante roles específicos, garantizando que solo los usuarios autorizados puedan acceder a determinados endpoints.

Por ejemplo, el `ClienteController` permite a los clientes gestionar sus cuentas y realizar transacciones, mientras que el `GerenteController` ofrece funcionalidades adicionales para la gestión del personal y las sucursales.

### 2.5 Security (Seguridad) <a name="security"></a>

El paquete de seguridad implementa Spring Security junto con JWT para manejar la autenticación y autorización en la aplicación.

Componentes principales:

- **Configuración de Seguridad**: Define las políticas de seguridad, especificando qué endpoints requieren autenticación y qué roles están autorizados para acceder a cada recurso.
- **JWT Token Provider**: Encargado de generar y validar los tokens JWT utilizados para autenticar las solicitudes.
- **Filtros de Seguridad**: Interceptan las solicitudes entrantes para verificar la presencia y validez del token JWT antes de permitir el acceso a los recursos protegidos.
- **Servicios de Usuario y Rol**: Gestionan la carga de detalles de usuario y la asignación de roles, permitiendo que la autenticación se base en la información almacenada en la base de datos.

Este enfoque garantiza que todas las interacciones con la API sean seguras y que los usuarios solo puedan acceder a las funcionalidades para las que están autorizados.

### 2.6 Validation (Validación) <a name="validation"></a>

El paquete de validación se encarga de asegurar que los datos enviados a los endpoints cumplan con las reglas de negocio y los requisitos de integridad antes de ser persistidos en la base de datos.

Funciones principales:

- **Validación de Atributos**: Verifica que los atributos de las entidades cumplan con los formatos y restricciones definidos (por ejemplo, formatos de RFC, números de cuenta válidos, etc.).
- **Validación de Reglas de Negocio**: Asegura que las operaciones solicitadas sean coherentes con las reglas de negocio establecidas, como evitar saldos negativos en cuentas o limitar el número de tarjetas por cuenta.
- **Manejo de Errores**: Proporciona respuestas claras y detalladas en caso de que la validación falle, facilitando a los clientes de la API entender y corregir los errores.

Esta capa es crucial para mantener la integridad y consistencia de los datos en la aplicación.

---

## 3. Flujo de Autenticación y Autorización <a name="autenticación"></a>

El proceso de autenticación y autorización en la aplicación sigue los siguientes pasos:

1. **Inicio de Sesión**: Un usuario envía sus credenciales (RFC como nombre de usuario y contraseña) al endpoint de autenticación.
2. **Generación de Token**: Si las credenciales son válidas, el sistema genera un token JWT que incluye la información del usuario y sus roles.
3. **Uso del Token**: El token JWT es devuelto al cliente y debe ser incluido en el encabezado de autorización (`Authorization: Bearer <token>`) de todas las solicitudes subsecuentes.
4. **Validación del Token**: Cada solicitud entrante con un token JWT válido permite el acceso a los recursos protegidos según los roles asignados al usuario.
5. **Control de Acceso**: Spring Security verifica los roles del usuario y determina si tiene permiso para acceder al endpoint solicitado.

Este flujo garantiza que solo los usuarios autenticados y autorizados puedan acceder a las funcionalidades de la aplicación.

---

## 4. Manejo de Roles <a name="roles"></a>

La aplicación define varios roles para gestionar el acceso y las funcionalidades disponibles para cada tipo de usuario. Los roles definidos son:

- **ADMIN**: Tiene acceso completo a todas las funcionalidades de la aplicación, incluyendo la gestión de usuarios y configuración del sistema.
- **GERENTE**: Puede gestionar al personal, supervisar operaciones y acceder a reportes financieros.
- **EJECUTIVO**: Enfocado en la gestión de cuentas y servicios a clientes, con acceso limitado a ciertas operaciones administrativas.
- **CAJERO**: Responsable de realizar transacciones financieras básicas como depósitos y retiros.
- **CLIENTE**: Puede gestionar sus propias cuentas, realizar transacciones y ver su información financiera.

Cada rol está asociado con permisos específicos que determinan qué endpoints y operaciones puede ejecutar el usuario. Esta estructura de roles permite una gestión granular del acceso en la aplicación.

---

## 5. Interacción entre Componentes <a name="interacción"></a>

La arquitectura de la aplicación sigue el patrón de diseño de tres capas: Controladores, Servicios y Repositorios.

1. **Controladores**: Reciben las solicitudes HTTP y las validan antes de enviarlas a la capa de servicios.
2. **Servicios**: Implementan la lógica de negocio, interactúan con los repositorios y responden a los controladores con los resultados de las operaciones.
3. **Repositorios**: Se encargan de realizar las operaciones de acceso a la base de datos, devolviendo datos a la capa de servicios.

Esta separación asegura una clara división de responsabilidades y facilita el mantenimiento y la escalabilidad de la aplicación.

---

## 6. Consideraciones de Seguridad <a name="seguridad-consideraciones"></a>

- **Cifrado de Contraseñas**: Las contraseñas de los usuarios se almacenan en la base de datos utilizando un algoritmo de cifrado seguro (por ejemplo, BCrypt).
- **Protección de Endpoints**: Todos los endpoints sensibles están protegidos mediante autenticación y autorización basada en roles.
- **Token Expiration**: Los tokens JWT tienen un tiempo de expiración para minimizar el riesgo de uso indebido en caso de comprometerse.
- **Roles y Permisos**: La asignación de roles a los usuarios garantiza que cada persona solo tenga acceso a las funcionalidades necesarias para su trabajo, reduciendo el riesgo de abuso de privilegios.

---

## 7. Conclusión <a name="conclusión"></a>

Este proyecto de aplicación bancaria implementa una estructura clara y escalable, con un enfoque en la seguridad y la separación de responsabilidades. Utilizando Spring Boot y Spring Security, se garantiza que las operaciones sean seguras y que los usuarios solo puedan realizar las acciones para las que están autorizados, proporcionando una experiencia de uso confiable y eficiente.

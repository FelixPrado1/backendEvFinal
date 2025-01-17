# API REST para Gestión de Pedidos de Productos

## 📋 Descripción
Este proyecto es una API REST construida con Spring Boot, diseñada para gestionar pedidos de productos. La aplicación permite realizar operaciones fundamentales como registrar, listar, consultar, actualizar y eliminar pedidos. Además, implementa lógica de negocio robusta y prácticas recomendadas de seguridad.

## 🚀 Funcionalidades Principales

* **Registrar un nuevo pedido**: Permite a los clientes crear nuevos pedidos de productos.
* **Listar todos los pedidos**: Proporciona la capacidad de obtener una lista de todos los pedidos, con restricciones basadas en el rol del usuario (solo los clientes pueden ver sus propios pedidos y el admin todos los pedidos existentes).
* **Consultar un pedido por su ID**: Permite a los usuarios consultar detalles de un pedido específico mediante su ID (de la misma forma la restricción del cliente para que solo pueda consultar los propios).
* **Actualizar un pedido existente**: Facilita la modificación de los detalles de un pedido existente (de la misma forma la restricción del cliente para que solo pueda consultar los propios).
* **Eliminar un pedido**: Permite a los usuarios eliminar un pedido existente, con verificaciones de permisos (de la misma forma la restricción del cliente para que solo pueda consultar los propios).

## 🛠️ Endpoints

### 🔐 Autenticación
Estos endpoints permiten el registro de nuevos usuarios y la generación de tokens de acceso:

* `POST /auth/login` - Autentica a un usuario y devuelve un token de autenticación
* `POST /auth/register/cliente` - Registra un nuevo cliente
* `POST /auth/register/admin <JWT>🔐` - Registra un nuevo administrador (solo accesible para usuarios con rol ADMIN)

> **Nota**: Existe un endpoint adicional para generar un admin con acceso de cliente, utilizado únicamente para pruebas.

### 👥 Gestión de Usuarios

* `GET /auth/users <JWT>🔐` - Obtiene todos los usuarios (solo accesible para usuarios con rol ADMIN)

### 📦 Gestión de Pedidos

* `GET /api/pedido <JWT>🔐` - Lista todos los pedidos (clientes: solo propios, admin: todos)
* `GET /api/pedido/{id} <JWT>🔐` - Obtiene un pedido específico por ID
* `POST /api/pedido <JWT>🔐` - Crea un nuevo pedido
* `PATCH /api/pedido/{id} <JWT>🔐` - Actualiza un pedido existente
* `DELETE /api/pedido/{id} <JWT>🔐` - Elimina un pedido existente

### 🏷️ Gestión de Productos

* `GET /api/producto <JWT>🔐` - Lista todos los productos
* `GET /api/producto/{id} <JWT>🔐` - Obtiene un producto específico
* `POST /api/producto <JWT>🔐` - Crea un nuevo producto (solo ADMIN)
* `PATCH /api/producto/{id} <JWT>🔐` - Actualiza un producto (solo ADMIN)
* `DELETE /api/producto/{id} <JWT>🔐` - Elimina un producto (solo ADMIN)

## 🔒 Seguridad

La aplicación utiliza Spring Security para garantizar que solo los usuarios autorizados tengan acceso a recursos específicos. Se implementan roles de usuario (Cliente y Admin) para gestionar el acceso a diferentes operaciones, además de emplear DTOs para mostrar o solicitar en el request solo lo necesario.

## 🧪 Pruebas con Postman

Para facilitar las pruebas de la API, se incluye una colección de Postman lista para importar en:
```src/main/resources/static/Backend Final - Pasantia Entelgy.postman_collection.json```

## 📝 Gracias

Gracias a lo aprendido en la pasantía, mi proyecto no solo se limita a las funcionalidades básicas de gestión de pedidos, sino que también implementa una arquitectura sólida y prácticas de seguridad avanzadas para proporcionar una solución confiable y escalable.
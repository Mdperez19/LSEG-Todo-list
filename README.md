# Ejercicio práctico - API de Gestión de Tareas (To-Do List)

API REST para gestión de tareas desarrollada con Spring Boot y PostgreSQL.

## Características

- CRUD completo de tareas
- Paginación de resultados
- Documentación con Swagger/OpenAPI
- Persistencia en PostgreSQL
- Dockerización de la aplicación
- Tests unitarios
- Para la creación y migración del esquema de la base de datos se utiliza *Flyway*. Los scripts de migración SQL están en la carpeta `src/main/resources/db/migration`, lo que permite tener control sobre los cambios, mantener historial de versiones y garantizar que la base de datos siempre esté alineada con el código. Así, la base de datos se crea y actualiza automáticamente al iniciar la aplicación, sin necesidad de definir el esquema manualmente.
- Arquitectura: Para este proyecto utilicé una *arquitectura hexagonal* combinado con *DDD*, que básicamente ayuda a separar bien la lógica del negocio de todo lo que tiene que ver con frameworks, base de datos o detalles técnicos.
  De esta forma, el “corazón” del sistema queda independiente y es más fácil de mantener y probar. Además, traté de seguir los principios *SOLID* para que el código sea más claro, ordenado y fácil de modificar si después se requiere agregar nuevas funciones o cambiar algo de la tecnología.
- Se utilizó Lombok para reducir el boilerplate en las entidades, DTOs y para agregar el logging con la anotación @Slf4j en los casos de uso y controladores, esto facilita registrar eventos clave y errores sin escribir código repetitivo. Además lombok permite agregar el patrón de diseño `Builder`, pero al ser un patron de diseño que requiere mucho boilerplate, lombok ayuda bastante y mejora la legibilidad.

## Requisitos Previos

- Docker y Docker Compose
- Java 21 (para desarrollo local)
- Maven (para desarrollo local)

## Ejecutar la Aplicación

```bash
docker compose up db -d
```
Esperar unos segundos en lo que levanta la base de datos (esto es importante, ya que Flyway intentará conectarse de inmediato a la base de datos; si esta no está disponible, fallará la migración y la app no levantará :c).

```bash
./mvnw clean package
```

```bash
docker-compose up --build
```

La aplicación estará disponible en:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/api-docs/swagger-ui.html
![image](https://github.com/user-attachments/assets/189cf7b7-a792-47bf-bdde-9b313544b15f)

- Base de datos PostgreSQL: jdbc:postgresql://localhost:5433/tododb

## Endpoints

| Método | URL                  | Request Body              | Response Body          | Códigos HTTP               | Descripción                       |
|--------|----------------------|---------------------------|------------------------|----------------------------|-----------------------------------|
| POST   | `/tasks`             | CreationTaskCommand       | Task                   | 201 (Created), 400 (Bad Request) | Crear tarea                      |
| GET    | `/tasks`             | -                         | Page<Task>             | 200 (OK)                   | Listar tareas (paginado)          |
| GET    | `/tasks/{id}`        | -                         | Task                   | 200 (OK), 404 (Not Found)  | Obtener tarea por ID              |
| PUT    | `/tasks/{id}/status` | UpdateTaskStatusCommand   | Task                   | 200 (OK), 404 (Not Found)  | Actualizar estado de tarea        |
| DELETE | `/tasks/{id}`        | -                         | -                      | 204 (No Content), 404 (Not Found) | Eliminar tarea               |

### Listar Tareas (Paginado)

Parámetros de paginación:
- `page`: Número de página (comienza desde 0)
- `size`: Cantidad de elementos por página
- `sort`: Campo por el cual ordenar, seguido de la dirección (`asc` o `desc`)

Ejemplos:
- Primera página, 5 elementos: `/tasks?page=0&size=5`
- Segunda página, 10 elementos: `/tasks?page=1&size=10`
- Ordenar por título ascendente: `/tasks?page=0&size=10&sort=title,asc`
- Ordenar por fecha de vencimiento descendente: `/tasks?page=0&size=10&sort=dueDate,desc`

### Pruebas en POSTMAN (Se incluye la colección en la carpeta `collection-postman` de este repo )

- Crear una tarea (POST /tasks)
  ![image](https://github.com/user-attachments/assets/11c41a03-4a8a-46d3-809c-637e0e93e2e3)

- Consultar una lista de tareas (GET /tasks)
  ![image](https://github.com/user-attachments/assets/248daab8-9e26-4927-96b0-920fb984f114)

- Consultar detalle de una tarea (GET /tasks/{id})
  ![image](https://github.com/user-attachments/assets/d88657eb-71e6-4828-9440-64529433715b)

- Actualizar el estado de una tarea (PUT /tasks/{id})
  ![image](https://github.com/user-attachments/assets/15eb2c0c-ee1f-43b6-b36e-709948c13d88)

- Eliminar una tarea (DELETE /tasks/{id})
  ![image](https://github.com/user-attachments/assets/88a9be23-6a94-47f4-950b-f6343072a230)


## Retos de aprendizaje rápido

- ¿Qué pasos tomaste para aprender Swagger si no lo habías usado antes?
  
Cuando trabajé en Alpura no había usado Swagger antes, así que con ayuda de mis compañeros y la documentación oficial, pude integrarlo en el proyecto. Leí sobre las configuraciones básicas y cómo generar la documentación automáticamente. Además de como usar la interfaz de usuario para probar los endpoints.
- ¿Cómo resolviste dudas sobre Docker?

Ya había trabajado con Docker en proyectos anteriores, así que utilicé mis conocimientos previos y la documentación oficial de Docker para resolver dudas. También consulté foros y comunidades en línea cuando me encontré con problemas específicos. 
Además tomé un curso en línea que me ayudó a entender mejor cómo funciona Docker 
- ¿Qué desafíos encontraste?

Tuve algunos desafíos al configurar la conexión entre la aplicación y la base de datos PostgreSQL en Docker, especialmente con la configuración de red y volúmenes, ya que no me había dado cuenta que tenía ya el puerto común de PostgreSQL en mi computadora, entonces me tardé un poco en encontrar que esa era la razón.
También se me complicó la parte de Flyway, porque me di cuenta que tenía que primero generar la base de datos PostgreSQL, ya que Flyway intenta conectarse y aplicar migraciones inmediatamente cuando la aplicación arranca; si la base no está disponible, Flyway falla y la aplicación no puede iniciar.
- ¿Qué mejorarías si tuvieras más tiempo?

Si tuviera más tiempo, mejoraría la documentación del proyecto para incluir más ejemplos de uso de los endpoints y una guía más detallada sobre cómo contribuir al proyecto. También implementaría pruebas de integración para asegurar que todos los componentes funcionen correctamente juntos. También me gustaría agregar funcionalidades de Jenkins con SonarQube (aunque sí usé Sonar de manera local) para mejorar la calidad del código y la integración continua.
Además, consideraría implementar autenticación y autorización para proteger los endpoints de la API.

Por temas de carga laboral no me fue posible agregar la parte de seguridad :c, pero tenía planeado crear una nueva entidad user dentro del árbol de carpetas siguiendo el mismo enfoque de arquitectura hexagonal y DDD.
De ahí partiría agregando los casos de uso de crear usuario y buscar usuario por nombre, además de otra carpeta de seguridad donde pondría toda la parte de configuración de Spring Security.
Dependiendo del tiempo, podría implementar desde una autenticación básica (usuario y contraseña en el encabezado Basic Auth), hasta OAuth2 usando Keycloak (he visto su uso en Alpura aunque lamentablemente no pude participar directamente, pero es algo que me gustaría estudiar a fondo para futuras versiones del proyecto).

Aunque este ejercicio ya tiene su limite de tiempo, seguiré usando este proyecto para seguir aprendiendo nuevas tecnologias y metodologias c:

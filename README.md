# Ejercicio práctico - API de Gestión de Tareas (To-Do List)

API REST para gestión de tareas desarrollada con Spring Boot y PostgreSQL.

## Características

- CRUD completo de tareas
- Paginación de resultados
- Documentación con Swagger/OpenAPI
- Persistencia en PostgreSQL
- Dockerización de la aplicación
- Tests unitarios
- Para la creación de la base de datos se utilizó la configuración de `  jpa.hibernate.ddl-auto: update` para poder crear la base de datos automáticamente al iniciar la aplicación. Esto permite que la base de datos se cree y se actualice según las entidades definidas en el código, facilitando el desarrollo y las pruebas sin necesidad de crear manualmente la base de datos.
- Se utilizó lombok para reducir el boilerplate en las entidades y DTOs, lo que mejora la legibilidad del código y reduce la cantidad de código repetitivo. Usando lombok tambien se usó el patrón de diseño *Builder* para la creación de objetos, lo que facilita la construcción de instancias complejas de manera más legible y mantenible.
- Arquitectura: Para este proyecto utilicé una *arquitectura hexagonal* combinado con *DDD*, que básicamente ayuda a separar bien la lógica del negocio de todo lo que tiene que ver con frameworks, base de datos o detalles técnicos.
  De esta forma, el “corazón” del sistema queda independiente y es más fácil de mantener y probar. Además, traté de seguir los principios *SOLID* para que el código sea más claro, ordenado y fácil de modificar si después se requiere agregar nuevas funciones o cambiar algo de la tecnología.

## Requisitos Previos

- Docker y Docker Compose
- Java 21 (para desarrollo local)
- Maven (para desarrollo local)

## Ejecutar la Aplicación

```bash
./mvnw clean package
```

```bash
docker-compose up --build
```

La aplicación estará disponible en:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/api-docs/swagger-ui.html
- Base de datos PostgreSQL: jdbc:postgresql://localhost:5433/tododb

## Endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/tasks` | Crear tarea |
| GET | `/tasks` | Listar tareas (paginado) |
| GET | `/tasks/{id}` | Obtener tarea por ID |
| PUT | `/tasks/{id}/status` | Actualizar estado de tarea |
| DELETE | `/tasks/{id}` | Eliminar tarea |

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

## Retos de aprendizaje rápido

Agrega brevemente en el README.md del proyecto:
- ¿Qué pasos tomaste para aprender Swagger si no lo habías usado antes?
Cuando trabajé en Alpura no había usado Swagger antes, así que con ayuda de mis compañeros y la documentación oficial, pude integrarlo en el proyecto. Leí sobre las configuraciones básicas y cómo generar la documentación automáticamente. Además de como usar la interfaz de usuario para probar los endpoints.
- ¿Cómo resolviste dudas sobre Docker?
Ya había trabajado con Docker en proyectos anteriores, así que utilicé mis conocimientos previos y la documentación oficial de Docker para resolver dudas. También consulté foros y comunidades en línea cuando me encontré con problemas específicos. 

Además tomé un curso en línea que me ayudó a entender mejor cómo funciona Docker 

- ¿Qué desafíos encontraste?

Tuve algunos desafíos al configurar la conexión entre la aplicación y la base de datos PostgreSQL en Docker, especialmente con la configuración de red y volúmenes, ya que no me había dado cuenta que tenía ya el puerto comun de postgresql en mi computadora, entonces me tardé un poco en encontrar que esa era la razón. También fue un reto inicial recordar cómo funcionaba Swagger y cómo integrarlo correctamente en el proyecto.
- ¿Qué mejorarías si tuvieras más tiempo?
Si tuviera más tiempo, mejoraría la documentación del proyecto para incluir más ejemplos de uso de los endpoints y una guía más detallada sobre cómo contribuir al proyecto. También implementaría pruebas de integración para asegurar que todos los componentes funcionen correctamente juntos. También me gustaría agregar funcionalidadades de Jenkins con SonarQube (aunque si usé Sonar de manera local) para mejorar la calidad del código y la integración continua. Además, consideraría implementar autenticación y autorización para proteger los endpoints de la API.
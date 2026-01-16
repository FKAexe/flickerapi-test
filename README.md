# flickerapi-test 

Prueba técnica para Innporting
- Web app para búsqueda de imágenes usando la API de Flickr. 

#Backend: Java / Spring
#Frontend: Typescript/ Angular

## Características Principales:

- REST API hecha en Spring con Java como lenguaje de programación.
- Frontend hecho en Angular.
- Diseño responsive usando Tailwind CSS y Angular Material.
- Scroll infinito a través de los resultados de búsqueda.
- Vista de detalles con información completa de la imagen.

## Capturas de pantalla

Búsqueda: 
<img width="1337" height="556" alt="image" src="https://github.com/user-attachments/assets/e24357fa-0b9f-47d5-88ac-ca1c3ee6a2ee" />
Detalles:
<img width="995" height="590" alt="image" src="https://github.com/user-attachments/assets/afd69cd1-603d-44d0-b4c4-923857327455" />


## Endpoints

### GET /api/images/search
Buscar imágenes por texto.

**Parámetros:**
- query (required)
- page (optional, default=1)
- size (optional, default=20)

### GET /api/images/{id}
Obtener detalle de una imagen.

#### GET /api/images/{id}/download
Descargar una imagen


Requisitos:
Para lanzar la API necesitas una llave de FlickrAPI para añadir al backend.

## Instrucciones para uso:

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/flickr-app.git
cd flickr-app
```

### 2. Configurar Backend
```bash
cd flickr-backend
```

Crear archivo `src/main/resources/application.properties`:
```properties
# Flickr API Configuration
flickr.api.key=TU_API_KEY_AQUI
flickr.api.secret=TU_SECRET_AQUI
flickr.api.base-url=https://api.flickr.com/services/rest/

# Server Configuration
server.port=8080
```

Instalar dependencias y compilar:
```bash
mvn clean install
```

### 3. Configurar Frontend
```bash
cd ../flickr-app
npm install
```

## Ejecutar la Aplicación

### Backend
```bash
cd flickr-backend
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

### Frontend
```bash
cd flickr-app
ng serve
```

El frontend estará disponible en: `http://localhost:4200`





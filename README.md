# TripPlanner - Aplicación Móvil Android

## Descripción

TripPlanner es una aplicación móvil desarrollada en Android Studio que permite a los usuarios gestionar sus viajes de manera eficiente. La aplicación implementa una arquitectura moderna siguiendo las mejores prácticas de desarrollo Android.

## Características Principales

### Autenticación de Usuarios
- Registro de nuevos usuarios
- Inicio de sesión con email y contraseña
- Gestión de sesiones de usuario

### Gestión de Viajes
- Crear nuevos viajes con información detallada
- Visualizar lista de viajes del usuario
- Editar viajes existentes
- Eliminar viajes
- Ver detalles completos de cada viaje

### Información de Viajes
Cada viaje incluye los siguientes campos:
- **Destino**: Nombre del lugar de destino
- **País**: País del destino
- **Ciudad**: Ciudad específica
- **Fecha del viaje**: Fecha programada
- **Medio de transporte**: Tipo de transporte utilizado
- **Comentario personal**: Notas opcionales sobre la experiencia

## Arquitectura del Proyecto

### Patrón MVVM (Model-View-ViewModel)
La aplicación implementa el patrón MVVM para separar claramente las responsabilidades:

- **Model**: Capa de datos con entidades Room y modelos de API
- **View**: Interfaces de usuario construidas con Jetpack Compose
- **ViewModel**: Lógica de presentación y gestión de estado

### Componentes Principales

#### Capa de Datos
- **Room Database**: Base de datos local para almacenamiento offline
- **Retrofit**: Cliente HTTP para comunicación con API REST
- **Repository Pattern**: Abstracción de acceso a datos

#### Capa de Presentación
- **Jetpack Compose**: UI declarativa moderna
- **Navigation Component**: Navegación entre pantallas
- **ViewModel**: Gestión de estado y lógica de negocio
- **StateFlow**: Flujo de datos reactivo

#### Inyección de Dependencias
- **Hilt**: Inyección de dependencias para Android

## Tecnologías Utilizadas

### Core Android
- Kotlin
- Android SDK
- Jetpack Compose
- Material Design 3

### Arquitectura y Patrones
- MVVM (Model-View-ViewModel)
- Repository Pattern
- Dependency Injection (Hilt)

### Base de Datos y Networking
- Room Database
- Retrofit
- OkHttp
- Coroutines

### Navegación
- Navigation Compose
- Safe Args

## Estructura del Proyecto

```
app/src/main/java/com/example/tripplaner/
├── data/
│   ├── api/
│   │   ├── ApiService.kt
│   │   └── RetrofitClient.kt
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── TripDao.kt
│   │   └── UserDao.kt
│   ├── model/
│   │   ├── Trip.kt
│   │   ├── User.kt
│   │   └── ApiResponse.kt
│   └── repository/
│       ├── TripRepository.kt
│       └── UserRepository.kt
├── di/
│   └── DatabaseModule.kt
├── ui/
│   ├── navigation/
│   │   ├── NavGraph.kt
│   │   └── Screen.kt
│   ├── screens/
│   │   ├── auth/
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   └── trips/
│   │       ├── TripsListScreen.kt
│   │       ├── TripDetailScreen.kt
│   │       └── AddEditTripScreen.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodel/
│       ├── AuthViewModel.kt
│       └── TripViewModel.kt
├── MainActivity.kt
└── TripPlannerApplication.kt
```

## Configuración del Proyecto

### Requisitos Previos
- Android Studio Hedgehog o superior
- JDK 11 o superior
- Android SDK API 24+
- Dispositivo Android o emulador

### Configuración de la API
La aplicación está configurada para conectarse a una API REST en Spring Boot. Para desarrollo local:

1. Asegúrate de que el servidor Spring Boot esté ejecutándose en `http://localhost:8080`
2. Si usas un emulador, la URL se configura automáticamente como `http://10.0.2.2:8080`
3. Para dispositivos físicos, actualiza la URL base en `RetrofitClient.kt`

### Instalación y Ejecución

1. Clona el repositorio:
```bash
git clone <repository-url>
cd TripPlanner
```

2. Abre el proyecto en Android Studio

3. Sincroniza las dependencias de Gradle

4. Ejecuta la aplicación en un dispositivo o emulador

## Funcionalidades Detalladas

### Pantalla de Login
- Campos para email y contraseña
- Validación de campos
- Manejo de errores de autenticación
- Navegación a registro

### Pantalla de Registro
- Formulario completo de registro
- Validación de contraseñas
- Confirmación de contraseña
- Navegación a login

### Lista de Viajes
- Vista de lista con tarjetas de viajes
- Botón flotante para agregar viajes
- Acciones de editar y eliminar por viaje
- Estado vacío con mensaje informativo
- Pull-to-refresh para sincronización

### Detalles del Viaje
- Vista detallada de toda la información
- Botón de edición
- Formato de fecha legible
- Iconos descriptivos

### Agregar/Editar Viaje
- Formulario completo con validación
- Selector de fecha integrado
- Campo de comentario opcional
- Modo de edición vs creación

## Manejo de Estados

La aplicación implementa un manejo robusto de estados:

- **Loading**: Indicadores de carga durante operaciones
- **Success**: Confirmación de operaciones exitosas
- **Error**: Manejo y visualización de errores
- **Empty**: Estados vacíos informativos

## Persistencia de Datos

### Base de Datos Local (Room)
- Almacenamiento offline de viajes
- Sincronización automática con servidor
- Consultas optimizadas con Flow

### Sincronización
- Datos se sincronizan automáticamente al iniciar sesión
- Operaciones CRUD se reflejan tanto local como remotamente
- Manejo de conflictos de red

## Consideraciones de Seguridad

- Contraseñas se transmiten de forma segura
- Tokens de autenticación se manejan apropiadamente
- Validación de entrada en todos los formularios
- Manejo seguro de errores sin exponer información sensible

## Pruebas

La aplicación incluye:
- Pruebas unitarias básicas
- Pruebas de instrumentación
- Pruebas de UI con Compose Testing

## Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature
3. Implementa los cambios
4. Ejecuta las pruebas
5. Crea un Pull Request

## Licencia

Este proyecto está desarrollado como parte de un proyecto académico de la asignatura "Programación Orientada a Objetos II".

## Contacto

Para preguntas o soporte, contacta al equipo de desarrollo.

---

**Nota**: Esta aplicación está diseñada para funcionar con un backend Spring Boot. Asegúrate de tener el servidor correspondiente ejecutándose para todas las funcionalidades. 
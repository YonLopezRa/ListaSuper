# Lista Súper Inteligente

App Android para armar la lista del súper. Puedes escribir los productos o escanear su código de barras, y la app llena sola el nombre, la marca y la imagen.

La hice para practicar Kotlin y la forma en que hoy se construyen las apps Android: MVVM, Room, Coroutines, Flow y Retrofit.

## Capturas

| Mis listas | Detalle de lista | Agregar producto | Producto escaneado |
|:---:|:---:|:---:|:---:|
| ![Listas](docs/screenshots/1_lista_listas.jpg) | ![Detalle](docs/screenshots/2_detalle_lista.jpg) | ![Agregar](docs/screenshots/3_antes_escanear.jpg) | ![Escaneado](docs/screenshots/4_despues_escanear.jpg) |

## Resultado

Con un solo escaneo, la app intenta llenar 3 datos del producto (nombre, marca e imagen) consultando hasta 3 fuentes. Si no lo encuentra, lo capturas a mano.

## Cómo busca un producto

Cuando escaneas un código, la app lo busca en este orden:

1. En la base de datos local (Room), por si ya lo habías agregado antes.
2. En Open Food Facts.
3. En UPCitemdb, si Open Food Facts no lo tiene.

Si no aparece en ninguno, te deja llenarlo a mano. La lógica está en `data/repository/ProductoRepository.kt`.

## Qué puedes hacer

- Crear y eliminar listas.
- Agregar productos a mano (nombre, marca y cantidad) o escaneando su código de barras.
- Marcar productos como comprados.
- Usar tus listas sin internet, porque se guardan en el teléfono. Solo la búsqueda por código necesita conexión.

## Tecnologías

- Kotlin
- MVVM con una sola Activity y Fragments
- Room con Coroutines y Flow
- Retrofit, OkHttp y Gson
- ZXing Android Embedded para el escáner
- Coil para las imágenes y Lottie para la animación de carga
- Navigation Component con Safe Args
- Material Design 3, ViewBinding y ConstraintLayout

## Estructura

```
data/
  local/       Room: base de datos, DAOs y entidades
  remote/      Retrofit: APIs de Open Food Facts y UPCitemdb
  repository/  Repositorios que deciden de dónde sacar los datos
ui/
  listas/      Pantalla de mis listas
  detalle/     Productos de una lista
  agregar/     Agregar producto y escanear
```

## Cómo correrlo

1. Clona el repositorio: `git clone https://github.com/YonLopezRa/ListaSuper.git`
2. Ábrelo en Android Studio (usa JDK 17).
3. Córrelo en un emulador o en un teléfono con Android 7.0 o superior. Para escanear necesitas un teléfono con cámara.

No necesita llaves de API.

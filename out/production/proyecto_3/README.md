# Analizador Sintáctico de Juegos de Ajedrez

Este proyecto implementa un analizador sintáctico de juegos de ajedrez escritos en notación algebraica estándar (SAN) y visualiza el juego como un árbol binario entrelazado por turnos.

## Descripción

El programa permite analizar una partida de ajedrez en notación SAN. Si la partida cumple con las reglas definidas en la gramática BNF proporcionada, se representa visualmente como un árbol binario donde:

- La raíz es "Partida"
- Cada turno añade dos hijos: jugada blanca (izquierda) y jugada negra (derecha)
- Los turnos se conectan entre sí formando un árbol binario entrelazado

## Características

- **Validación Sintáctica**: Implementa un analizador que verifica que cada movimiento y cada turno completo cumple con las reglas de la notación SAN.
- **Visualización de Árbol**: Construye y muestra un árbol binario representando las jugadas.
- **Interfaz Gráfica**: Proporciona una interfaz amigable para ingresar y analizar partidas.
- **Ejemplos Predefinidos**: Incluye ejemplos de partidas para pruebas rápidas.

## Requisitos

- Java JDK 8 o superior
- Swing (incluido en el JDK)

## Uso

1. Compile el proyecto usando su IDE favorito o mediante línea de comandos.
2. Ejecute la clase `Main` para iniciar la aplicación.
3. Ingrese una partida en notación SAN o use el botón "Cargar Ejemplo".
4. Haga clic en "Analizar Partida" para validar la notación.
5. Si la partida es válida, haga clic en "Visualizar Árbol" para ver la representación gráfica.

## Estructura del Proyecto

- `Main.java`: Clase principal para iniciar la aplicación.
- `VisualizadorArbol.java`: Implementa la interfaz gráfica y la visualización del árbol.
- `ChessGameParser.java`: Contiene la lógica para analizar la notación SAN.
- `ArbolBinario.java`: Implementa la estructura de árbol binario.
- `Nodo.java`: Representa los nodos del árbol.
- `Turno.java`: Modela los turnos en la partida de ajedrez.

## Autores

- [Tu Nombre]
- [Nombre de tu compañero]

## Versión

1.0.0

## Entorno de Desarrollo

- IDE: [Tu IDE, ej. IntelliJ IDEA, Eclipse, NetBeans]
- Lenguaje: Java
- Versión del compilador: [Versión de tu JDK, ej. JDK 11]

## Licencia

Este proyecto es parte de una práctica académica para la Universidad EAFIT - Lenguajes de Programación.

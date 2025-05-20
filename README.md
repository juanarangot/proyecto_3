# Chess Game Parser

This project implements a syntactic parser for chess games written in Standard Algebraic Notation (SAN) and visualizes the game as an interleaved binary tree by turns.

## Description

The program allows parsing a chess game in SAN notation. If the game complies with the rules defined in the provided BNF grammar, it is visually represented as a binary tree where:

- The root is "Game"
- Each turn adds two children: white's move (left) and black's move (right)
- Turns are interconnected, forming an interleaved binary tree

## Features

- **Syntactic Validation**: Implements a parser that verifies each move and full turn complies with SAN notation rules.
- **Tree Visualization**: Builds and displays a binary tree representing the moves.
- **Graphical Interface**: Provides a user-friendly interface for inputting and analyzing games.
- **Predefined Examples**: Includes example games for quick testing.

## Requirements

- Java JDK 8 or higher
- Swing (included in the JDK)

## Usage

1. Compile the project using your favorite IDE or via the command line.
2. Run the `Main` class to start the application.
3. Enter a game in SAN notation or use the "Load Example" button.
4. Click "Analyze Game" to validate the notation.
5. If the game is valid, click "Visualize Tree" to view the graphical representation.

## Project Structure

- `Main.java`: Main class to start the application.
- `VisualizadorArbol.java`: Implements the graphical interface and tree visualization.
- `ChessGameParser.java`: Contains the logic for parsing SAN notation.
- `ArbolBinario.java`: Implements the binary tree structure.
- `Nodo.java`: Represents the tree nodes.
- `Turno.java`: Models the turns in the chess game.

## Authors

- **Juan Manuel Arango Tobon** | lunes 6 a.m 
- **Santiago Arellano Lopez** | Martes 6 a.m

## Version

1.0.0 (2025-05-15)

## Development Environment

- IDE: [IntelliJ IDEA]
- Language: Java 
- Compiler Version: [JDK 21 correto]

## License

This project is part of an academic exercise for EAFIT University - Programming Languages.

2025

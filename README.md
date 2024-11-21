# My Super Mario Java 2D Game

This is a full screen 2D Java game like super mario with some basic creatures and maps.

This game is implemented using the Java programming language, using the java graphics librieries.

The used IDE is netbeans (I uploaded all project artifacts), also the game JAR file is uploaded.

This game is free to use and the code is available to anyone to learn from it without any restrictions.

Please visit my website. http://www.mohamedtalaat.net/


# CREDENCIALES CONEXIÓN BASE DE DATOS
Según las que se establezcan a la hora de crear las bases de datos habrá que añadirlas al archivo "config.xml" en la carpeta "Configuración" del proyecto
- En la etiqueta host indicariamos la IP
- En la etiqueta port el puerto de conexión (5432 postgresql y 3306 mysql)
- En la etiqueta user el usuario de la conexión
- En la etiqueta pass la contraseña de la conexión


# CONSULTA POSTGESQL
```sql
-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS gamesql;

-- Crear la base de datos
CREATE DATABASE gamesql;

-- Seleccionar la base de datos
\c gamesql

-- Crear la tabla Players
CREATE TABLE Players (
    player_id SERIAL PRIMARY KEY,
    nick_name VARCHAR(100) NOT NULL,
    experience INT NOT NULL DEFAULT 0,
    life_level INT NOT NULL DEFAULT 100,
    coins INT NOT NULL DEFAULT 0,
    session_count INT NOT NULL DEFAULT 0,
    last_login TIMESTAMP
);

-- Crear la tabla Videogames
CREATE TABLE Videogames (
    game_id SERIAL PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE,
    title VARCHAR(100) NOT NULL,
    player_count INT NOT NULL DEFAULT 0,
    total_sessions INT NOT NULL DEFAULT 0,
    last_session TIMESTAMP
);

-- Crear la tabla Games
CREATE TABLE Games (
    session_id SERIAL PRIMARY KEY,
    game_id INT NOT NULL,
    player_id INT NOT NULL,
    experience INT NOT NULL DEFAULT 0,
    life_level INT NOT NULL DEFAULT 0,
    coins INT NOT NULL DEFAULT 0,
    session_date TIMESTAMP NOT NULL,
    FOREIGN KEY (game_id) REFERENCES Videogames(game_id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES Players(player_id) ON DELETE CASCADE
);

```

# CONSULTA MySQL
```sql
DROP DATABASE gamesql;

CREATE DATABASE gamesql;
USE gamesql;

CREATE TABLE Players (
	player_id INT PRIMARY KEY AUTO_INCREMENT,
	nick_name VARCHAR(100) NOT NULL,
	experience INT NOT NULL DEFAULT 0,
	life_level INT NOT NULL DEFAULT 100,
	coins INT NOT NULL DEFAULT 0,
	session_count INT NOT NULL DEFAULT 0,
	last_login DATETIME
);

CREATE TABLE Videogames (
	game_id INT PRIMARY KEY AUTO_INCREMENT,
	isbn VARCHAR(20) UNIQUE,
	title VARCHAR(100) NOT NULL,
	player_count INT NOT NULL DEFAULT 0,
	total_sessions INT NOT NULL DEFAULT 0,
	last_session DATETIME
);

CREATE TABLE Games (
	session_id INT PRIMARY KEY AUTO_INCREMENT,
	game_id INT NOT NULL,
	player_id INT NOT NULL,
	experience INT NOT NULL DEFAULT 0,
	life_level INT NOT NULL DEFAULT 0,
	coins INT NOT NULL DEFAULT 0,
	session_date DATETIME NOT NULL,
	FOREIGN KEY (game_id) REFERENCES Videogames(game_id) ON DELETE CASCADE,
	FOREIGN KEY (player_id) REFERENCES Players(player_id) ON DELETE CASCADE
);
```

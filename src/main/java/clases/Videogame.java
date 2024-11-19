package clases;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Videogame {

    private int game_id, player_count, total_sessions;
    private String isbn, title;
    private LocalDateTime last_session;

    public Videogame(String isbn, String title, int player_count, int total_sessions, LocalDateTime last_session) {
        this.isbn = isbn;
        this.title = title;
        this.player_count = player_count;
        this.total_sessions = total_sessions;
        this.last_session = last_session;
    }

    public Videogame(int gameId, String isbn, String title, int player_count, int total_sessions, LocalDateTime last_session) {
        this.game_id = gameId;
        this.isbn = isbn;
        this.title = title;
        this.player_count = player_count;
        this.total_sessions = total_sessions;
        this.last_session = last_session;
    }

    /**
     * @return the id
     */
    public int getId() {
        return game_id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.game_id = id;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the player_count
     */
    public int getPlayer_count() {
        return player_count;
    }

    /**
     * @param player_count the player_count to set
     */
    public void setPlayer_count(int player_count) {
        this.player_count = player_count;
    }

    /**
     * @return the total_sessions
     */
    public int getTotal_sessions() {
        return total_sessions;
    }

    /**
     * @param total_sessions the total_sessions to set
     */
    public void setTotal_sessions(int total_sessions) {
        this.total_sessions = total_sessions;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the last_session
     */
    public LocalDateTime getLast_session() {
        return last_session;
    }

    /**
     * @param last_session the last_session to set
     */
    public void setLast_session(LocalDateTime last_session) {
        this.last_session = last_session;
    }

    public String[] getVideogameDataArray() {
        ArrayList<String> data = new ArrayList<>();

        // Añadir los atributos del videojuego al ArrayList
        data.add(String.valueOf(game_id));
        data.add(isbn);  // ISBN como cadena
        data.add(title);  // Título del videojuego
        data.add(String.valueOf(player_count));  // Cantidad de jugadores como cadena
        data.add(String.valueOf(total_sessions));  // Total de sesiones como cadena
        data.add(last_session != null ? last_session.toString() : "null");  // Manejo de 'null' para 'last_session'

        // Convertir el ArrayList a un arreglo String[]
        return data.toArray(new String[0]);
    }

}

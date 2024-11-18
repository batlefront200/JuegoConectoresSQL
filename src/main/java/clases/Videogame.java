
package clases;

import java.time.LocalDateTime;

public class Videogame {
    private int game_id, player_count, total_sessions;
    private String isbn, title;
    private LocalDateTime last_session;
    
    public Videogame(int game_id, String isbn, String title, int player_count, int total_sessions, LocalDateTime last_session) {
        this.game_id = game_id;
        this.isbn = isbn;
        this.title = title;
        this.player_count = player_count;
        this.total_sessions = total_sessions;
        this.last_session = null;
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
    
}

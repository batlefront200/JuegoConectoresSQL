package clases;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Player {

    private int player_id, experience, life_level, coins, session_count;
    private String nick_name;
    private LocalDateTime last_login;

    public Player() {

    }

    public Player(String nick_name, int experience, int life_level, int coins, int session_count, LocalDateTime last_login) {
        this.nick_name = nick_name;
        this.experience = experience;
        this.life_level = life_level;
        this.coins = coins;
        this.session_count = session_count;
        this.last_login = last_login; // Permite que sea null
    }

    public Player(int player_id, String nick_name, int experience, int life_level, int coins, int session_count) {
        this.player_id = player_id;
        this.nick_name = nick_name;
        this.experience = experience;
        this.life_level = life_level;
        this.coins = coins;
        this.session_count = 0;
        this.last_login = LocalDateTime.now();
    }

    public Player(int player_id, String nick_name, int experience, int life_level, int coins, int session_count, LocalDateTime last_login) {
        this.player_id = player_id;
        this.nick_name = nick_name;
        this.experience = experience;
        this.life_level = life_level;
        this.coins = coins;
        this.session_count = 0;
        this.last_login = last_login;
    }

    /**
     * @return the nick_name
     */
    public String getNick_name() {
        return nick_name;
    }

    /**
     * @param nick_name the nick_name to set
     */
    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    /**
     * @return the player_id
     */
    public int getId() {
        return player_id;
    }

    /**
     * @param id the player_id to set
     */
    public void setId(int id) {
        this.player_id = id;
    }

    /**
     * @return the experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * @param experience the experience to set
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * @return the life_level
     */
    public int getLife_level() {
        return life_level;
    }

    /**
     * @param life_level the life_level to set
     */
    public void setLife_level(int life_level) {
        this.life_level = life_level;
    }

    /**
     * @return the coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * @param coins the coins to set
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * @return the session_count
     */
    public int getSession_count() {
        return session_count;
    }

    /**
     * @param session_count the session_count to set
     */
    public void setSession_count(int session_count) {
        this.session_count = session_count;
    }

    /**
     * @return the last_login
     */
    public LocalDateTime getLast_login() {
        return last_login;
    }

    /**
     * @param last_login the last_login to set
     */
    public void setLast_login(LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public String[] getPlayerDataArray() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(player_id));
        data.add(nick_name);
        data.add(String.valueOf(experience));
        data.add(String.valueOf(life_level));
        data.add(String.valueOf(coins));
        data.add(String.valueOf(session_count));
        data.add(last_login != null ? last_login.toString() : "null"); // Evitar null si last_login es null

        // Convertir el ArrayList a un arreglo String[]
        return data.toArray(new String[0]);
    }

}

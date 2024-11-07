
package clases;

import java.sql.Date;

/* game_id: Identificador del juego.
player_id: Identificador del jugador.
experience: Incremento del nivel de experiencia.
life_level: Actualización del nivel de vida (+/-).
coins: Actualización de las monedas acumuladas (+/-).
session_date: Fecha de la sesión o juego (partida). */

public class Partidas {
    private int game_id, player_id, experience, life_level, coins;
    private Date session_date;
    
    public Partidas(int player_id, int experience, int life_level, int coins, Date session_date) {
        game_id = 0; // Modificar game id a auto increment
        this.player_id = player_id;
        this.experience = experience;
        this.life_level = life_level;
        this.coins = coins;
        this.session_date = session_date;
    }
    
    /**
     * @return the game_id
     */
    public int getGame_id() {
        return game_id;
    }

    /**
     * @param game_id the game_id to set
     */
    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    /**
     * @return the player_id
     */
    public int getPlayer_id() {
        return player_id;
    }

    /**
     * @param player_id the player_id to set
     */
    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
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
     * @return the session_date
     */
    public Date getSession_date() {
        return session_date;
    }
    
    /**
     * @param session_date the session_date to set
     */
    public void setSession_date(Date session_date) {
        this.session_date = session_date;
    }
}

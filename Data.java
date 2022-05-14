import java.util.ArrayList;

/**
 * This class holds data that has been processed by
 * the DataProcessor. It allows for primarily the 
 * player to easily reference data necessary for 
 * updating their game state
 *
 * @see DataProcessor
 */

public class Data {

    // player
    public Ship player;
    // player's projectiles
    public ArrayList<Projectile> projectiles;
    // list of asteroids
    public ArrayList<Asteroid> asteroids;

    /**
     * Creates a new Data object containing the sending player's
     * Ship, associated Projectiles, and current Asteroids
     * iteration. The asteroids ArrayList is only used when a
     * new Player joins the game in order to update its own state.
     * 
     * @param player the object of the player sending data
     * @param projectiles the object of the projectiles from the
     *                    player sending data
     * @param asteroids the object of the current asteroids 
     *                  present in the universe
     */
    public Data(Ship player, 
                ArrayList<Projectile> projectiles,
                ArrayList<Asteroid> asteroids) {
        this.player = player;
        this.projectiles = projectiles;
        this.asteroids = asteroids;
    }

    public String toString() {
        String data = "";
        data += player.toString();
        for (Projectile p : projectiles) data += p.toString() + "\n";
        for (Asteroid a : asteroids) data += a.toString() + "\n";
        return data.trim();
    }
}
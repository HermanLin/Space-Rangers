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
    public Ship spaceship;
    // player's projectiles
    public ArrayList<Projectile> projectiles;
    // list of asteroids
    public ArrayList<Asteroid> asteroids;

    /**
     * Creates a new Data object only containing the asteroids
     * of the current game state
     *
     * @param asteroids the objects of the asteroids from the
     *                  player sending data
     */
    public Data(ArrayList<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }

    /**
     * Creates a new Data object only containing the sending
     * player's Ship and associated Projectiles.
     *
     * @param spaceship the object of the player sending data
     * @param projectiles the objects of the projectiles from the
     *                    player sending data
     */
    public Data(Ship spaceship, ArrayList<Projectile> projectiles) {
        this.spaceship = spaceship;
        this.projectiles = projectiles;
    }
    
    /**
     * Creates a new Data object containing the sending player's
     * Ship, associated Projectiles, and current Asteroids
     * iteration. 
     * 
     * @param player the object of the player sending data
     * @param projectiles the objects of the projectiles from the
     *                    player sending data
     * @param asteroids the objects of the current asteroids 
     *                  present in the universe
     */
    public Data(Ship spaceship, 
                ArrayList<Projectile> projectiles,
                ArrayList<Asteroid> asteroids) {
        this.spaceship = spaceship;
        this.projectiles = projectiles;
        this.asteroids = asteroids;
    }

/*    public String toString() {
        String data = "";
        data += player.toString() + "\n";
        for (Projectile p : projectiles) data += p.toString() + "\n";
        for (Asteroid a : asteroids) data += a.toString() + "\n";
        return data.trim();
    }
*/
}
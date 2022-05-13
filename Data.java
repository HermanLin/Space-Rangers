import java.io.Serializable;
import java.util.ArrayList;

/**
 * The sole purpose of this class is to provide an easy
 * way to transfer data to and from the server and player.
 * It is passed through the network sockets using the
 * ObjectInputStream and ObjectOutputStream classes. The 
 * fields of this class represents all necessary data for
 * a smooth gaming experience.
 *
 * @author Herman Lin
 * @author Devin Zhu
 */

class Data {

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
}
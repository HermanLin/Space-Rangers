import java.io.Serializable;

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

class Data implements Serializable {

    // player
    public Ship player;
    // player's projectiles
    public ArrayList<Projectile> projectiles;
    // list of asteroids
    public ArrayList<Asteroid> asteroids;

    /**
     * This constructor is used there there is one or a 
     * consistent number of players present on the Server. 
     * Players only need to update each other on their Ships 
     * as well as their Projectiles.
     *
     * @param player the object of the player sending data
     * @param projectiles the object of the projectiles from the
     *                    player sending data
     */
    public Data(Ship player, ArrayList<Projectile> projectiles) {
        this.player = player;
        this.projectiles = projectiles;
    }

    /**
     * This constructor is used when there is a new Player
     * joins a Server where there are already existing 
     * Players. This is because the new Player needs to not 
     * only know where all Players and their Projectiles are,
     * but also the current Asteroid locations.
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
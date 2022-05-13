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

    public Data(Ship player, 
                ArrayList<Projectile> projectiles,
                ArrayList<Asteroid> asteroids) {
        this.player = player;
        this.projectiles = projectiles;
        this.asteroids = asteroids;
    }
}
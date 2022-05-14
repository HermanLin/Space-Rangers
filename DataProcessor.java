import java.util.ArrayList;
// import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.awt.Color;

/**
 * The sole purpose of this class is to provide an easy way for
 * players and servers to process data they want to send or 
 * receive to and from the players and servers. 
 *
 * The data needed for a full update is as follows:
 * - Ship color (represented as three integers)
 * - Ship positionx, positiony, facing
 * - (Projectile p : projectiles)
 *   p positionx, positiony, direction
 * - (Asteroid a : asteroids)
 *   a type, scale, positionx, positiony,
 *     direction, velocityx, velocityy
 *
 * For the projectiles and asteroids, only those that are still
 * alive will be processed
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */

class DataProcessor {

    // ByteArrayOutputStream compiler;

    DataProcessor() {
        // compiler = new ByteArrayOutputStream();
    }

    /**
     * This will compress the necessary values from a game state
     * into a String that can be sent to the Server and 
     * subsequently to other players via an input/output stream.
     *
     * @param player the Ship of the player sending data
     * @param projectiles the list of Projectiles from the player
     * @param asteroids the list of Asteroids from the player
     * @return a formatted String containing game state data
     */
    public String compress(Ship player,
                           ArrayList<Projectile> projectiles,
                           ArrayList<Asteroid> asteroids) {
        // get player properties
        String playerData = player.dataString();
        // get all projectile properties and put into one String
        String projectileData = "";
        for (Projectile p : projectiles) 
            projectileData += p.dataString();
        // get all asteroid properties and put into one string
        String asteroidData = "";
        for (Asteroid a : asteroids) 
            asteroidData += a.dataString();

        // combine all the data into a String, separating each
        // data set with a "SEP "
        return playerData + "SEP " + projectileData + "SEP " + asteroidData;
    }

    /**
     * This will decompress and extract the necessary values from
     * a compressed game state String. The extracted data is put
     * into a Data object that can then be referenced by a player.
     *
     * @param data the String containing all necessary game state 
     *             values from a player
     * @return a Data object containing the player ship, projectiles,
     *         and asteroids
     */
    public Data decompressAll(String data) {
        // each set of data is separated by a special "SEP " element
        String[] sets = data.split("SEP ");
        
        // each set of data is also internally separated by a space
        String[] playerData = sets[0].split(" ");
        String[] projectileData = sets[1].split(" ");
        String[] asteroidData = sets[2].split(" ");

        /* extract the data from each String array into Data */
        int red = Integer.parseInt(playerData[0]);
        int green = Integer.parseInt(playerData[1]);
        int blue = Integer.parseInt(playerData[2]); 
        Color color = new Color(red, green, blue);
        
        // extract player specific data
        double playerX = Double.parseDouble(playerData[3]);
        double playerY = Double.parseDouble(playerData[4]);
        double playerFacing = Double.parseDouble(playerData[5]);
        Ship playerShip = new Ship(color, playerX, playerY, playerFacing);
        
        // extract projectile specific data into an ArrayList
        ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
        for (int i = 0; i < projectileData.length; i += 3) {
            double posX = Double.parseDouble(projectileData[i]);
            double posY = Double.parseDouble(projectileData[i+1]);
            double dir = Double.parseDouble(projectileData[i+2]);
            projectiles.add(new Projectile(color,dir, posX, posY));
        }

        // extract asteroid specific data into an ArrayList
        ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < asteroidData.length; i += 7) {
            int type = Integer.parseInt(asteroidData[i]);
            int scale = Integer.parseInt(asteroidData[i+1]);
            double posX = Double.parseDouble(asteroidData[i+2]);
            double posY = Double.parseDouble(asteroidData[i+3]);
            double velX = Double.parseDouble(asteroidData[i+4]);
            double velY = Double.parseDouble(asteroidData[i+5]);
            double dir = Double.parseDouble(asteroidData[i+6]);
            asteroids.add(new Asteroid(posX, posY, dir, velX, velY,
                                       type, scale, asteroids));
        }

        return new Data(playerShip, projectiles, asteroids);
    }

    /**
     * This decompression method is used only when a player needs
     * only the ship data and projectile data of other players.
     * All players that are already in the game keep track of the
     * asteroids in the universe and update their asteroids 
     * independently. Since all players have the same asteroid 
     * state as all others when they join, that set of data is
     * unneeded when updating others and themselves.
     * 
     * @param data the String containing all necessary game state 
     *             values from a player
     * @return a Data object containing only the player's ship and
     *         associated projectiles
     */
    public Data decompress(String data) {
        // each set of data is separated by a special "SEP " element
        String[] sets = data.split("SEP ");
        
        // each set of data is also internally separated by a space
        String[] playerData = sets[0].split(" ");
        String[] projectileData = sets[1].split(" ");

        /* extract the data from each String array into Data */
        int red = Integer.parseInt(playerData[0]);
        int green = Integer.parseInt(playerData[1]);
        int blue = Integer.parseInt(playerData[2]); 
        Color color = new Color(red, green, blue);
        
        // extract player specific data
        double playerX = Double.parseDouble(playerData[3]);
        double playerY = Double.parseDouble(playerData[4]);
        double playerFacing = Double.parseDouble(playerData[5]);
        Ship playerShip = new Ship(color, playerX, playerY, playerFacing);
        
        // extract projectile specific data into an ArrayList
        ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
        for (int i = 0; i < projectileData.length; i += 3) {
            double posX = Double.parseDouble(projectileData[i]);
            double posY = Double.parseDouble(projectileData[i+1]);
            double dir = Double.parseDouble(projectileData[i+2]);
            projectiles.add(new Projectile(color,dir, posX, posY));
        }

        return new Data(playerShip, projectiles);
    }
/*    public static void main(String[] args) {
        Ship test = new Ship(Color.RED);
        System.out.println(test);
        ArrayList<Projectile> testp = new ArrayList<Projectile>();
        for (int i = 0; i < 3; i++) {
            Projectile newP = 
                new Projectile(test.getColor(),
                               test.getFacing(),
                               test.getTranslatedCentroidX(),
                               test.getTranslatedCentroidY());
            testp.add(newP);
            System.out.println(newP);
        }
        ArrayList<Asteroid> testa = new ArrayList<Asteroid>();
        for (int i = 0; i < 5; i++) {
            Asteroid newA = new Asteroid(testa);
            testa.add(newA);
            System.out.println(newA);
        }
        System.out.println("=====");
        DataProcessor dp = new DataProcessor();
        String woohoo = dp.compress(test, testp, testa);
        System.out.println(woohoo);
        System.out.println("=====");
        
        Data result = dp.decompress(woohoo);
        System.out.println(result);
    }
*/
}
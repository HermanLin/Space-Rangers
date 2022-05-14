import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
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
 *   a type, scale, positionx, positiony, centroidx, centroidy,
 *     direction, velocityx, velocityy
 * For the projectiles and asteroids, only those that are still
 * alive will be processed
 *
 * The full representation of the return byte array in bytes:
 * 4 + 8 + 8 + 8 +                              // player 
 * (n * (8 + 8 + 8)) +                          // projectiles
 * (m * (4 + 4 + 8 + 8 + 8 + 8 + 8 + 8 + 8))    // asteroids
 * = 28 + 24n + 64m bytes
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */

class DataProcessor {

    ByteArrayOutputStream compiler;

    DataProcessor() {
        compiler = new ByteArrayOutputStream();
    }

    /**
     * This will compress the necessary values from a game state
     * into a byte array that can be sent to the Server and 
     * subsequently to other players via an input/output stream.
     *
     * @param player the Ship of the player sending data
     * @param projectiles the list of Projectiles from the player
     * @param asteroids the list of Asteroids from the player
     * @return a formatted byte array containing game state data
     */
    public byte[] compress(Ship player, 
                           ArrayList<Projectile> projectiles,
                           ArrayList<Asteroid> asteroids) {
        // get color and put into buffer
        compiler.write(player.getColor().getRGB());
        // get player properties and put into buffer
        ByteBuffer playerData = ByteBuffer.allocate(28);
        playerData.putDouble(player.getPositionX());
        playerData.putDouble(player.getPositionY());
        playerData.putDouble(player.getFacing());
        compiler.write(playerData.array(), 0, 28);

        // indicator for how many projectiles there are
        // and therefore how many bytes to read all projectiles
        compiler.write(projectiles.size());
        // get projectile properties and put into buffer
        for (Projectile p : projectiles) {
            ByteBuffer projData = ByteBuffer.allocate(24);
            projData.putDouble(p.getPositionX());
            projData.putDouble(p.getPositionY());
            projData.putDouble(p.getDirection());
            compiler.write(projData.array(), 0, 24);
        }
        
        /* the rest of the byte array will contain data
           only about the asteroids */
        // get asteroid properties and put into buffer
        for (Asteroid a : asteroids) {
            ByteBuffer astData = ByteBuffer.allocate(64);
            astData.putInt(a.getType());
            astData.putInt(a.getScale());
            astData.putDouble(a.getPositionX());
            astData.putDouble(a.getPositionY());
            astData.putDouble(a.getCentroidX());
            astData.putDouble(a.getCentroidY());
            astData.putDouble(a.getDirection()); 
            astData.putDouble(a.getVelocityX());
            astData.putDouble(a.getVelocityY());
            compiler.write(astData.array(), 0, 64);          
        }
        
        byte[] compressed = compiler.toByteArray();
        try { compiler.flush(); } // reset compiler
        catch (Exception e) {
            System.out.println("Error: cannot flush data compiler");
        }
        return compressed;
    }   

    public Data decompress(byte[] data) {
        // A ByteBuffer for traversing the data byte array
        // Everytime we grab a value, we increment the position
        ByteBuffer store = ByteBuffer.wrap(data);
        System.out.println(store.array());
        System.out.println(store.get(0) + store.get(1) + store.get(2) + store.get(3));
        System.out.println("=====");

        // variables for holding data to be passed into Data
        Ship playerShip;
        ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
        ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

        // first 28 bytes contain data about the player
        Color playerColor = new Color(store.getInt()); 
        double playerPosX = store.getDouble(4);
        double playerPosY = store.getDouble(12);
        double playerFace = store.getDouble(20);

        playerShip = new Ship(playerColor, playerPosX, playerPosY, playerFace);
        System.out.println(playerShip);
/*
        // next projectileBytes bytes will contain all 
        // information about the projectiles
        int projectileBytes = store.getInt(28) * 24;
        for (int i = 28; i < projectileBytes; i += 8) {
            double pposx = store.get(i);
            double pposy = store.get(i + 8);
            double pdir = store.get(i + 16);
            projectiles.add(new Projectile(playerColor, pposx, pposy, pdir));
        }

        // the rest of the ByteBuffer onlny contains
        // information about the asteroids
        for (int i = 28 + projectileBytes; i < store.limit(); i += 64) {
            int atype = store.get(i);
            int ascale = store.get(i + 4);
            double aposx = store.get(i + 12);
            double aposy = store.get(i + 20);
            double acenx = store.get(i + 28);
            double aceny = store.get(i + 36);
            double adir = store.get(i + 44);
            double avelx = store.get(i + 52);
            double avely = store.get(i + 60);
            asteroids.add(
                new Asteroid(aposx, aposy, adir, avelx, avely, atype, ascale,
                             asteroids));
        }
*/
        // return a Data object containing all the data values
        return new Data(playerShip, projectiles, asteroids);
    }


    public static void main(String[] args) {
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
        byte[] woohoo = dp.compress(test, testp, testa);
        System.out.println(woohoo);
        System.out.println(woohoo[0]+woohoo[1]+woohoo[2]+woohoo[3]);
        System.out.println("=====");
        
        Data result = dp.decompress(woohoo);
        // System.out.println(result);
    }
}
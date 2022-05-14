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
 *   a type, scale, positionx, positiony, centroidx, centroidy,
 *     direction, velocityx, velocityy
 * For the projectiles and asteroids, only those that are still
 * alive will be processed
 *
 * The full representation of the return byte array in bytes:
 * 4 + 4 + 4 + 8 + 8 + 8 +                      // player 
 * (n * (8 + 8 + 8)) +                          // projectiles
 * (m * (4 + 4 + 8 + 8 + 8 + 8 + 8 + 8 + 8))    // asteroids
 * = 36 + 4 + 24n + 4 + 64m bytes
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
        // get player properties and put into buffer
        ByteBuffer playerData = ByteBuffer.allocate(36);
        playerData.putInt(player.getColor().getRed());
        playerData.putInt(player.getColor().getGreen());
        playerData.putInt(player.getColor().getBlue());
        playerData.putDouble(player.getPositionX());
        playerData.putDouble(player.getPositionY());
        playerData.putDouble(player.getFacing());

        // indicator for how many projectiles there are
        // and therefore how many bytes to read all projectiles
        System.out.println("Num Projectiles: " + projectiles.size());
        // compiler.write(projectiles.size());
        ByteBuffer projectilesData = ByteBuffer.allocate(24 * projectiles.size() + 4);
        projectilesData.putInt(projectiles.size());
        // get projectile properties and put into buffer
        for (Projectile p : projectiles) {
            // ByteBuffer projData = ByteBuffer.allocate(24);
            // projData.putDouble(p.getPositionX());
            // projData.putDouble(p.getPositionY());
            // projData.putDouble(p.getDirection());
            // compiler.write(projData.array(), 0, 24);
            projectilesData.putDouble(p.getPositionX());
            projectilesData.putDouble(p.getPositionY());
            projectilesData.putDouble(p.getDirection());
        }
        
        System.out.println("Num Asteroids: " + asteroids.size());
        // compiler.write(asteroids.size());
        ByteBuffer asteroidsData = ByteBuffer.allocate(64 * asteroids.size() + 4);
        asteroidsData.putInt(asteroids.size());
        // get asteroid properties and put into buffer
        for (Asteroid a : asteroids) {
            // ByteBuffer astData = ByteBuffer.allocate(64);
            // astData.putInt(a.getType());
            // astData.putInt(a.getScale());
            // astData.putDouble(a.getPositionX());
            // astData.putDouble(a.getPositionY());
            // astData.putDouble(a.getCentroidX());
            // astData.putDouble(a.getCentroidY());
            // astData.putDouble(a.getDirection()); 
            // astData.putDouble(a.getVelocityX());
            // astData.putDouble(a.getVelocityY());
            // compiler.write(astData.array(), 0, 64);  
            asteroidsData.putInt(a.getType());
            asteroidsData.putInt(a.getScale());
            asteroidsData.putDouble(a.getPositionX());
            asteroidsData.putDouble(a.getPositionY());
            asteroidsData.putDouble(a.getCentroidX());
            asteroidsData.putDouble(a.getCentroidY());
            asteroidsData.putDouble(a.getDirection()); 
            asteroidsData.putDouble(a.getVelocityX());
            asteroidsData.putDouble(a.getVelocityY());
        }
        
        // byte[] compressed = compiler.toByteArray();
        // try { compiler.flush(); } // reset compiler
        // catch (Exception e) {
        //     System.out.println("Error: cannot flush data compiler");
        // }
        ByteBuffer compressed = ByteBuffer.allocate(playerData.capacity() + 
                                                    projectilesData.capacity() +
                                                    asteroidsData.capacity());
        compressed.put(playerData);
        compressed.put(projectilesData);
        compressed.put(asteroidsData);
        return compressed.array();
    }   

    public Data decompress(byte[] data) {
        // A ByteBuffer for traversing the data byte array
        // Everytime we grab a value, we increment the position
        ByteBuffer store = ByteBuffer.wrap(data);

        // variables for holding data to be passed into Data
        Ship playerShip;
        ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
        ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

        // first 28 bytes contain data about the player
        Color playerColor = new Color(store.getInt(),   
                                      store.getInt(),  
                                      store.getInt()); 
        double playerPosX = store.getDouble();        
        double playerPosY = store.getDouble();        
        double playerFace = store.getDouble();

        playerShip = new Ship(playerColor, playerPosX, playerPosY, playerFace);
        System.out.println(playerShip);
/*
        // next n bytes contain information about the projectiles
        // System.out.println("Position: " + store.position());
        int numProjectiles = store.get();
        store.position(store.position() + 3);
        // System.out.println("Position: " + store.position());
        System.out.println(numProjectiles);
        System.out.println("=====");
        for (int i = 0; i < numProjectiles; i++) {
            // System.out.println("Projectile " + i);
            double pposx = store.getDouble();
            double pposy = store.getDouble();
            double pdir = store.getDouble();
            // System.out.println("Position: " + store.position());
            projectiles.add(new Projectile(playerColor, pposx, pposy, pdir));
        }
*/
        // next m bytes contain information about the projectiles
        
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
        System.out.println("=====");
        
        Data result = dp.decompress(woohoo);
        // System.out.println(result);
    }
}
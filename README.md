# Space-Rangers

_By Herman Lin and Devin Zhu_

---

## About the Game

Space Rangers is a small remake of the original Asteroids arcade game with an added two-player feature. You control your own spaceship within the game and work with another player to shoot down asteroids all around you!

---

## Steps to Run the Game

1. First, both players need to compile all the Java files.

    `$ javac *.java`

2. Then, one player starts up the server.

    `$ java Server`

3. Next, both players run the SpaceRangers program.

    `$ java SpaceRangers`

4. Have both players enter the server address (IPv4 address of the server host), choose their spaceship color, and then _Join the Space Rangers_! **NOTE:** Only two players can join a server at a time. A third player attempting to connect will be given an error message.

5. Shoot down the asteroids!

---

## How to Play

<table align=center>
    <tr>
        <th>Action</th>
        <th>Input</th>
    </tr>
    <tr>
        <td>Forwards</td>
        <td>UP ARROW</td>
    </tr>
    <tr>
        <td>Backwards</td>
        <td>DOWN ARROW</td>
    </tr>
    <tr>
        <td>Turn Left</td>
        <td>LEFT ARROW</td>
    </tr>
    <tr>
        <td>Turn Right</td>
        <td>RIGHT ARROW</td>
    </tr>
    <tr>
        <td>Shoot Missles</td>
        <td>SPACEBAR</td>
    </tr>
</table>

---

## Programming Features

As part of the project, we were tasked to implement three of five programming topics we learned during our second half of the semester. We chose to implement the following:

### - Networking

In order to implement the multiplayer feature, we utilized Java **_Sockets_** to create a method of communication between devices. The **_Server_** accepts all connections to the host machine's IPv4 address with the default port `5190` while the **_Client_** attempts to connect to the server through the Socket connection and communicates game information through the Socket communication. When a player closes their game, the Sockets are automatically closed and removed from the list of connections to the game. At this point, another player is able to join the same Server.

### - Thread Concurrency

In order to fully implement the multiplayer feature, we had to decide on how each player would be able to display the other without disrupting gameplay as much as possible. The main part of this feature was to have three threads operating the game at the same time, and interacting with a single Queue data structure in different ways. One thread is used to send data packets to the Server, which gets sent to the other player. Another thread is to read data packets from the Server, process the packets, and add it to the Queue of updates. The third thread is responsible for updating the graphics, which includes polling the Queue for an update and drawing both the player's game state as well as the other player's game state. 

### - Graphics

The game uses Java's **_AWT_** and **_Swing_** libraries to create an interactive environment for players. Each player is able to view both their and the  other player control their respective spaceship in _almost_ real time. As part of the design implementation to reduce as much delay between graphics updates as possible, we decided to have each player manage their own asteroids as much as possible. The data packets sent between the players contain important information not only on the other player's ship and projectiles, but also their asteroids states. However, drawing the other player's asteroids would be redundant since each player should ideally have the same asteroids in play. As a result, the only time the one player draws another's asteroids is when the player joins the game when the other player is already playing. We also chose only the most important information to be sent through the data packets, such as positional data, but no velocity data. This allows for less computation when updating the graphics and thus improves the graphical update speeds.

---

## Known (and Unknown) Bugs and Glitches

* Holding down the Spacebar to shoot missles will shoot a stream of projectiles instead of one at a time. This supposed special feature has the potential to crash a player's game when playing multiplayer.

* At some points, projectile updates from one player can be delayed compared to the current asteroid states of the other player's. This can result in shooting down one asteroid from one perspective, but the same asteroid still alive in the other perspective. 

* Although it has not reached that point through long enough testing, it is possible for the update Queue data structure to consume a lot of memory if the graphics update thread does not consume elements from the Queue fast enough. This can either slow down the gameplay or even crash the game in the long run.

---

<br>
</br>

#### Special thanks to Professor Katz for an amazing semester of learning Java!
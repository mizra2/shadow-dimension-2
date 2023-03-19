import bagel.*;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Rectangle;
import bagel.util.Point;
import bagel.util.Vector2;
import java.lang.reflect.Array;
import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2022
 *
 * Please enter your name below
 * Milad Izra
 */

public class ShadowDimension extends AbstractGame implements Sprites {

    /*********** Attributes ***************/

    /** Width of the window */
    private final static int WINDOW_WIDTH = 1024;

    /** Window Height */
    private final static int WINDOW_HEIGHT = 768;

    /** Window Height */
    private final static String GAME_TITLE = "SHADOW DIMENSION";

    /** Menu Text 1 */
    private final static String MENU_TEXT_1 = "PRESS SPACE TO START";

    /** Menu Text 2 */
    private final static String MENU_TEXT_2 = "USE ARROW KEYS TO FIND GATE";

    /** Message displayed when player wins*/

    private final static String WIN_MESSAGE = "CONGRATULATIONS!";

    /** Message displayed when player loss*/

    private final static String LOSE_MESSAGE = "GAME OVER!";

    /** Fonts */
    private Font healthFont = new Font("res/frostbite.ttf", 30);

    /** Font at the start menu */
    private Font gameStartFont = new Font("res/frostbite.ttf", 40);

    /** General Default Font */
    private Font FONT = new Font("res/frostbite.ttf", 75);

    /** Various Draw Options */
    private DrawOptions healthOptions = new DrawOptions();

    private DrawOptions startScreen = new DrawOptions();

    /** Has the player failed the game? */
    private boolean failedGameState = false;

    /** HashMap with Players corrospending to each level */
    private HashMap<Integer, Player> players = new HashMap<>();

    /** Max Health Of A Player */
    private final int MAX_HEALTH = 100;

    /** Rectangle for the gate */
    private Rectangle gatePortalCollider2D = new Rectangle(new Point(950, 670), 49, 74);

    /** Level 0 */
    private Level level0 = new Level("res/background0.png");

    /** Level 1 */
    private Level level1 = new Level("res/background1.png");

    /** An ArrayList containing all the enemies */
    private ArrayList<Demon> enemies = new ArrayList<>();

    /** Map Containing a List of World Objects
     * Key 0: Walls
     * Key 1: Trees */
    private Map<Integer, List<WorldObject>> worldObjects = new HashMap<>();

    /** Key : List of Collidables for Level 0, 1*/
    private Map<Integer, List<Collidable>> worldCollideables = new HashMap<>();

    /** Frame Counter For Stage 0 Tranisiton Screen */
    private int transitionCounter = 0;

    /** Colour Constants for Health Bar */
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);
    private final static Timescale TIMESCALE = Timescale.getTimescale();

    /** Level Constants for CSV */
    private final static char LEVEL_0 = '0';
    private final static char LEVEL_1 = '1';

    private final static int GATE_X = 950;

    private final static int GATE_Y = 670;

    private final static int REFRESH_RATE = 60;

    private final static int TRANSITION_TIME = 3000;

    /** Game Constructor */
    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        // Generate the map
        this.readCSV("res/level0.csv");
        this.readCSV("res/level1.csv");
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method used to read file and create objects (You can change this
     * method as you wish).
     */
    private void readCSV(String fileName){
        // Array List containing WorldObjects
        List<WorldObject> list = new ArrayList<>();
        // Collidables for Stage 0
        List<Collidable> collidables0 = new ArrayList<>();
        // Collideables for Stage 1
        List<Collidable> collidables1 = new ArrayList<>();

        if(fileName.charAt(9) == LEVEL_0) {
            worldObjects.put(0, list);
            worldCollideables.put(0, collidables0);
        } else if(fileName.charAt(9) == LEVEL_1) {
            worldObjects.put(1, list);
            this.worldCollideables.put(1, collidables1);
        }

        // Read a CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String text;
            int count;
            while((text = br.readLine()) != null) {
                double x, y;
                String cells[] = text.split(",");
                switch (cells[0].toLowerCase()) {
                    case "fae":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        if(fileName.charAt(9) == LEVEL_0) {
                            players.put(0, new Player(new Point(x, y)));

                        } else {
                            players.put(1, new Player(new Point(x, y)));
                        }
                        break;
                    case "wall":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        list.add(new WorldObject(new Point(x, y), 0, new Image(WALL_SPRITE)));
                        break;
                    case "sinkhole":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        if(fileName.charAt(9) == LEVEL_0) {
                            collidables0.add(new Collidable(new Point(x, y)));
                        } else if (fileName.charAt(9) == LEVEL_1) {
                            collidables1.add(new Collidable(new Point(x, y)));
                        }
                        break;
                    case "topleft":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        if(fileName.charAt(9) == LEVEL_0) {
                            level0.setLeftBound(new Point(x, y));
                            System.out.println(level0.getLeftBoundX());
                            System.out.println(level0.getLeftBoundY());
                        }
                        if(fileName.charAt(9) == LEVEL_1) {
                            level1.setLeftBound(new Point(x, y));
                            System.out.println(level1.getLeftBoundX());
                            System.out.println(level1.getLeftBoundY());
                        }
                        break;
                    case "bottomright":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        if(fileName.charAt(9) == LEVEL_0) {
                            level0.setRightBound(new Point(x, y));
                        } else if (fileName.charAt(9) == LEVEL_1) {
                            level1.setRightBound(new Point(x, y));
                        }
                        break;
                    case "demon":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        enemies.add(new Demon(new Point(x,y),  150, false));
                        break;
                    case "navec":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        enemies.add(new Demon(new Point(x, y), 200, true));
                        break;
                    case "tree":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        list.add(new WorldObject(new Point(x, y), 1, new Image(TREE_SPRITE)));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(fileName.charAt(9) == LEVEL_0) {
            worldObjects.put(0, list);
            worldCollideables.put(0, collidables0);
        } else if(fileName.charAt(9) == LEVEL_1) {
            worldObjects.put(1, list);
            this.worldCollideables.put(1, collidables1);
        }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        /** Game has not yet started till player hits ENTER */
        if (!level0.getStart()) {
            startGameState(input);
            /**  Game Started, Render Stage 0 */
        } else if (level0.getStart() && !level0.getState() && !failedGameState && !level1.getStart()){
            /** Get the player for this stage */
            Player player = players.get(0);
            /** Render the level (background) and associated entities */
            level0.renderLevel();
            drawMap(player, 0, input);
            renderPlayer(player, input, level0);
            checkGateConditon(player);
        } else if (level0.getStart() && level0.getState() && !level1.getStart()) {
            /** Stage 0 Complete -- Draws Tranisiton Screen to Stage 1 */
            drawLevelTransition(input);
        } else if(level1.getStart() && !level1.getState() && !failedGameState) {
            /** Get the player for this stage */
            Player player = players.get(1);
            /** Render the level (background) and associated entities */
            level1.renderLevel();
            drawMap(player, 1, input);
            renderPlayer(player, input, level1);
        } else if(level0.getState() && level1.getState()) {
            /** You've won the game!~ */
            drawMessage(WIN_MESSAGE);
        } else if (failedGameState) {
            /** Player lost the game  */
            drawMessage(LOSE_MESSAGE);
        }
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
    }

    /** Draws the start game screen
     * @param input - A Players Input
     */
    private void startGameState(Input input) {
        FONT.drawString(GAME_TITLE, 260, 250);
        gameStartFont.drawString(MENU_TEXT_1, 350, 440);
        gameStartFont.drawString(MENU_TEXT_2, 350, 480);
        if (input.wasPressed(Keys.SPACE)) {
            level0.setStart(true);
        }
    }

    /** Draws a message to the player
     * i.e player wins screen
     * @param message - The message you want to display
     */
    private void drawMessage(String message) {
        FONT.drawString(message, Window.getWidth() / 2.0 - FONT.getWidth(message) / 2.0, Window.getHeight() / 2.0);
    }

    /** Check if the player has collided with the gate
     * i.e player reached end of stage 0
     * @param player - Players rectangle we want to check
     */
    private void checkGateConditon(Player player) {
        if (player.getX() >= GATE_X && player.getY() >= GATE_Y) {
            level0.setState(true);
        }
    }

    /** Renders the map and it's Entities / WorldObjects
     * @param player - The maps player we want to render
     * @param level - The stage we want to render
     */
    private void drawMap(Player player, int level, Input input) {

        List<WorldObject> walls = worldObjects.get(0);
        List<WorldObject> trees = worldObjects.get(1);

        List<WorldObject> objects;

        if(level == 0) {
            objects = worldObjects.get(0);
        } else {
            objects = worldObjects.get(1);
        }
        List<Collidable> sinkholes = worldCollideables.get(level);

        /** Draw and check for collisons for worldobjects associated with the level*/
        for(WorldObject object: objects) {
            object.getEntitySprite().drawFromTopLeft(object.getX(), object.getY());
            if (player.onCollision(object.getBoxCollider2D())) {
                player.movePlayer(player.getOldPos());
            }
            checkDemonCollison(object.getBoxCollider2D());
        }

        /** Render Sinkholes and remove the ones that have been triggered
         * by a player
         */
        List<Collidable> triggered = new ArrayList<>();
        for (Collidable sinkhole: sinkholes) {

            checkDemonCollison(sinkhole.getBoxCollider2D());

            sinkhole.getEntitySprite().drawFromTopLeft(sinkhole.getX(), sinkhole.getY());

            if (player.onCollision(sinkhole.getBoxCollider2D())) {
                sinkhole.dealDmg(player);

                System.out.format("Sinkhole inflicts 30 damage points on Fae. Fae's Current Health is: %d/100\n",
                        player.getHealth());

                triggered.add(sinkhole);
            }
        }
        /** Remove triggered Collideables*/
        worldCollideables.get(level).removeAll(triggered);

        /** Render Demons and remove the ones that have been killed
         * by a player
         */
        List<Demon> enemiesKilled = new ArrayList<>();
        if(level==1) {
            for (Demon demon: this.enemies) {
                if(demon.getHealth() <= 0) {
                    enemiesKilled.add(demon);
                }

                demon.moveDemon();
                demon.getEntitySprite().drawFromTopLeft(demon.getX(),  demon.getY());
                demon.drawHealth();

                if ((demon.getX() < level1.getLeftBoundX() || demon.getY() < level1.getLeftBoundY()
                        || demon.getX() > level1.getRightBoundX() || demon.getY() > level1.getRightBoundY())) {
                    demon.reverseDirection();
                }

                demon.invisCooldown();
                attackDemon(demon);
                demon.fireProjectile(player);
                bossDefeated(demon);

            }
        }

        /** Check for changes in timescale */
        TIMESCALE.changeScale(input, enemies);
        /** Remove the killed enemies */
        enemies.removeAll(enemiesKilled);
        /** Check and count players invisibility period */
        players.get(level).invisCooldown();
    }

     /** Used to move a player
     * @param input - The users input used to control the player.
     * @param player - The player that is being controlled/
     * @param level - The level that the player is being moved on.
     */
    private void playerMover(Input input, Player player, Level level) {
        /** Control the player*/
        if (input.isDown(Keys.LEFT)) {
            player.setLeftSprite();
            player.setOldPoint();
            player.movePlayer(new Point(player.getX() - player.getSpeed(), player.getY()));
            player.setDirectionVector(Vector2.left);
        }
        else if (input.isDown(Keys.RIGHT)) {
            player.setRightSprite();
            player.setOldPoint();
            player.movePlayer(new Point(player.getX() + player.getSpeed(), player.getY()));
            player.setDirectionVector(Vector2.right);
        }
        else if (input.isDown(Keys.UP)) {
            player.setOldPoint();
            player.movePlayer(new Point(player.getX(), player.getY() - player.getSpeed()));
        }
        else if (input.isDown(Keys.DOWN)) {
            player.setOldPoint();
            player.movePlayer(new Point(player.getX(), player.getY() + player.getSpeed()));
        }
        /** Player Attacks! */
        if(input.wasPressed(Keys.A)) {
            if (player.attack()) {
                player.setHasAttacked(false);
            }
        }
        /** If player is over the levels bound, move back to oldPos */
        if (player.getX() < level.getLeftBoundX() || player.getY() < level.getLeftBoundY()
                || player.getX() > level.getRightBoundX() || player.getY() > level.getRightBoundY()) {
            player.movePlayer(player.getOldPos());
        }
    }

     /** Renders the players health at the top left
     * @param player - The players health we wish to render
     */
    private void drawHealth(Player player) {
        /** If players health is below 0 due to the damage dealt, set it back to 0 */
        if (player.getHealth() <= 0) {
            player.setHealth(0);
        }
        /** If players health greater than 65, then set it to green, if between 35 & 65 orange,
         if less than 35, set to red */

        if (player.getHealth() > 65) {
            healthOptions.setBlendColour(GREEN);
            String healthString = String.format("%d%%", player.getHealth());
            healthFont.drawString(healthString, 20, 25, healthOptions);
        } else if (player.getHealth() < 65 && player.getHealth() > 35) {
            healthOptions.setBlendColour(ORANGE);
            String healthString = String.format("%d%%", player.getHealth());
            healthFont.drawString(healthString, 20, 25, healthOptions);
        } else {
            healthOptions.setBlendColour(RED);
            String healthString = String.format("%d%%", player.getHealth());
            healthFont.drawString(healthString, 20, 25, healthOptions);
        }
        /** If players health is 0, then you lose! */
        if (player.getHealth() <= 0) {
            failedGameState = true;
        }
    }

    /** Render the tranisiton screen to stage 1
     * @param input - A users input
     */
    public void drawLevelTransition(Input input) {
        transitionCounter++;
        if ((transitionCounter * 1000) / REFRESH_RATE < TRANSITION_TIME) {
            FONT.drawString("Stage Complete", Window.getWidth() / 2.0 - FONT.getWidth("Stage Complete") / 2.0,
                    Window.getHeight() / 2.0);
        } else {
            gameStartFont.drawString("PRESS SPACE TO START", 350, 350);
            gameStartFont.drawString("PRESS A TO ATTACK", 370, 400);
            gameStartFont.drawString("DEFEAT NAVEC TO WIN", 355, 450);
            if (input.wasPressed(Keys.SPACE)) {
                level1.setStart(true);
            }
        }
    }

    /** Method that checks if the levels
     * demons are colliding with another object
     * @param - An objects parameter
     */
    public void checkDemonCollison(Rectangle rectangle) {
        for (Demon enemy: this.enemies) {
            if(enemy.onCollision(rectangle)) {
                enemy.reverseDirection();
            }
        }
    }

    /** Check if player attacks a demon
     * and log the action occuring
     * @param demon - The demon that is being attacked
     */
    public void attackDemon(Demon demon) {
        DecimalFormat df = new DecimalFormat("#");
        /** If a player is in attack stage, and the demon is not invis */
        if(players.get(1).onCollision(demon.getDemonBoxCollider2D()) &&
                players.get(1).getAttackState() && !demon.getInvisState()) {

            demon.takesDamage();
            demon.setInvinState(true);

            /** Demon becomes invis for 3 seconds */
            /** Change demons sprite */

            if(!demon.bossStatus()) {
                String string = String.format("Player deals 20 damage points on Demon: Demon's current health: %s / %d\n",
                        df.format(demon.getHealth()), 40);

                System.out.printf(string);

            } else {
                String string = String.format("Player deals 20 damage points on Navec: Navec's current health: %s / %d\n",
                        df.format(demon.getHealth()), 80);

                System.out.printf(string);
            }
            if(!demon.getAgressive() && !demon.bossStatus()) {
                demon.setEntitySprite(new Image(DEMON_INVIS_RIGHT));
            } else {
                demon.setEntitySprite(new Image(DEMON_INVIS_RIGHT));
            }
            if (demon.bossStatus()) {
                if(demon.getDirectionVector().y == 1 || demon.getDirectionVector().y == -1) {
                    demon.setEntitySprite(new Image(NAVEC_INVIS_RIGHT));
                }
            }
        }
    }

    /** Check if Navec has been defeated
     * ends stage 1 if so.
     * @param demon Check if it's of type
     * navec and check if demon has been defeated
     */
    public void bossDefeated(Demon demon) {
        if(demon.bossStatus() && demon.getHealth() <= 0) {
            level1.setState(true);
        }
    }

    /** Render a player on the screen and all
     * it's associted attributes (health etc..)
     * @param input A users input to control the player
     * @param player The player you want to render
     * @param level The level on which the player is moving on
     * */
    public void renderPlayer(Player player, Input input, Level level) {
        player.drawPlayer();
        playerMover(input, player, level);
        drawHealth(player);
        player.attackCooldown();
    }
}

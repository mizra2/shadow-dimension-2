import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Demon extends Entity implements Cooldown, Sprites {

    /*********** Attributes ***************/

    /** Unit vector representing the direction the demon will move */
    private Vector2 directionVector;

    /** Is the demon in a invinciable state */
    private boolean invinState;

    /** Current health of the demon */
    private double health;

    /** Attack Range of the Demon */
    private final int ATTACK_RANGE;

    /** Movespeed of the demon */
    private double movementSpeed;

    /** Random Seed */
    private Random random = new Random();

    /** Is the demon in an aggressive state */
    private boolean isAgressive = false;

    /** Rectangled box collider */
    private Rectangle demonBoxCollider2D;

    /** Is the demon of type navec */
    private boolean isBoss;

    /** Font of the health bar */
    private Font healthFont = new Font("res/frostbite.ttf", 15);

    /** Drawing optons for the health text */
    private DrawOptions healthOptions = new DrawOptions();

    /** Demons Invis Duration */
    private int invisDuration = 1;

    /** A demons fire projectile*/
    private Projectile projectile;

    /** A demons maximum health */
    private final static int DEMON_MAX_HEALTH = 40;

    /** Navecs maximum health */
    private final static int NAVEC_MAX_HEALTH = 80;

    /** Rotations for a demons fire projectile */
    private final static DrawOptions ROTATION = new DrawOptions();

    /** A demons base movement speed, used to sucessfully apply timescale factors. */
    private final double BASE_SPEED;

    /** MAX BASE SPEED OF A DEMON*/
    private final double MAX_SPEED = 0.7;

    /** MIN BASE SPEED OF A DEMON*/
    private final double MIN_SPEED = 0.2;

    /** Colour Constants for Health Bar */
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);

    /** Health Bar Bounds */
    private final double UPPER_BOUND = 0.65;
    private final double MID_BOUND = 0.35;

    /****************************************************/

    /** Assumed refresh rate of a user? lol */
    private final static int REFRESH_RATE = 60;

    /** Projectile Rotations */
    private final static double BOTTOM_LEFT = -1 * Math.PI / 2;

    private final static double TOP_RIGHT = Math.PI / 2;

    private final static double BOTTOM_RIGHT = -1 * Math.PI;
    /****************************************************/
    /** Creates a demon object
     * @param point - Demons starting location
     * @param range - Demons Attack Range
     * @param isBoss - If demon is of type Navec
     */
    public Demon(Point point, int range, boolean isBoss) {
        super(point, new Image(DEMON_RIGHT));
        this.demonBoxCollider2D = this.getEntitySprite().getBoundingBox();
        this.isBoss = isBoss;
        this.projectile = new Projectile(new Image(DEMON_FIRE));
        health = DEMON_MAX_HEALTH;
        /** If the Demon is of type boss, then we change it accordingly */
        if (isBoss) {
            health = NAVEC_MAX_HEALTH;
            this.setEntitySprite(new Image(NAVEC_RIGHT));
            this.demonBoxCollider2D = this.getEntitySprite().getBoundingBox();
            isAgressive = true;
            this.projectile = new Projectile(new Image(NAVEC_FIRE));
        }

        this.demonBoxCollider2D.moveTo(getEntityPos());
        /** Randomlly assign aggressiveness */
        if(random.nextBoolean() || isBoss) {
            isAgressive = true;
            directionVector = randomVector();
        }
        /** Randomly pick the demons movespeed between 0.2 - 0.7 */
        this.movementSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * random.nextDouble();
        this.ATTACK_RANGE = range;
        BASE_SPEED = movementSpeed;
    }

    /** Returns true if a demon collides with
     * a rectangle
     * @param o - A rectangle that the player may collide with
     * @return True if player collides with an object
     */
    public boolean onCollision(Rectangle o) {
        return this.demonBoxCollider2D.intersects(o);
    }

    public void setDirectionVector(Vector2 vector) {this.directionVector = vector;}

    public Vector2 getDirectionVector() {return this.directionVector;}

    /** Move demon in the demons direction vector
     * and change their sprite depending on the current
     * direction they are facing
     */
    public void moveDemon() {
        if(directionVector == null) {
            return;
        }
        if(directionVector == Vector2.right) {
            if(!this.isBoss) {
                if(invinState) {
                    setEntitySprite(new Image(DEMON_INVIS_RIGHT));
                } else {
                    setEntitySprite(new Image(DEMON_RIGHT));
                }
            } else if(isBoss) {
                if(invinState) {
                    setEntitySprite(new Image(NAVEC_INVIS_RIGHT));
                } else {
                    setEntitySprite(new Image(NAVEC_RIGHT));
                }
            }
            setEntityPos(new Point(this.getEntityPos().x + movementSpeed, this.getEntityPos().y));
            this.demonBoxCollider2D.moveTo(this.getEntityPos());
        }
        if(directionVector == Vector2.left) {
            if(!this.isBoss) {
                if(invinState) {
                    setEntitySprite(new Image(DEMON_INVIS_LEFT));
                } else {
                    setEntitySprite(new Image(DEMON_LEFT));
                }
            } else if(isBoss) {
                if(invinState) {
                    setEntitySprite(new Image(NAVEC_INVIS_LEFT));
                } else {
                    setEntitySprite(new Image(NAVEC_LEFT));
                }
            }
            setEntityPos(new Point(this.getEntityPos().x - movementSpeed, this.getEntityPos().y));
            this.demonBoxCollider2D.moveTo(this.getEntityPos());
        }
        if(directionVector == Vector2.down) {
            if(isBoss){
                if(invinState) {
                    setEntitySprite(new Image(NAVEC_INVIS_RIGHT));
                } else if(!invinState) {
                    setEntitySprite(new Image(NAVEC_RIGHT));
                }
            } else if(!isBoss) {
                if(invinState) {
                    setEntitySprite(new Image(DEMON_INVIS_RIGHT));
                } else if(!invinState) {
                    setEntitySprite(new Image(DEMON_RIGHT));
                }
            }
            setEntityPos(new Point(this.getEntityPos().x, this.getEntityPos().y + movementSpeed));
            this.demonBoxCollider2D.moveTo(this.getEntityPos());
        }
        if(directionVector == Vector2.up) {
            if(isBoss){
                if(invinState) {
                    setEntitySprite(new Image(NAVEC_INVIS_RIGHT));
                } else if(!invinState) {
                    setEntitySprite(new Image(NAVEC_RIGHT));
                }
            } else if(!isBoss) {
                if(invinState) {
                    setEntitySprite(new Image(DEMON_INVIS_RIGHT));
                } else if(!invinState) {
                    setEntitySprite(new Image(DEMON_RIGHT));
                }
            }
            setEntityPos(new Point(this.getEntityPos().x, this.getEntityPos().y - movementSpeed));
            this.demonBoxCollider2D.moveTo(this.getEntityPos());
        }
    }

    /** Reverse the vector direction
      * vector if Demon collides with an object
      */
    public void reverseDirection() {
        if(getDirectionVector() == null) { return; }
        if(getDirectionVector() == Vector2.right) {
            setDirectionVector(Vector2.left);
        } else if(getDirectionVector() == Vector2.left) {
            setDirectionVector(Vector2.right);
        } else if(getDirectionVector() == Vector2.down) {
            setDirectionVector(Vector2.up);
        } else if(getDirectionVector() == Vector2.up) {
            setDirectionVector(Vector2.down);
        }
    }
    public Rectangle getDemonBoxCollider2D() {
        return this.demonBoxCollider2D;
    }

    /** Demon takes damage if attacked
    by player */
    public void takesDamage() {
        if(!invinState) {
            this.health -= 20;
        }
    }

    /** Deal damage to a player depending on demon type
     * @param player - The player taking damage
     */
    public void dealDamage(Player player) {
        if(isBoss) {player.setHealth(player.getHealth() - 20);}
        else {player.setHealth(player.getHealth() - 10);}
    }
    public boolean getInvisState() {return this.invinState;}

    /** Cooldown implementation for the demon's invis state
     * if duration is longer than INVINCIBLE_DURATION, then
     * demon comes out of Invisiblity
     * */
    public void invisCooldown() {
        if(invinState) {
            invisDuration++;
        }
        if ((invisDuration * 1000) / REFRESH_RATE == INVINCIBLE_DURATION) {
            invinState = false;
            invisDuration = 1;
            if(!isAgressive && !isBoss) {
                setEntitySprite(new Image(DEMON_RIGHT));
            }
        }
    }
    public void setInvinState(boolean state) {this.invinState = state;}

    public double getHealth() {return this.health;}

    public void setHealth(int health) {
        this.health = health;
    }

    /** Method to render players health according to the demons
      * current health. If the demons health reduces below 0,
      * set it automatically to 0
      */
    public void drawHealth() {
        if (getHealth() <= 0) {
            setHealth(0);
        }
        DecimalFormat df = new DecimalFormat("#%");
        /** If demon health greater than 65, then set it to green, if between 35 & 65 orange,
         if less than 35, set to red **/
        if(!isBoss) {
            if ((getHealth() / DEMON_MAX_HEALTH) > UPPER_BOUND) {
                healthOptions.setBlendColour(GREEN);
                double health = (getHealth()) / DEMON_MAX_HEALTH;
                healthFont.drawString(df.format(health), getX(), getY() - 6, healthOptions);
            } else if ((getHealth() / DEMON_MAX_HEALTH) < UPPER_BOUND && (getHealth() / DEMON_MAX_HEALTH) > MID_BOUND) {
                healthOptions.setBlendColour(ORANGE);
                double health = (getHealth()) / DEMON_MAX_HEALTH;
                healthFont.drawString(df.format(health), getX(), getY() - 6, healthOptions);
            } else {
                healthOptions.setBlendColour(RED);
                double health = (getHealth()) / DEMON_MAX_HEALTH;
                healthFont.drawString(df.format(health), getX(), getY() - 6, healthOptions);
            }
        } else {
            if ((getHealth() / NAVEC_MAX_HEALTH) > UPPER_BOUND) {
                healthOptions.setBlendColour(GREEN);
                double health = (getHealth()) / NAVEC_MAX_HEALTH;
                healthFont.drawString(df.format(health), getX(), getY() - 6, healthOptions);
            } else if ((getHealth() / NAVEC_MAX_HEALTH) < UPPER_BOUND && (getHealth() / NAVEC_MAX_HEALTH) > MID_BOUND) {
                healthOptions.setBlendColour(ORANGE);
                double health = (getHealth()) / NAVEC_MAX_HEALTH;
                healthFont.drawString(df.format(health), getX(), getY() - 6, healthOptions);
            } else {
                healthOptions.setBlendColour(Colour.RED);
                double health = (getHealth()) / NAVEC_MAX_HEALTH;
                healthFont.drawString(df.format(health), getX(), getY() - 6, healthOptions);
            }
        }
    }

    /** Check if player can be attacked depending
     * on it's attack and or invis stage.
     * Logs the action occuring
     * @param player - The player being attacked
     */
    public void attackPlayer(Player player) {
        if(!player.getAttackState() && !player.getInvState()) {
            dealDamage(player);
            player.setInvincibleState(true);
            if(bossStatus()) {
                String string = String.format("Navec deals 20 damage points on Player: Fae's current health: %d / %d\n", player.getHealth(), 100);
                System.out.printf(string);
            } else {
                String string = String.format("Demon deals 10 damage points on Player: Fae's current health: %d / %d\n", player.getHealth(), 100);
                System.out.printf(string);
            }
        }
    }

    /** Rendering the demons projectile if a
     * player enters the demons attack range
     * @param player - the player we wish to potentially
     * fire a projectile at.
     */
    public void fireProjectile(Player player) {
        if(Math.hypot(getCenter().x - player.getCenter().x, getCenter().y - player.getCenter().y) <= getAttackRange()) {
            int quadrant = determineQuadrant(player);
            switch (quadrant) {
                case 0:
                    projectile.getProjectileSprite().drawFromTopLeft(getX() - projectile.getWidth(),
                            getY() - projectile.getHeight());
                    projectile.getBoxCollider().moveTo(new Point(getX() - projectile.getWidth(),
                            getY() - projectile.getHeight()));
                    if (player.onCollision(projectile.getBoxCollider())) {
                        attackPlayer(player);
                    }
                    break;
                case 1:
                    ROTATION.setRotation(BOTTOM_LEFT);
                    projectile.getProjectileSprite().drawFromTopLeft(getX() - projectile.getWidth(),
                            getY() + getEntitySprite().getHeight(), ROTATION);
                    projectile.getBoxCollider().moveTo(new Point(getX() - projectile.getWidth(),
                            getY() + getEntitySprite().getHeight()));
                    if (player.onCollision(projectile.getBoxCollider())) {
                        attackPlayer(player);
                    }
                    break;
                case 2:
                    ROTATION.setRotation(TOP_RIGHT);
                    projectile.getProjectileSprite().drawFromTopLeft(getX() + getEntitySprite().getWidth(),
                            getY() - projectile.getHeight(),
                            ROTATION);
                    projectile.getBoxCollider().moveTo(new Point(getX() + getEntitySprite().getWidth(),
                            getY() - projectile.getHeight()));
                    if (player.onCollision(projectile.getBoxCollider())) {
                        attackPlayer(player);
                    }
                    break;
                case 3:
                    ROTATION.setRotation(BOTTOM_RIGHT);
                    projectile.getProjectileSprite().drawFromTopLeft(getX() + getEntitySprite().getWidth(), getY() + getEntitySprite()
                            .getHeight(), ROTATION);
                    projectile.getBoxCollider().moveTo(new Point(getX() + getEntitySprite().getWidth(), getY() + getEntitySprite()
                            .getHeight()));
                    if (player.onCollision(projectile.getBoxCollider())) {
                        attackPlayer(player);
                    }
                    break;
            }
        }
    }


    /** Determines which quadrant a player lies
     * respective to a demons 4 corners
     * @param player - The players quadrant
     * we wish to determine
     */
    public int determineQuadrant(Player player) {
        if(player.getCenter().x <= getCenter().x && player.getCenter().y <= getCenter().y) {
            return 0;
        } else if(player.getCenter().x <= getCenter().x && player.getCenter().y > getCenter().y) {
            return 1;
        } else if(player.getCenter().x > getCenter().x && player.getCenter().y <= getCenter().y) {
            return 2;
        } else {
            return 3;
        }
    }

    /** Returns a random 2D vector in 1 of 4
     * directions
     * @return A random vector (Up, Down, Left, Right) */
    private Vector2 randomVector() {
        Vector2 vector2 = new Vector2();
        /** Pick a random number between 0-3 to represent a direction vector */
        int randomInt = random.nextInt(4);
        /** Randomly pick a direction (up : down : left : right) */
        switch (randomInt) {
            case 0:
                vector2 = Vector2.right;
                break;
            case 1:
                vector2 = Vector2.left;
                break;
            case 2:
                vector2 = Vector2.up;
                break;
            case 3:
                vector2 = Vector2.down;
                break;
        }
        return vector2;
    }

    public boolean getAgressive() {
        return this.isAgressive;
    }
    public boolean bossStatus() {
        return this.isBoss;
    }
    public Projectile getProjectile() {
        return this.projectile;
    }
    public int getAttackRange() {return this.ATTACK_RANGE;}
    public void setMovementSpeed(double movementSpeed) {this.movementSpeed = movementSpeed;}
    public double getMovementSpeed() {return this.movementSpeed;}

    public double getBaseSpeed() {return this.BASE_SPEED;}

    public Point getCenter() {return new Point(getEntityPos().x + getEntitySprite().getWidth() / 2,
            getEntityPos().y + getEntitySprite().getHeight() / 2);}
}

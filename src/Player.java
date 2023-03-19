import bagel.*;
import bagel.util.Rectangle;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.Vector;

public class Player extends Entity implements Cooldown, Sprites {

    /*********** Attributes ***************/

    /** Players Current Health */
    private int health;

    /** Players Rectangle */
    private Rectangle playerBoxCollider2D;

    /** Players Old Positon */
    private Point oldPos;

    /** Players Speed */
    private final static double PLAYER_SPEED = 2;

    /** Is the player attacking */
    private boolean attackState;

    /** Is the player invincible */
    private boolean invincibleState;

    /** Time since the player last attacked */
    private static int timeSinceLastAttack;

    /** Frame Counter for the Duration of Players Invis */
    private static int invincibleFrameCounter;

    /** The direction the player is facing */
    private static Vector2 directionVector;

    /** How long the player has been attacking for */
    private static int attackDuration = 0;

    /** Has the player had it's first attack */
    private boolean firstAttack = false;

    /** Has the players attack finished */
    private static boolean hasAttacked = false;

    private final static int REFRESH_RATE = 60;

    /** TODO: CHANGE BOTH DURATIONS TO REFLECT 60HZ SCREEN AS WAS CODED ON 240HZ.*/
    private final static int ATTACK_COOLDOWN = 2000;
    private final static int ATTACK_DURATION = 1000;

    /** Create a new player
     * @param point - The players starting
     * position
     */
    public Player(Point point) {
        super(point, new Image(FAE_RIGHT));
        this.health = 100;
        playerBoxCollider2D = getEntitySprite().getBoundingBox();
        this.oldPos = new Point(0, 0);
        this.attackState = false;
        this.invincibleState = false;
        timeSinceLastAttack = 0;
        invincibleFrameCounter = 0;
        this.directionVector = Vector2.right;
    }
    /**
     * @param  health new Health
     */
    public void setHealth(int health) {this.health = health;}

    /** Sets to Fae Right Sprite */
    public void setRightSprite() {
        if(attackState) {setEntitySprite(new Image(FAE_ATTACK_RIGHT));}
        else {setEntitySprite(new Image(FAE_RIGHT)); }
    }

    /** Set to Fae Left Sprite */
    public void setLeftSprite() {
        if(attackState) {setEntitySprite(new Image(FAE_ATTACK_LEFT));}
        else {setEntitySprite(new Image(FAE_LEFT));}
    }

    /** @return current health */
    public int getHealth() {return this.health;}

    /** @return current speed */
    public double getSpeed() {return PLAYER_SPEED;}

    /** Returns true if player collides with
     * a rectangle
     * @param object - A rectangle that the player may collide with
     * @return True if player collides with an object
     */
    public boolean onCollision(Rectangle object) {
        return this.playerBoxCollider2D.intersects(object);
    }

    /** Move the player to a new location
     * @param newPos - New Point Location
     */
    public void movePlayer(Point newPos) {
        setEntityPos(newPos);
        this.playerBoxCollider2D.moveTo(getEntityPos());
    }

    /** Render The Player */
    public void drawPlayer() {
        getEntitySprite().drawFromTopLeft(getX(), getY());
    }

    /** Set players old point */
    public void setOldPoint() {
        oldPos = getEntityPos();
    }

    /** @return Players old positon
     */
    public Point getOldPos() { return this.oldPos; }


    /** Attack Method for the player
     * if the player can attack, then set it is sprite according
     * to the direction it is currently facing
     * @return Returns true if the player attacks
     */
    public boolean attack() {
        if (canAttack()) {
            this.attackState = true;
            if(this.directionVector.equals(Vector2.right)) {
                this.setEntitySprite(new Image(FAE_ATTACK_RIGHT));
            }
            else if(this.directionVector.equals(Vector2.left)) {
                this.setEntitySprite(new Image(FAE_ATTACK_LEFT));
            }
            return true;
        }
        return false;
    }

    /** Check if the player can attack. That is,
    * check if the cooldown before they can attack is over
    * and that they are not in the attack state already
    * @return Returns true if the player acn attack
    */
    public boolean canAttack() {
        // if players first attack
        if(!firstAttack) {
            firstAttack = true;
            this.setTimeSinceLastAttack(0);
            return true;
        }
        // if not already attacking and cooldown is over we can attack
        if (!attackState && ((timeSinceLastAttack * 1000) / REFRESH_RATE) > ATTACK_COOLDOWN) {
            this.setTimeSinceLastAttack(0);
            return true;
        }
        else if(attackState) {
            return false;
        }
        return false;
    }

    /** Count the number of frames since last attack*/
    public void frameCounters() {
        if (hasAttacked) {
            timeSinceLastAttack++;
        }
    }

    /**
     * @return players current attack stage
    */
    public boolean getAttackState() {
        return this.attackState;
    }

    /** Set players attack state
     * @param state - new state
     * */
    public void setAttackState(boolean state) {
        this.attackState = false;

    }

    /** @param directionVector - players new direction */
    public void setDirectionVector(Vector2 directionVector) {this.directionVector = directionVector;}

    /** @return players current direction
     */
    public Vector2 getDirectionVector() {return this.directionVector;}

    public void setHasAttacked(boolean hasAttacked1) {hasAttacked = hasAttacked1;}

    public int getTimeSinceLastAttack() {return timeSinceLastAttack;}

    public boolean getHasAttacked() {return this.hasAttacked;}

    public void setTimeSinceLastAttack(int time) {timeSinceLastAttack= time;}

    public Rectangle getBoxCollider() {return this.playerBoxCollider2D;}

    public boolean getInvState() {return this.invincibleState;}

    public void setInvincibleState(boolean state) {this.invincibleState = state;}

    /** Method to implement invicinbility cooldown
    * for demon. If they are in the Invisiblity
    * State, then begin counting the frame duration
    */
    @Override
    public void invisCooldown() {
        if(invincibleState) { invincibleFrameCounter++; }
        if(((invincibleFrameCounter * 1000) / REFRESH_RATE == INVINCIBLE_DURATION)) {
            invincibleState = false;
            invincibleFrameCounter = 1;
        }
    }

    /** Method that counts the cooldown of a players
     * attack
     */
    public void attackCooldown() {
        if(getAttackState()) { attackDuration++; }
        /** TODO */
        if ((attackDuration * 1000) / REFRESH_RATE == ATTACK_DURATION) {
            setHasAttacked(true);
            setAttackState(false);
            if (getDirectionVector().equals(Vector2.right)) {
                setEntitySprite(new Image(FAE_RIGHT));
            }
            else if (getDirectionVector().equals(Vector2.left)) {
                setEntitySprite(new Image(FAE_LEFT));
            }
            attackDuration = 0;
        }
        frameCounters();
    }

    /** Return The players current centre
     * @return - The players current centre
     * */
    public Point getCenter() {
        return new Point(getEntityPos().x + getEntitySprite().getWidth() / 2,
                getEntityPos().y + getEntitySprite().getHeight() / 2);
    }

}




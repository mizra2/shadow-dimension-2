import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.*;

public class Collidable extends Entity implements Sprites {

    /*********** Attributes ***************/

    /** Damage amount caused by a Collidable */
    private static final int SINK_HOLE_DMG = 30;

    /** Hitbox / Rectangle for the Collidable */
    private Rectangle collidableBoxCollider2D;

    /** Has the sinkhole been triggered yet */
    private boolean hasTriggered;

    /**  Creates a new Collidable Object
     * @param entityPos - Position of the Sinkhole
     */
    public Collidable(Point entityPos) {
        super(entityPos, new Image(SINKHOLE_SPRITE));

        this.collidableBoxCollider2D = new Rectangle(entityPos.x, entityPos.y,
                getEntitySprite().getWidth() - 10, getEntitySprite().getHeight() - 20);

        this.hasTriggered = false;
    }
    /*********** Methods ***************/
    /** Deals damage to a player
     * @param player - the player getting damaged
     */
    public void dealDmg(Player player) {
        player.setHealth(player.getHealth() - SINK_HOLE_DMG);
        if (player.getHealth() < 0) {player.setHealth(0);}
        this.hasTriggered = true;
    }
    public boolean getTrigger() {
        return this.hasTriggered;
    }

    public Rectangle getBoxCollider2D() { return this.collidableBoxCollider2D; }

}

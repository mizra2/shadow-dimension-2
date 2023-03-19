import bagel.*;
import bagel.util.Rectangle;
import bagel.util.Point;

public class WorldObject extends Entity implements Sprites {

    /*********** Attributes ***************/

    /** Hitbox / Rectangle of the WorldObject  */
    private Rectangle boxCollider2D;

    /** Creates a new World Object depending
     * @param point - Location of the object
     * @param level - The level determines the object sprite
     * @param image - The file pertaining the sprite
     */
    public WorldObject(Point point, int level, Image image) {
        super(point, new Image(WALL_SPRITE));
        if(level == 1) {setEntitySprite(new Image(TREE_SPRITE));}
        boxCollider2D = getEntitySprite().getBoundingBox();
        boxCollider2D.moveTo(getEntityPos());
    }
    public Rectangle getBoxCollider2D() {return this.boxCollider2D;}

}

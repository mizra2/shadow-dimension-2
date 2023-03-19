import bagel.*;
import bagel.util.Rectangle;
import bagel.util.Point;

public abstract class Entity {

    /*********** Attributes ***************/

    /** The entities position (topleft of a sprite) */
    private Point entityPos;

    /** The entities sprite */
    private Image entitySprite;

    /** Create an entity
     * @param entityPos - Starting poisition of an entity
     * @param entitySprite - An entitys image or sprite
     */
    public Entity(Point entityPos, Image entitySprite) {
        this.entityPos = entityPos;
        this.entitySprite = entitySprite;
    }
    public double getX() {
        return entityPos.x;
    }
    public double getY() {
        return entityPos.y;
    }

    public void setEntityPos(Point point) {
        this.entityPos = point;
    }
    public void setEntitySprite(Image image) {
        this.entitySprite = image;
    }
    public Point getEntityPos() {return this.entityPos;}

    public Image getEntitySprite() {return this.entitySprite;}
}

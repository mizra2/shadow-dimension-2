import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.w3c.dom.css.Rect;

public class Projectile {

    /*********** Attributes ***************/

    /** A projectiles sprite / image */
    private Image projectileSprite;

    /** A projectiles rectangle / hitbox */
    private Rectangle projectileCollider2D;

    /** drawoptions for a projectile rotations*/
    private DrawOptions projectileOptions = new DrawOptions();

    /** Create an instance of a projectile
     * @#param image - The fires sprite / image
     */

    public Projectile(Image image) {
        this.projectileSprite = image;
        this.projectileCollider2D = this.projectileSprite.getBoundingBox();
    }
    public void setProjectileOptions(DrawOptions options) {
        this.projectileOptions = options;
    }
    public Image getProjectileSprite() {
        return this.projectileSprite;
    }
    public Rectangle getBoxCollider() {return this.projectileCollider2D;}

    public double getHeight() {return projectileSprite.getHeight();}

    public double getWidth() {return projectileSprite.getWidth();}

}

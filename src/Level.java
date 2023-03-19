import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class Level {

    // Background image of the level
    private final Image BACKGROUND_IMAGE;

    // Top Left Bound of the Level
    private Point TOP_LEFT_BOUND;
    // Bottom Right bound of the Level
    private Point BOTTOM_RIGHT_BOUND;

    // Level State, i.e has the level been completed
    private boolean levelState;

    // Level Start, i.e has the level started yet
    private boolean levelStart;

    public Level(String image) {
        this.BACKGROUND_IMAGE = new Image(image);
        this.levelState = false;
        this.levelStart = false;
    }
    public void setState(boolean state) {this.levelState = state;}

    public void setStart(boolean start) {this.levelStart = start;}

    public boolean getState() {return this.levelState;}

    public boolean getStart() {return this.levelStart;}

    // Draw the background for the level
    public void renderLevel() {
       this.BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }

    public double getLeftBoundX() {return this.TOP_LEFT_BOUND.x;}

    public double getLeftBoundY() {return this.TOP_LEFT_BOUND.y;}

    public double getRightBoundX() {return this.BOTTOM_RIGHT_BOUND.x;}

    public double getRightBoundY() {return this.BOTTOM_RIGHT_BOUND.y;}
    public void setLeftBound(Point leftBound) {this.TOP_LEFT_BOUND = leftBound;}

    public void setRightBound(Point rightBound) {this.BOTTOM_RIGHT_BOUND = rightBound;}

}

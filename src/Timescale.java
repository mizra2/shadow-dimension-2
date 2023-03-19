import bagel.Input;
import bagel.Keys;
import java.sql.Time;
import java.util.List;

public class Timescale {

    /*********** Attributes ***************/

    /** Singleton Class Implementation */

    /** Instance of itself */
    private static Timescale timescale;

    /** Scale factor */
    private static int scaleFactor;

    private Timescale() {
        scaleFactor = 0;
    }

    /** Create an instance of timescale
     * @return A new intance of timescale
     */

    public static Timescale getTimescale() {
        if(timescale == null) {
            timescale = new Timescale();
        }
        return timescale;
    }

    /** Change the scale factor of the timescale
     * @param demons - A list of all demons
     * @param input - A users input
     * */
    protected void changeScale(Input input, List<Demon> demons) {
        if(input.wasPressed(Keys.L)) {
            if(scaleFactor < 3) {
                scaleFactor++;
                System.out.println("Sped up, Speed: " + scaleFactor);
                setSpeed(demons);
            }
        } else if(input.wasPressed(Keys.K)) {
            if(scaleFactor > -3) {
                scaleFactor--;
                System.out.println("Slowed down, Speed:  " + scaleFactor);
                setSpeed(demons);
            }
        }
    }

    /** Scale the speed of demons
     * @param demons - list containing all demons
     * */
    protected void setSpeed(List<Demon> demons) {
        if(scaleFactor >= 0) {
            for (Demon demon : demons) {
                demon.setMovementSpeed(demon.getBaseSpeed() * Math.pow((1.5), scaleFactor));

            }
        }
        if(scaleFactor < 0) {
            for (Demon demon : demons) {
                demon.setMovementSpeed(demon.getBaseSpeed() * Math.pow(2, scaleFactor));

            }
        }
    }
}

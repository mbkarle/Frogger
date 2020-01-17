import java.awt.*;

/**
 * Created by michael_hopps on 2/14/17.
 */
public abstract class Obstacle extends Sprite {


    /*
    You could modify this class to be a parent to each obstacle,
    and have Car and Lawnmower extend it.
    OR you could not do that, and have Car extend Sprite, etc.
    I'm not sure which is "better" here, so do what makes sense to you.
     */

    /*
    I'd recommend that each of your obstacles
    have images the same height as the frog.
    It will make movement easier and hit detection cleaner.
     */


    private int[] startCoords;
    private Point endCoords;
    public Obstacle(int x, int y, String picture, int dir, int speed){
        super(x, y, EAST );
        startCoords = new int[]{(x > 0)?FroggerMain.FRAMEWIDTH + 10:-180,y};
        setPic(picture, dir);
        setDir(dir);
        setSpeed(speed);
        setEndCoords(new Point((startCoords[0] > 0)?-180:FroggerMain.FRAMEWIDTH + 10, y));
        trimSize();
    }

    public void trimSize(){
        if(getPic().getHeight() > 25) {
            setPic(resize(getPic(), getPic().getWidth(), 25));
        }
    }

    @Override
    public void update(){
        super.update();

        if(getLoc().x * Math.cos(Math.toRadians(getDir())) > endCoords.x * Math.cos(Math.toRadians(getDir()))){
            loop();
        }
    }

    public abstract void collide(Frog player);

    public abstract void loop();

    public Point getStartCoords() {
        return new Point(startCoords[0], startCoords[1]);
    }

    public void setStartCoords(int[] startCoords) {
        this.startCoords = startCoords;
    }

    public Point getEndCoords() {
        return endCoords;
    }

    public void setEndCoords(Point endCoords) {
        this.endCoords = endCoords;
    }
}

import java.awt.*;

/**
 * Created by student on 2/4/18.
 */
public class Log extends Obstacle{


    public Log(int y, int dir, int numInRow, int speed){
        super((dir == Sprite.EAST)?-100 * numInRow /*- (int)(Math.random() * 3) * 50*/:FroggerMain.FRAMEWIDTH+100 * numInRow /*+ (int)(Math.random() * 3) * 20*/, y, "logShort.png", dir, speed);
        //setStartCoords(new Point(-50, y));

    }


    @Override
    public void collide(Frog player) {
        player.setSafe(true);
        player.move(getDir(), getSpeed());
    }

    @Override
    public void loop() {
        setLoc(getStartCoords());
    }
}

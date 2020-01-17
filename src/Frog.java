//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by michael_hopps on 2/13/17.
 */
public class Frog extends Sprite {

    private BufferedImage defaultImage;
    private int hopDelay = 0, furthestY;
    private boolean safe, muted;
    private FroggerMain game;
    public Frog(FroggerMain game){
        super(500, FroggerMain.FRAMEHEIGHT - 25, NORTH);
//        move(Sprite.NORTH, getSpeed());
        setPic("frog1.png", NORTH);
        defaultImage = getPic();
        safe = false;
        this.game = game;
        furthestY = getLoc().y;
    }

    @Override
    public void update() {
        super.update();
        safe = false;
        if(getLoc().y + game.getTranslateGoal() < (FroggerMain.FRAMEHEIGHT) * 2 / 3){
            //move(Sprite.SOUTH, getSpeed());
            game.setTranslateGoal(game.getTranslateGoal() + getSpeed());
        }
        if(getLoc().y < furthestY){
            game.setScore(game.getScore() + 1);
            furthestY = getLoc().y;
        }
        //This makes a hopping sound when the frog is told to move.
//        if(!muted) {
//            try {
//                // open the sound file as a Java input stream
//                String hop = "res/hop.wav";
//                InputStream in = new FileInputStream(hop);
//
//                // create an audiostream from the inputstream
//                AudioStream audioStream = new AudioStream(in);
//
//                // play the audio clip with the audioplayer class
//                AudioPlayer.player.start(audioStream);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Error loading sound file.");
//            }
//        }
    }

    @Override
    public void move(int direction, int velocity){
        Rectangle frame = new Rectangle(0, 0, FroggerMain.FRAMEWIDTH, FroggerMain.FRAMEHEIGHT);
        Rectangle relRect = new Rectangle(getLoc().x, getLoc().y + game.getTranslate(), getBoundingRectangle().width, getBoundingRectangle().height);
        if(!frame.intersects(relRect)){
            System.out.println(frame);
            System.out.println(relRect);
            kill();
        }
        super.move(direction, velocity);
    }

    public BufferedImage getDefaultImage(){return defaultImage;}

    public int getHopDelay() {
        return hopDelay;
    }

    public void setHopDelay(int hopDelay) {
        this.hopDelay = hopDelay;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public void kill(){
        game.killPlayer();
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public int getFurthestY() {
        return furthestY;
    }

    public void setFurthestY(int furthestY) {
        this.furthestY = furthestY;
    }
}

import java.awt.*;

/**
 * Created by student on 2/8/18.
 */
public class Snake extends Obstacle {//not really functional, nor a real priority

    private Tile[][] zoneMap;
    public Snake(int x, int y, int speed, Zone zone) {
        super(x, y, "snake.png", Sprite.EAST, speed);
        this.zoneMap = zone.getTileMap();
        setEndCoords(zoneMap[(int)(Math.random() * zoneMap.length)][(int)(Math.random()* zoneMap[0].length)].getLoc());
        setDir((int)(Math.toDegrees(Math.atan2(getEndCoords().y - y, getEndCoords().x - x))));
        System.out.println(getDir());
    }

    @Override
    public void collide(Frog player) {
        player.kill();
    }

    @Override
    public void loop() {
        Point end = getEndCoords();
        while(end.equals( getEndCoords())){
            setEndCoords(zoneMap[(int)(Math.random() * zoneMap.length)][(int)(Math.random()* zoneMap[0].length)].getLoc());
        }
        setDir((int)(Math.toDegrees(Math.atan2(getEndCoords().y - end.y, getEndCoords().x - end.x))));
        System.out.println(getDir());


    }

    @Override
    public void update(){
        super.move(getDir(), getSpeed());
        if(Math.abs(getEndCoords().x - getLoc().x) < 10 && Math.abs(getEndCoords().y - getLoc().y) < 10){
            System.out.println("looping");
            loop();
        }
    }
}

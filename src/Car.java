/**
 * Created by student on 2/4/18.
 */
public class Car extends Obstacle {

    public static final String[] cars = new String[]{"car1.png", "car2.png", "car3.png", "car4.png"};
    public Car(int y, int dir, int numInRow, int speed) {
        super((dir == Sprite.EAST)?-100 * numInRow:FroggerMain.FRAMEWIDTH+100 * numInRow, y, cars[(int)(Math.random() * cars.length)], dir, speed);
    }

    public Car(int x, int y, int dir, int numInRow, int speed){
        super(x, y, cars[(int)(Math.random() * cars.length)], dir, speed);
    }


    @Override
    public void collide(Frog player) {
        player.kill();
    }

    @Override
    public void loop() {
        setLoc(getStartCoords());
    }
}

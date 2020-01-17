import java.awt.*;

/**
 * Created by student on 2/1/18.
 */
public class Tile extends Sprite {

    private int width, height;
    private boolean deadly;
    public static final int DEFAULTWIDTH = 25, DEFAULTHEIGHT = 25;
    public static final Tile GRASS, WATER, ROAD, DESERT;
    static{
        GRASS = new Tile(0,0, DEFAULTWIDTH, DEFAULTHEIGHT, Sprite.NORTH, false);
        GRASS.setPic("grass.png", Sprite.NORTH);

        WATER = new Tile(0, 0, DEFAULTWIDTH, DEFAULTHEIGHT, Sprite.NORTH, true);
        WATER.setPic("water.png", Sprite.NORTH);

        ROAD = new Tile(0,0, DEFAULTWIDTH, DEFAULTHEIGHT, Sprite.NORTH, false);
        ROAD.setPic("road.png", Sprite.NORTH);

        DESERT = new Tile(0, 0, DEFAULTWIDTH, DEFAULTHEIGHT, Sprite.NORTH, false);
        DESERT.setPic("sand.png", Sprite.NORTH);
    }

    public Tile(int x, int y, int width, int height, int direction, boolean deadly) {
        super(x, y, direction);
        this.width = width;
        this.height = height;
        this.deadly = deadly;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return new Rectangle(getLoc().x, getLoc().y, width, height);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDeadly() {
        return deadly;
    }

    public void setDeadly(boolean deadly) {
        this.deadly = deadly;
    }

    public Tile emulate(int x, int y){
        Tile tile = new Tile(x, y, width, height, getDir(), deadly);
        tile.setPic(getPic());

        return tile;
    }
}

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by student on 2/1/18.
 */
public class Zone {
    protected int tileRows, tileCols, yCoord;
    private ArrayList<Obstacle> obstacles;
    protected Tile modelTile;
    protected Tile[][] tileMap;

    public Zone(int yCoord, int numTileRows, Tile modelTile, ArrayList<Obstacle> obstacles){
        this.yCoord = yCoord;
        this.tileRows = numTileRows;
        this.obstacles = obstacles;
        this.modelTile = modelTile;
        tileCols = (int)Math.ceil(FroggerMain.FRAMEWIDTH / modelTile.getWidth());
        tileMap = new Tile[tileRows][tileCols];
        buildTileMap();
        this.obstacles = new ArrayList<>();
        if(obstacles != null){
            this.obstacles.addAll(obstacles);
        }

    }

    public void buildTileMap(){
        for(int i = 0; i < tileMap.length; i++){

            for(int j = 0; j < tileMap[0].length; j++){
                tileMap[i][j] = modelTile.emulate(modelTile.getWidth() * j, yCoord + modelTile.getHeight() * i);
            }

        }
    }

    public void rebuild(int newY){
        int deltaY = newY - yCoord;
        yCoord = newY;
        buildTileMap();
        for(Obstacle o : obstacles){
            o.setLoc(new Point(o.getLoc().x, o.getLoc().y + deltaY));
            o.setStartCoords(new int[]{o.getStartCoords().x, o.getStartCoords().y + deltaY});
//            o.setEndCoords(new Point(o.getEndCoords().x, o.getEndCoords().y + deltaY));
//            o.loop();
        }
    }

    public void draw(Graphics2D g2){
        for(Tile[] tiles : tileMap){
            for(Tile tile : tiles){
                tile.draw(g2, tile.getWidth(), tile.getHeight());
            }
        }
        for(Obstacle obstacle : obstacles){
            obstacle.draw(g2);
        }
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public Tile[][] getTileMap() {
        return tileMap;
    }

    public void setTileMap(Tile[][] tileMap) {
        this.tileMap = tileMap;
    }

    public Rectangle getBoundingRectangle(){
        return new Rectangle(0, yCoord, tileCols * modelTile.getWidth(), tileRows * modelTile.getHeight());
    }

    public boolean intersects(Rectangle other){
        return getBoundingRectangle().intersects(other);
    }

    public ArrayList<Obstacle> getObstacles(){return obstacles;}

    public void addObstacle(Obstacle obstacle){
        obstacles.add(obstacle);
    }
}

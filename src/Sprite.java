//import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This class models a rectangular movable object on the screen.  It is drawn using an image.
 */
public class Sprite {

    private static int nextID = 1; //static instance fields- there is ONE shared variable for all objects of this class.

    private Point loc; //top left corner of this Sprite. Note loc.x and loc.y are the easy way to access the point.
    private int dir, picOrientation; //dir is the current direction in degrees.  See the constants below.
    private BufferedImage pic; //put the file in the res folder.
    private int speed; //Number of pixels moved each frame.
    private int id;
    public static final int NORTH = 90, SOUTH = 270, WEST = 180, EAST = 0, NE = 45, NW = 135, SW = 225, SE = 315;


    public Sprite(int x, int y, int direction) {
        loc = new Point(x, y);
        dir = direction;
        setPic("blank.png", NORTH);
        speed = Tile.DEFAULTHEIGHT;  //moves one height's worth by default.

        id = nextID;
        nextID++;
    }

    /**
     * This draws the image pic at the Point loc, rotated to face dir.
     */
    public void draw(Graphics2D g2) {
        double rotationRequired = Math.toRadians(picOrientation - dir);
        double halfWidth = pic.getWidth() / 2;
        double halfHeight = pic.getHeight() / 2;
        g2.rotate(rotationRequired, loc.x+halfWidth, loc.y+halfHeight);
        g2.drawImage(pic, loc.x, loc.y, null);
        g2.rotate(-rotationRequired, loc.x+halfWidth, loc.y+halfHeight);
//        g2.draw(getBoundingRectangle());
    }

    public void draw(Graphics2D g2, int width, int height){
        double rotationRequired = Math.toRadians(picOrientation - dir);
        double halfWidth = pic.getWidth() / 2;
        double halfHeight = pic.getHeight() / 2;
        g2.rotate(rotationRequired, loc.x+halfWidth, loc.y+halfHeight);
        g2.drawImage(pic, loc.x, loc.y, width, height, null);
        g2.rotate(-rotationRequired, loc.x+halfWidth, loc.y+halfHeight);
    }

    /**
     * Moves the pic in the direction the Sprite is facing (dir).
     */
    public void update() {
        move(dir, speed);
    }

    /**
     * Moves pic regardless of sprite's own speed and direction
     */
    public void move(int direction, int vel){
        int dx = (int) (Math.cos(Math.toRadians(direction)) * vel);
        int dy = -(int) (Math.sin(Math.toRadians(direction)) * vel);
        loc.translate(dx, dy);
    }

    /**
     Returns true if this Sprite intersects the other Sprite
     */
    public boolean intersects(Sprite other){
        return getBoundingRectangle().intersects(other.getBoundingRectangle());
    }


    /**
     * Changes the image file that this Sprite uses to draw.
     *
     * @param fileName    the case-sensitive file name
     * @param orientation the direction that the image file is facing
     */
    public void setPic(String fileName, int orientation) {
        try {
            pic = ImageIO.read(new File("res/" + fileName));
            picOrientation = orientation;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the direction the Sprite is facing by the given angle.
     *
     * @param delta change in angle measured in degrees
     */
    public void rotateBy(int delta) {
        setDir(dir + delta);
    }

    /**
     * Changes the direction the Sprite is facing to the given angle.
     *
     * @param newDir the new direction measured in degrees
     */
    public void setDir(int newDir) {
        dir = newDir;
    }

    /**
     * Returns a Rectangle object that surrounds this Sprite's pic.
     * Useful for hit detection.  Really useful.
     *
     * @return the bounding Rectangle.
     */
    public Rectangle getBoundingRectangle() {
      //  if(facingNorth() || facingSouth())
            return new Rectangle(loc.x, loc.y, pic.getWidth(), pic.getHeight());
//        else
//            return new Rectangle(loc.x, loc.y, pic.getHeight(), pic.getWidth());

    }

    /**
     * Returns the location of this Sprite.
     *
     * @return A point object.  Use p.x and p.y or p.getX() and p.getY()
     */
    public Point getLoc() {
        return loc;
    }

    /**
     * Changes the location of this Sprite.
     *
     * @param loc
     */
    public void setLoc(Point loc) {
        this.loc = loc;
    }

    /**
     * @return the direction the Sprite is facing.  See the constants for reference.
     */
    public int getDir() {
        return dir;
    }

    public BufferedImage getPic() {
        return pic;
    }

    public void setPic(BufferedImage pic) {
        this.pic = pic;
    }

    /**
     * Returns true if this Sprite is facing East, not true EAST, but EAST at all.
     *
     * @return Returns true if this Sprite is facing East, not true EAST, but EAST at all.
     */
    public boolean facingEast() {
        return dir % 360 < 90 || dir % 360 > 270;
    }

    /**
     * @return Returns true if this Sprite is facing NORTH, not true NORTH, but NORTH at all.
     */
    public boolean facingNorth() {
        return dir % 360 > 0 && dir % 360 < 180;
    }

    /**
     * @return Returns true if this Sprite is facing WEST, not true WEST, but WEST at all.
     */
    public boolean facingWest() {
        return dir % 360 > 90 && dir % 360 < 270;
    }

    /**
     * @return Returns true if this Sprite is facing SOUTH, not true SOUTH, but SOUTH at all.
     */
    public boolean facingSouth() {
        return dir % 360 > 180;
    }

    public int getID() {
        return id;
    }

    /**
     * Overrides the equals method.
     *
     * @param o should be a Sprite
     * @return true if the sprites share the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sprite)) //if not a Sprite...false
            return false;
        Sprite other = (Sprite) o;  //cast Object to Sprite variable
        if (other.getID() == getID())    //if ID's match...
            return true;
        return false;
    }

    /**
     Returns the center of this Sprite
     */
    public Point getCenterPoint(){
        return new Point(loc.x + pic.getWidth()/2, loc.y + pic.getHeight()/2);
    }

    /**
     Changes the speed of this Sprite
     */
    public void setSpeed(int newSpeed){
        speed = newSpeed;
    }

    /**
     Returns the current speed of this Sprite
     */
    public int getSpeed() {
        return speed;
    }

    public void flipImageHoriz(){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-getPic().getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        setPic(op.filter(getPic(), null));
    }

    public void setToPoint(Point point){
        loc = point;
    }


    public int getPicOrientation() {
        return picOrientation;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}


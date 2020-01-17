//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * made by Matthew Benjamin Karle circa 2018
 */

public class FroggerMain extends JPanel {

    //instance fields for the general environment
    public static final int FRAMEWIDTH = 1000, FRAMEHEIGHT = 600;
    private Timer timer;
    private boolean[] keys;

    //instance fields for frogger.
    private Frog frog;
    private ArrayList<Sprite> obstacles;
    private ArrayList<Zone> zones;
    private String[] zoneTypes;
    private int translate, translateGoal, riverAlternationIndex, score, highScore, currHigh;
    private JButton muteButton, clearScore;
    private FroggerMain self;

    public FroggerMain(){
//TODO: encapsulate and randomize obstacles and zones
        keys = new boolean[512]; //should be enough to hold any key code.
        zoneTypes = new String[]{"grass", "river", "road",/*"desert"*/};
        frog = new Frog(this);
        init();
        self = this;

        muteButton = new JButton("Mute SFX");
        setLayout(null);
        muteButton.setBounds(FRAMEWIDTH - 100, FRAMEHEIGHT - 50, 80, 40);
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frog.setMuted(!frog.isMuted());
                muteButton.setText((frog.isMuted())?"Unmute SFX":"Mute SFX");
                self.grabFocus();

            }
        });
        add(muteButton);

        clearScore = new JButton("Clear High");
        clearScore.setBounds(muteButton.getX(), muteButton.getY() - 40, 80, 40);
        clearScore.addActionListener(e -> {
            writeHighScore(0);
            self.grabFocus();
        });
        add(clearScore);

//        try {
//            // open the sound file as a Java input stream
//            InputStream in = new FileInputStream("res/Mii-Channel-Theme.wav");
////
////            // create an audiostream from the inputstream
////            AudioStream audioStream = new AudioStream(in);
////            // play the audio clip with the audioplayer class
////            AudioPlayer.player.start(audioStream);
//
//        } catch (Exception i) {
//            i.printStackTrace();
//            System.out.println("Error loading sound file.");
//        }
//        int length = 5 * 1000 * 60 + 14 * 1000;
//        Timer musicLoop = new Timer(length, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    // open the sound file as a Java input stream
//                    InputStream in = new FileInputStream("res/Mii-Channel-Theme.wav");
////
////            // create an audiostream from the inputstream
//                    AudioStream audioStream = new AudioStream(in);
//                    // play the audio clip with the audioplayer class
//                    AudioPlayer.player.start(audioStream);
//
//                } catch (Exception i) {
//                    i.printStackTrace();
//                    System.out.println("Error loading sound file.");
//                }
//            }
//        });
//        musicLoop.start();


        timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean didMove = false;
                //move the frog
                if(keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]){
                    frog.setDir(Sprite.NORTH);
                    frog.update();
                    keys[KeyEvent.VK_W] = false; //probably.  Makes 1 move per button press.
                    keys[KeyEvent.VK_UP] = false;
                    didMove = true;
                }
                if(keys[KeyEvent.VK_A]|| keys[KeyEvent.VK_LEFT]){
                    frog.setDir(Sprite.WEST);
                    frog.update();
                    keys[KeyEvent.VK_A] = false; //probably.  Makes 1 move per button press.
                    keys[KeyEvent.VK_LEFT] = false;
                    didMove = true;
                }
                if(keys[KeyEvent.VK_D]|| keys[KeyEvent.VK_RIGHT]){
                    frog.setDir(Sprite.EAST);
                    frog.update();
                    keys[KeyEvent.VK_D] = false;
                    keys[KeyEvent.VK_RIGHT] = false;
                    didMove = true;
                }
                if(keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]){
                    frog.setDir(Sprite.SOUTH);
                    frog.update();
                    keys[KeyEvent.VK_S] = false;
                    keys[KeyEvent.VK_DOWN] = false;
                    didMove = true;
                }

                if(didMove){
                    frog.setPic("frog2.png", Sprite.NORTH);
                    frog.setHopDelay(frog.getHopDelay() + 1);
                }

                if(frog.getHopDelay() > 0){
                    frog.setHopDelay(frog.getHopDelay() + 1);
                }

                if(translate < translateGoal){
                    translate += (FRAMEHEIGHT - (frog.getLoc().y + translate))/100;
                    if(zones.size() == 0 || zones.get(zones.size()-1).getyCoord() + translate > 0){
                        buildWorld();
                    }
                }


                for(Zone zone : zones){
                    for(Obstacle obstacle : zone.getObstacles()){
                        obstacle.update();
                        if(obstacle.intersects(frog)){
                            obstacle.collide(frog);
                        }
                    }
                    if(zone.intersects(frog.getBoundingRectangle())){

                        for(Tile[] tiles : zone.getTileMap()){
                            for(Tile tile : tiles){
                                if(!frog.isSafe() && tile.isDeadly() && tile.intersects(frog)){
                                    killPlayer();
                                }
                            }
                        }
                    }
                }

                repaint(); //always the last line.  after updating, refresh the graphics.
            }
        });
        timer.start();

        /*
        You probably don't need to modify this keyListener code.
         */
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {/*intentionally left blank*/ }

            //when a key is pressed, its boolean is switch to true.
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                keys[keyEvent.getKeyCode()] = true;
            }

            //when a key is released, its boolean is switched to false.
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                keys[keyEvent.getKeyCode()] = false;
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void init(){
        frog.setLoc(new Point(500, FroggerMain.FRAMEHEIGHT - 25));
        frog.setFurthestY(frog.getLoc().y);
        zones = new ArrayList<>();
        translate = 0;
        translateGoal = 0;
        score = 0;
        buildWorld();
    }

    //Our paint method.
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.translate(0, translate);
        for(Zone zone : zones){
            zone.draw(g2);
        }
        frog.draw(g2);
        if(!frog.getPic().equals(frog.getDefaultImage()) && frog.getHopDelay() > 5){
            frog.setPic(frog.getDefaultImage());
            frog.setHopDelay(1);
        }

        g2.setFont(new Font("TimesRoman", Font.BOLD, 48));
        int titleX = muteButton.getX() - g2.getFontMetrics().stringWidth("FROGGER") - 10;
        g2.drawString("FROGGER", titleX, muteButton.getY());
        Font standardFont = new Font("Trebuchet", Font.BOLD, 24);
        g2.setFont(standardFont);
        g2.drawString("By Matt Karle", titleX + g2.getFontMetrics().stringWidth("By Matt Karle") / 2, muteButton.getY() + 25);
        g2.translate(0, -translate);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext frc = g2.getFontRenderContext();
        String scoreBoard = "" + score + "   Previous Best: " + getHighScore();
        GlyphVector gv = standardFont.createGlyphVector(frc, scoreBoard);
        g2.translate(20, 20);
        g2.setColor(Color.white);
        g2.drawString(scoreBoard, 0, 0);
        g2.setColor(Color.black);
        g2.draw(gv.getOutline());
        g2.translate(-20, -20);

    }

    public void killPlayer(){
        timer.stop();
        if(score > getHighScore()){
            writeHighScore(score);
            System.out.println(getHighScore());
        }
        init();
        timer.start();

    }

    public void buildWorld(){
        if(zones.size() == 0 || zones.get(zones.size()-1).getyCoord() + translate > 0) {
            Zone newZone = buildZone((zones.size()>0)?zoneTypes[(int)(Math.random()*zoneTypes.length)]:"grass", 0);
            int buildFrom = (zones.size() > 0)?zones.get(zones.size()-1).getyCoord():FRAMEHEIGHT;
            newZone.rebuild(buildFrom - newZone.modelTile.getHeight() * newZone.getTileMap().length);

            zones.add(newZone);

            if (zones.size() >= 1 && zones.get(zones.size() - 1).getyCoord() > 0) {
                buildWorld();
            }
        }
        int index = 0;
        while(!zones.get(index).getBoundingRectangle().intersects(new Rectangle(FRAMEWIDTH, FRAMEHEIGHT))){
            zones.remove(index);
            index++;
        }
    }

    public Zone buildZone(String type, int yCoord){ //TODO
        Zone built = new Zone(0, 0, Tile.GRASS, null);
        switch(type){
            case "grass":
                built = new Zone(yCoord, (int)(Math.random() * 5 + 3), Tile.GRASS, null);
                break;
            case "river":
                built = new Zone(yCoord, (int)(Math.random() * 5 + 3), Tile.WATER, null);
                for(Tile[] tiles : built.getTileMap()){
                    int speed = (int)(Math.random() * 7 + 3);
                    for (int i = 0; i < (int)(Math.random() * 3 + 1); i++) {
                        Log log = new Log(tiles[0].getLoc().y, (riverAlternationIndex%2==0)?Sprite.WEST:Sprite.EAST, i, speed);
                        built.addObstacle(log);
                    }
                    riverAlternationIndex++;
                }
                break;
            case "road":
                built = new Zone(yCoord, (int)(Math.random() * 5 + 3), Tile.ROAD, null);

                for(Tile[] tiles : built.getTileMap()){
                    int speed = (int)(Math.random() * 7 + 3);
                    int dir = (Math.random() > .5)?Sprite.WEST:Sprite.EAST;
                    for (int i = 0; i < (int)(Math.random() * 6 + 1); i++) {
                        Car car = new Car(tiles[0].getLoc().y, dir, i, speed);
                        built.addObstacle(car);
                    }

                }
                break;
            case "desert":
                built = new Zone(yCoord, (int)(Math.random() * 5 + 3), Tile.DESERT, null);
                built.addObstacle(new Snake(built.getTileMap()[0][1].getLoc().x, built.getTileMap()[1][0].getLoc().y, 4, built));
                break;


        }

        return built;
    }

    //sets ups the panel and frame.  Probably not much to modify here.
    public static void main(String[] args) {
        JFrame window = new JFrame("Frogger!");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(0, 0, FRAMEWIDTH, FRAMEHEIGHT + 22); //(x, y, w, h) 22 due to title bar.

        FroggerMain panel = new FroggerMain();
        panel.setSize(FRAMEWIDTH, FRAMEHEIGHT);

        panel.setFocusable(true);
        panel.grabFocus();

        window.add(panel);
        window.setVisible(true);
        window.setResizable(false);

    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public void setTranslate(int translation){
        translate = translation;
    }

    public int getTranslate(){
        return translate;
    }

    public void setTranslateGoal(int translateGoal){
        this.translateGoal = translateGoal;
    }

    public int getTranslateGoal() {
        return translateGoal;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    static String readFile(String path, Charset encoding)//use Charset.defaultCharset();
    {
        try{
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        }
        catch(IOException e){
            System.out.println("File not found");
            e.printStackTrace();
        }

        return null;
    }

    public int getHighScore(){
        String fileText = readFile("res/scores.txt", Charset.defaultCharset());
//        System.out.println(fileText);
        try{
            StringBuilder toBuild = new StringBuilder("");
            for(char character : fileText.toCharArray()){
                if(Character.isDigit(character)){
                    toBuild.append(character);
                }
            }
            return Integer.parseInt(toBuild.toString());
        }catch(NumberFormatException e){
            e.printStackTrace();
            return 0;
        }

    }

    public void writeHighScore(int high){
        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("res/scores.txt"));
            fileWriter.write("" + high);
            fileWriter.close();

//            System.out.println(readFile("res/scores.txt", Charset.defaultCharset()));
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
package finalGame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadInFile {
    public static void main(String[] args) {
        Frame f = new Frame();
        f.setup();
        f.draw();
    }
}
class Frame extends JFrame {
    private Image raster;
    private Graphics rasterGraphics;
    static ArrayList<Unit> objects = new ArrayList<>();
    public Frame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);
    }
    public void setup() {
        raster = this.createImage(500, 500);
        rasterGraphics = raster.getGraphics();
    }
    public void draw() {
        DrawBackground(rasterGraphics);
        int iter = 1;
        File file = new File("Map.txt");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String k = sc.nextLine();
                int i;
                for (i = 0; i < k.length(); i++) {
                    if (k.charAt(i) == '1') {
                        new Dirt(i * 25, 25 * iter);
                    }
                }
                for (Unit o : objects)
                    o.draw(rasterGraphics);
                getGraphics().drawImage(raster, 0, 0, 500, 500, null);
                iter++;
            }
            sc.close();
        }catch(FileNotFoundException e){}
    }
    private void DrawBackground(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0, 500, 500);
    }
}
abstract class Unit {
    Vector2D position;
    Unit(int x, int y) {
        position = new Vector2D();
        position.set(x,y);
        Frame.objects.add(this);
    }

    abstract public void draw(Graphics g);

}
//class Dirt extends Unit {
//    Dirt(int x, int y) {
//        super(x,y);
//    }
//    public void draw(Graphics g) {
//        g.setColor(Color.WHITE);
//        g.fillRect((int)position.getX(), (int)position.getY(), 25,25);
//    }
//}






//
//
//class Vector2D { //copied from DrawWithoutPaintMultiballWithCollision.java, original Author: Zaheer Ahmed
//    private float x;
//    private float y;
//
//    public Vector2D() {
//        this.setX(0);
//        this.setY(0);
//    }
//
//    public Vector2D(float x, float y) {
//        this.setX(x);
//        this.setY(y);
//    }
//
//    public void set(float x, float y) {
//        this.setX(x);
//        this.setY(y);
//    }
//
//    public void setX(float x) {
//        this.x = x;
//    }
//
//    public void setY(float y) {
//        this.y = y;
//    }
//
//    public float getX() {
//        return x;
//    }
//
//    public float getY() {
//        return y;
//    }
//
//    //Specialty method used during calculations of ball to ball collisions.
//    public float dot(Vector2D v2) {
//        float result = 0.0f;
//        result = this.getX() * v2.getX() + this.getY() * v2.getY();
//        return result;
//    }
//
//    public float getLength() {
//        return (float) Math.sqrt(getX() * getX() + getY() * getY());
//    }
//
//    public Vector2D add(Vector2D v2) {
//        Vector2D result = new Vector2D();
//        result.setX(getX() + v2.getX());
//        result.setY(getY() + v2.getY());
//        return result;
//    }
//
//    public Vector2D subtract(Vector2D v2) {
//        Vector2D result = new Vector2D();
//        result.setX(this.getX() - v2.getX());
//        result.setY(this.getY() - v2.getY());
//        return result;
//    }
//
//    public Vector2D multiply(float scaleFactor) {
//        Vector2D result = new Vector2D();
//        result.setX(this.getX() * scaleFactor);
//        result.setY(this.getY() * scaleFactor);
//        return result;
//    }
//
//    //Specialty method used during calculations of ball to ball collisions.
//    public Vector2D normalize() {
//        float length = getLength();
//        if (length != 0.0f)
//        {
//            this.setX(this.getX() / length);
//            this.setY(this.getY() / length);
//        }
//        else
//        {
//            this.setX(0.0f);
//            this.setY(0.0f);
//        }
//        return this;
//    }
//    public String toString() {
//        return "("+x+", "+y+")";
//    }
//}
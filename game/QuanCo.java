package game;

import java.awt.Color;

public class QuanCo implements Cloneable{
    private Color color; 

    public QuanCo(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;     }

    public void changeColor(Color newColor) {
        this.color = newColor; // Phương thức để thay đổi màu sắc của quân cờ
    }
}
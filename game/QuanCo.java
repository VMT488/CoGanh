package game;

import java.awt.Color;

public class QuanCo {
    private Color color; // Màu sắc của quân cờ

    public QuanCo(Color color) {
        this.color = color; // Khởi tạo quân cờ với màu sắc
    }

    public Color getColor() {
        return color; // Phương thức để lấy màu sắc của quân cờ
    }

    public void changeColor(Color newColor) {
        this.color = newColor; // Phương thức để thay đổi màu sắc của quân cờ
    }
}
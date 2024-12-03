package game;

import java.awt.Point;

public class Move {
    Point from;
    Point to;

    Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

	public Point getFrom() {
		return from;
	}

	public Point getTo() {
		return to;
	}

}

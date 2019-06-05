package Lesson_1.Marafon;

public class Cross extends Obstacle {
    int length;

    public Cross(int length) {
        this.length = length;
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.run(length);
    }

    @Override
    public String toString() {
        return "забег на " + length + " м";
    }
}
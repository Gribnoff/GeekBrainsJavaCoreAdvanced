package Lesson_1.Marafon;

public class Wall extends Obstacle {
    int height;

    public Wall(int height) {
        this.height = height;
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.jump(height);
    }

    @Override
    public String toString() {
        return "прыжок через стену высотой " + height + " см";
    }
}
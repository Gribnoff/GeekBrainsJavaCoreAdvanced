package Lesson_1.Marafon.course;

import Lesson_1.Marafon.team.Competitor;

public class Wall extends Obstacle {
    private int height;

    Wall(int height) {
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
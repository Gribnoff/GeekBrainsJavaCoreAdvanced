package Lesson_1.Marafon.course;

import Lesson_1.Marafon.team.Competitor;

public class Cross extends Obstacle {
    private int length;

    Cross(int length) {
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
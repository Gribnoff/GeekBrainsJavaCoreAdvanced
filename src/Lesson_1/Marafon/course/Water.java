package Lesson_1.Marafon.course;

import Lesson_1.Marafon.team.Competitor;

public class Water extends Obstacle {
    private int length;

    Water(int length) {
        this.length = length;
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.swim(length);
    }

    @Override
    public String toString() {
        return "заплыв на " + length + " м";
    }
}
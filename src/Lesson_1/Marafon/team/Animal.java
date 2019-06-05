package Lesson_1.Marafon.team;

public class Animal implements Competitor {
    private String type;
    private String name;

    private int maxRunDistance;
    private int maxJumpHeight;
    private int maxSwimDistance;

    private boolean onDistance;

    @Override
    public boolean isOnDistance() {
        return onDistance;
    }

    @Override
    public void setOnDistance() {
        onDistance = true;
    }

    Animal(String type, String name, int maxRunDistance, int maxJumpHeight, int maxSwimDistance) {
        this.type = type;
        this.name = name;
        this.maxRunDistance = maxRunDistance;
        this.maxJumpHeight = maxJumpHeight;
        this.maxSwimDistance = maxSwimDistance;
        this.onDistance = true;
    }

    @Override
    public void run(int dist) {
        if (dist <= maxRunDistance) {
            System.out.println(this.toString() + " хорошо справился с кроссом");
        } else {
            System.out.println(this.toString() + " не справился с кроссом");
            onDistance = false;
        }
    }

    @Override
    public void jump(int height) {
        if (height <= maxJumpHeight) {
            System.out.println(this.toString() + " удачно перепрыгнул через стену");
        } else {
            System.out.println(this.toString() + " не смог перепрыгнуть стену");
            onDistance = false;
        }
    }

    @Override
    public void swim(int dist) {
        if (maxSwimDistance == 0) {
            System.out.println(this.toString() + " не умеет плавать");
            onDistance = false;
            return;
        }
        if (dist <= maxSwimDistance) {
            System.out.println(this.toString() + " отлично проплыл");
        } else {
            System.out.println(this.toString() + " не смог проплыть");
            onDistance = false;
        }
    }

    @Override
    public void info() {
        System.out.println(this.toString() + " - " + onDistance);
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
package Lesson_1.Marafon.team;

public class Human implements Competitor {
    static int count = 0;
    static String[] names = {"Абрам", "Аваз", "Августин", "Авраам", "Агап", "Агапит", "Агафон", "Адам", "Адриан", "Азамат"};
    private String name;

    private int maxRunDistance;
    private int maxJumpHeight;
    private int maxSwimDistance;

    private boolean active;

    @Override
    public boolean isOnDistance() {
        return active;
    }

    @Override
    public void setOnDistance() {
        active = true;
    }

    Human(String name) {
        this.name = name;
        this.maxRunDistance = 5000;
        this.maxJumpHeight = 30;
        this.maxSwimDistance = 200;
        this.active = true;
        count++;
    }

    @Override
    public void run(int dist) {
        if (dist <= maxRunDistance) {
            System.out.println(name + " хорошо справился с кроссом");
        } else {
            System.out.println(name + " не справился с кроссом");
            active = false;
        }
    }

    @Override
    public void jump(int height) {
        if (height <= maxJumpHeight) {
            System.out.println(name + " удачно перепрыгнул через стену");
        } else {
            System.out.println(name + " не смог перепрыгнуть стену");
            active = false;
        }
    }

    @Override
    public void swim(int dist) {
        if (dist <= maxSwimDistance) {
            System.out.println(name + " отлично проплыл");
        } else {
            System.out.println(name + " не смог проплыть");
            active = false;
        }
    }

    @Override
    public void info() {
        System.out.println(name + " - " + active);
    }

    @Override
    public String toString() {
        return name;
    }
}
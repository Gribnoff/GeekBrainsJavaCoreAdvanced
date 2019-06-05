package Lesson_1.Marafon.team;

public interface Competitor {
    void run(int dist);
    void swim(int dist);
    void jump(int height);
    boolean isOnDistance();
    void setOnDistance();
    void info();
}
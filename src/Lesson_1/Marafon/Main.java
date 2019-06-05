package Lesson_1.Marafon;

public class Main {
    public static void main(String[] args) {
        Team teamA = new Team("Команда А", 5);
        Course adrenalin = new Course("Adrenalin Games", 5);

        teamA.startCourse(adrenalin);
        teamA.showResults(adrenalin);
        teamA.showWhoPassed(adrenalin);
    }
}
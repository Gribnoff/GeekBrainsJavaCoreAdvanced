package Lesson_1.Marafon.team;

import Lesson_1.Marafon.course.Course;

import java.util.HashMap;
import java.util.Map;

public class Team {
    private String name;
    public Competitor[] members;
    public Map<Course, boolean[][]> resultTable = new HashMap<>();

    public Team(String name, Competitor... members) {
        this.name = name;
        this.members = members;
    }

    public Team(String name, int number) {
        this.name = name;
        members = new Competitor[number];
        for (int i = 0; i < number; i++) {
            switch (i % 3) {
                case 0: {
                    members[i] = new Dog(Dog.names[Dog.count % Dog.names.length]);
                    break;
                }
                case 1: {
                    members[i] = new Human(Human.names[Human.count % Human.names.length]);
                    break;
                }
                case 2: {
                    members[i] = new Cat(Cat.names[Cat.count % Cat.names.length]);
                    break;
                }
            }
        }
    }

    public void startCourse(Course course) {
        course.doIt(this);
    }

    public void showWhoPassed(Course course) {
        System.out.println("\nУчастники из " + name + ", прошедшие полосу " + course.name + " полностью:");
        boolean somebodyPassed = false;
        for (int i = 0; i < members.length; i++) {
            if (resultTable.get(course)[i][course.obstacles.length - 1]) {
                somebodyPassed = true;
                System.out.println(members[i].toString());
            }
        }

        if (!somebodyPassed)
            System.out.println("Оказалось, что из команды " + name + " никто не прошёл полосу эту до конца!");
    }

    public void showResults(Course course) {
        System.out.println("\nРезультаты участников из " + name + " на соревнованиях " + course.name + "\n");
        for (int i = 0; i < members.length; i++) {
            StringBuilder result = new StringBuilder();

            if (resultTable.get(course)[i][course.obstacles.length - 1]){
                result.append(" справился со всеми испытаниями!");
            } else {
                if (!resultTable.get(course)[i][0])
                    result.append(" не завершил ни одного испытания!");
                else {
                    result.append(" справился со следующими испытаниями: ");
                    for (int j = 0; j < course.obstacles.length; j++) {
                        if (!resultTable.get(course)[i][j])
                            break;
                        else
                            result.append(course.obstacles[j].toString() + ", ");
                    }
                    result.append("а потом сошёл с дистанции");
                }
            }
            System.out.println(members[i].toString() + result);
        }
    }
}

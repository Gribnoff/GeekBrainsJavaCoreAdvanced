package Lesson_1.Marafon;

import java.util.HashMap;
import java.util.Map;

public class Team {
    String name;
    Competitor[] members;
    Map<Course, boolean[][]> resultTable = new HashMap<>();

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

    void startCourse(Course course) {
        course.doIt(this);
    }

    void showWhoPassed(Course course) {
        System.out.println("\nУчастники, прошедшие полосу " + course.name + " полностью:");
        boolean somebodyPassed = false;
        for (int i = 0; i < members.length; i++) {
            if (resultTable.get(course)[i][course.obstacles.length - 1]) {
                somebodyPassed = true;
                System.out.println(members[i].toString());
            }
        }

        if (!somebodyPassed)
            System.out.println("Оказалось, что никто не прошёл полосу эту до конца!");
    }

    void showResults(Course course) {
        System.out.println("\nИтоги проведения соревнований " + course.name + "\n");
        for (int i = 0; i < members.length; i++) {
            String result = "";

            if (resultTable.get(course)[i][course.obstacles.length - 1]){
                result += " справился со всеми испытаниями!";
            } else {
                if (!resultTable.get(course)[i][0])
                    result += " не завершил ни одного испытания!";
                else {
                    result += " справился со следующими испытаниями: ";
                    for (int j = 0; j < course.obstacles.length; j++) {
                        if (!resultTable.get(course)[i][j])
                            break;
                        else
                            result += course.obstacles[j].toString() + ", ";
                    }
                    result += "а потом сошёл с дистанции";
                }
            }
            System.out.println(members[i].toString() + result);
        }
    }
}

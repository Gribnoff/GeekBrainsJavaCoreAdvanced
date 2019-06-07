package Lesson_1.Marafon.course;

import Lesson_1.Marafon.team.Team;

public class Course {
    public String name;
    public Obstacle[] obstacles;

    public Course(String name, Obstacle... obstacles) {
        this.name = name;
        this.obstacles = obstacles;
    }

    public Course(String name, int number) {
        this.name = name;
        obstacles = new Obstacle[number];
        for (int i = 0; i < number; i++) {
            switch (i % 3) {
                case 0: {
                    obstacles[i] = new Cross(50 + (int)(Math.random() * 500));
                    break;
                }
                case 1: {
                    obstacles[i] = new Wall(2 + (int)(Math.random() * 5));
                    break;
                }
                case 2: {
                    obstacles[i] = new Water(10 + (int)(Math.random() * 20));
                    break;
                }
            }
        }
    }

    public void doIt(Team team) {
        System.out.println("\nСоревнования " + name + " начались!\n");
        boolean[][] resultTeamTable = new boolean[team.members.length][obstacles.length];

        for (int i = 0; i < team.members.length; i++) {

            team.members[i].setOnDistance();

            for (int j = 0; j < obstacles.length; j++) {
                resultTeamTable[i][j] = false;
                obstacles[j].doIt(team.members[i]);
                if (!team.members[i].isOnDistance())
                    break;
                else
                    resultTeamTable[i][j] = true;
            }
        }

        team.resultTable.put(this, resultTeamTable);
    }
}
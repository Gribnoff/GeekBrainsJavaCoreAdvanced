package Lesson_2.DayOfWeek;

class DayOfWeekMain {

    public static void main(final String[] args) {
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(getWorkingHours(day));
        }
    }

    private static String getWorkingHours(DayOfWeek day) {
        if (day.ordinal() == 5 || day.ordinal() == 6)
            return day.rus + " - выходной!";
        else
            return "Оставшиеся рабочие часы до конца недели для дня \"" + day.rus + "\" - " + (5 - day.ordinal()) * 8;
    }
}

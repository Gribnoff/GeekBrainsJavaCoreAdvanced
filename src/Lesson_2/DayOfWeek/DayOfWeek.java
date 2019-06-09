package Lesson_2.DayOfWeek;

enum DayOfWeek {
    MONDAY("Понедельник", 40),
    TUESDAY("Вторник", 32),
    WEDNESDAY("Среда", 24),
    THURSDAY("Четверг", 16),
    FRIDAY("Пятница", 8),
    SATURDAY("Суббота", 0),
    SUNDAY("Воскресенье", 0);

    String rus;
    int restWorkingHours;

    DayOfWeek(String rus, int restWorkingHours) {
        this.rus = rus;
        this.restWorkingHours = restWorkingHours;
    }
}

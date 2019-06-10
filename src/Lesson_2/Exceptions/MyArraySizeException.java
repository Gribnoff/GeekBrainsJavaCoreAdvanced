package Lesson_2.Exceptions;

class MyArraySizeException extends Exception {

    MyArraySizeException() {
        super("Нарушение размера массива");
    }
}

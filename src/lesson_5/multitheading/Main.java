package lesson_5.multitheading;

public class Main {
    static final int size = 10000000;
//    static final int size = 3;
    static final int numberOfThreads = 6;


    public static void main(String[] args) {
//        getTimeForGeneralCount();
        getTimeForMultithreadedCount();
    }

    private static void getTimeForGeneralCount() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1f;
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.out.printf("Время для обычного пересчёта - %dмс\n\n", System.currentTimeMillis() - startTime);
    }

    private static void getTimeForMultithreadedCount() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1f;
        }

        float[] resultArr = new float[size];

        long startTime = System.currentTimeMillis();

        float[][] splitArr = new float[numberOfThreads][];
        for (int i = 0; i < numberOfThreads; i++) {
            int startPos = (int)(i * (size * 1f / numberOfThreads));
            int endPos;
//            if (i != numberOfThreads)
                endPos = (int)((i + 1) * (size * 1f / numberOfThreads) - 1);
//            else
//                endPos = size - 1;
            splitArr[i] = new float[endPos - startPos + 1];
            System.arraycopy(arr, startPos, splitArr[i], 0, endPos - startPos + 1);
            System.out.println(splitArr[i].length);
        }
        System.out.printf("Общее время пересчёта(количество потоков: %d) - %d\n", numberOfThreads, System.currentTimeMillis() - startTime);
    }
}

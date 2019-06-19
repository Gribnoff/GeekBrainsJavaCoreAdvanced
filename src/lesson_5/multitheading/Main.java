package lesson_5.multitheading;

public class Main {
    private static final int size = 10000000;
    private static final int numberOfThreads = 2;


    public static void main(String[] args) {
        getTimeForGeneralCount();
        getTimeForMultithreadedCount();
    }

    private static void getTimeForGeneralCount() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1f;
        }

        long startTime = System.currentTimeMillis();

        new Calculation(arr).run();
        System.out.printf("Время для обычного пересчёта - %dмс\n\n", System.currentTimeMillis() - startTime);
    }

    private static void getTimeForMultithreadedCount() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1f;
        }

        long startTime = System.currentTimeMillis();

        //разбиваем на несколько массивов
        float[][] splitArr = new float[numberOfThreads][];
        for (int i = 0; i < numberOfThreads; i++) {
            int startPos = (int)(i * (size * 1f / numberOfThreads));
            int endPos;
            endPos = (int)((i + 1) * (size * 1f / numberOfThreads) - 1);
            splitArr[i] = new float[endPos - startPos + 1];
            System.arraycopy(arr, startPos, splitArr[i], 0, endPos - startPos + 1);
        }
        long arraySplitTime = System.currentTimeMillis();

        //пересчитываем в несколько потоков
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(new Calculation(splitArr[i]));
            threads[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long arrayCalcTime = System.currentTimeMillis();

        //склеиваем в один массив
        int startPos = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            System.arraycopy(splitArr[i], 0, arr, startPos, splitArr[i].length);
            startPos += splitArr[i].length;
        }
        long arrayCollectTime = System.currentTimeMillis();


        System.out.printf("Общее время пересчёта(количество потоков: %d) - %dмс\n", numberOfThreads, System.currentTimeMillis() - startTime);
        System.out.println("из которых ушло на:");
        System.out.printf("разбивку на несколько массивов - %dмс\n", arraySplitTime - startTime);
        System.out.printf("пересчёт - %dмс\n", arrayCalcTime - arraySplitTime);
        System.out.printf("ссклейку в один массив - %dмс\n", arrayCollectTime - arrayCalcTime);
    }
}

class Calculation implements Runnable {
    private float[] arr;

    Calculation(float[] array) {
        arr = array;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    public void run() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }
}

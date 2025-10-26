package aed.collections;

import java.util.Random;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        ensaioGraficoAddAt();
    }

    private static void ensaioGraficoAddAt() {
        Random random = new Random();
        int step = 255000, iterations = 30, initialComplexity = 1850000;

        System.out.println("-------------FintList-------------");

        System.out.println("i\tcomplexity\ttime(ms)");

        Consumer<FintList> fintListTest = (list) -> {
            list.addAt(random.nextInt(list.size()), 1);
        };

        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialFintList, initialComplexity, fintListTest, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }

        initialComplexity = 1850000;
        System.out.println("------------LinkedList------------");

        Consumer<LinkedList<Integer>> linkedListTest = (list) -> {
            list.addAt(random.nextInt(list.size()), 1);
        };

        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialLinkedList, initialComplexity, linkedListTest, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }
    }

    private static void ensaioRazaoDobradaAddAt() {
        Random random = new Random();

        Consumer<FintList> fintListTest = (list) -> {
            list.addAt(random.nextInt(list.size()), 1);
        };

        Consumer<LinkedList<Integer>> linkedListTest = (list) -> {
            list.addAt(random.nextInt(list.size()), 1);
        };

        System.out.println("-----------------FintList-----------------");
        TemporalAnalysisUtils.runDoublingRatioTest(Main::createSequentialFintList, fintListTest, 10);
        System.out.println("----------------LinkedList----------------");
        TemporalAnalysisUtils.runDoublingRatioTest(Main::createSequentialLinkedList, linkedListTest, 10);
    }

    private static FintList createSequentialFintList(int n) {
        FintList list = new FintList();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }

    private static LinkedList<Integer> createSequentialLinkedList(int n) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++)
            list.add(i);
        return list;
    }

    private static void ensaioRazaoDobradaRemoveAt() {
        Random random = new Random();

        Consumer<FintList> fintListTest = (list) -> {
            int n = list.size();
            list.removeAt(random.nextInt(n));
        };
        Consumer<LinkedList<Integer>> linkedListTest = (list) -> {
            int n = list.size();
            list.removeAt(random.nextInt(n));
        };

        System.out.println("-----------------FintList-----------------");
        TemporalAnalysisUtils.runDoublingRatioTest(Main::createSequentialFintList, fintListTest, 10);
        System.out.println("----------------LinkedList----------------");
        TemporalAnalysisUtils.runDoublingRatioTest(Main::createSequentialLinkedList, linkedListTest, 10);
    }

    private static void ensaioGraficoRemoveAt() {
        int step = 255000, iterations = 30, initialComplexity = 1850000;
        Random random = new Random();

        System.out.println("-------------FintList-------------");

        System.out.println("i\tcomplexity\ttime(ms)");

        Consumer<FintList> testFintList = (list) -> {
            int n = list.size();
            list.removeAt(random.nextInt(n));
        };
        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialFintList, initialComplexity, testFintList, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }
        initialComplexity = 1850000;
        System.out.println("------------LinkedList------------");

        Consumer<LinkedList<Integer>> testLinkedList = (list) -> {
            int n = list.size();
            list.removeAt(random.nextInt(n));
        };
        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialLinkedList, initialComplexity, testLinkedList, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }
    }

    private static void ensaioRazaoDobradaDeepCopy() {
        System.out.println("-----------------FintList-----------------");
        TemporalAnalysisUtils.runDoublingRatioTest(Main::createSequentialFintList, FintList::deepCopy, 10);
        System.out.println("----------------LinkedList----------------");
        TemporalAnalysisUtils.runDoublingRatioTest(Main::createSequentialLinkedList, LinkedList::shallowCopy, 10);
    }

    private static void ensaioGraficoDeepCopy() {
        int step = 790, iterations = 30, initialComplexity = 6000;

        System.out.println("-------------FintList-------------");

        System.out.println("i\tcomplexity\ttime(ms)");

        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialFintList, initialComplexity, FintList::deepCopy, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }
        initialComplexity = 5500;
        System.out.println("------------LinkedList------------");

        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialLinkedList, initialComplexity, LinkedList::shallowCopy, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }
    }
}

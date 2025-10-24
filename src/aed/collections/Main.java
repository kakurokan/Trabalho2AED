package aed.collections;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        ensaioRazaoDobradaRemoveAt();
    }

    private static void ensaioGraficoAddAt() {
        Random random = new Random();
        int step = 500, iterations = 30, initialComplexity = 10000;

        System.out.println("-------------FintList-------------");

        System.out.println("i\tcomplexity\ttime(ms)");

        Consumer<FintList> testFintList = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
        };

        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialFintList, initialComplexity, testFintList, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }

        initialComplexity = 10000;
        System.out.println("------------LinkedList------------");

        Consumer<LinkedList<Integer>> testLinkedList = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
        };

        for (int i = 0; i < iterations; i++) {
            initialComplexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(Main::createSequentialLinkedList, initialComplexity, testLinkedList, iterations);
            System.out.println(i + 1 + "\t" + initialComplexity + "\t" + time / 1E6);
        }
    }

    private static void ensaioRazaoDobradaAddAt() {
        Random random = new Random();

        Consumer<FintList> fintListTest = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
        };

        Consumer<LinkedList<Integer>> linkedListTest = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
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
        int step = 500, iterations = 30, initialComplexity = 10000;
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
        initialComplexity = 10000;
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
        Function<Integer, FintList> listGenerator = (n) ->
        {
            FintList list = new FintList();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<FintList> test = (list) -> {
            FintList para_copiar = new FintList();
            para_copiar = list.deepCopy();

        };
        Function<Integer, LinkedList<Integer>> linkedGenerator = (n) ->
        {
            LinkedList<Integer> list = new LinkedList<Integer>();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<LinkedList<Integer>> test_linked = (list) -> {
            LinkedList copia = new LinkedList();
            copia = list.shallowCopy();
        };
    }
}

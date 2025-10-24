package aed.collections;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class Main {

    private static void ensaioGraficoAddAtAleatorio() {
        Random random = new Random();
        int step = 50000, iterations = 50, complexity = 1000000, trials = 20;
        System.out.println("i\tcomplexity\ttime(ms)");
        Function<Integer, FintList> listGenerator = (n) ->
        {
            FintList list = new FintList();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<FintList> test = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
        };
        for (int i = 0; i < iterations; i++) {
            complexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(listGenerator, complexity, test, trials);
            System.out.println(i + 1 + "\t" + complexity + "\t" + time / 1E6);
        }

        Function<Integer, LinkedList<Integer>> linkedListGenerator = (n) ->
        {
            LinkedList<Integer> list = new LinkedList<>();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<LinkedList<Integer>> testLinkedList = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
        };
        for (int i = 0; i < iterations; i++) {
            complexity += step;
            long time = TemporalAnalysisUtils.getAverageCPUTime(linkedListGenerator, complexity, testLinkedList, trials);
            System.out.println(i + 1 + "\t" + complexity + "\t" + time / 1E6);
        }
    }

    private static void ensaioRazaoDobradaAddAtAleatorio() {
        Random random = new Random();
        Function<Integer, FintList> listGenerator = (n) ->
        {
            FintList list = new FintList();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<FintList> test = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
        };

        Function<Integer, LinkedList<Integer>> linkedgenerator = n -> {
            LinkedList<Integer> list = new LinkedList<>();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<LinkedList<Integer>> test_linked = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.addAt(i, random.nextInt(list.size()));
            }
        };
        TemporalAnalysisUtils.runDoublingRatioTest(linkedgenerator, test_linked, 20);
    }

    private static void ensaioRazaoDobradaRemoveAt() {
        Function<Integer, FintList> listGenerator = (n) ->
        {
            FintList list = new FintList();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<FintList> test = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.removeAt(0);
            }
        };
        Function<Integer, LinkedList<Integer>> linkedGenerator = (n) ->
        {
            LinkedList<Integer> list = new LinkedList<Integer>();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<LinkedList<Integer>> test_linked = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.removeAt(0);
            }
        };

    }

    private static void ensaioRazaoDobradaContains() {
        Function<Integer, FintList> listGenerator = (n) ->
        {
            FintList list = new FintList();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
        Consumer<FintList> test = (list) -> {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                list.contains(i);
            }
        };
        Function<Integer, LinkedList<Integer>> linkedGenerator = (n) ->
        {
            LinkedList<Integer> list = new LinkedList<Integer>();
            for (int i = 0; i < n; i++)
                list.add(i);
            return list;
        };
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

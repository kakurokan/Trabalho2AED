package aed.collections;

import java.util.Iterator;
import java.util.function.Consumer;

public class FintList implements Iterable<Integer> {
    private static final int INITIAL_CAPACITY = 10;

    private IntNode[] elements; //Array de elementos da lista
    private int free_index; //Tamanho da lista de elementos vazios
    private int head; //Primeiro elemento da lista
    private int tail; //Ultimo elemento da lista
    private int capacity; //Tamanho total da lista

    @Override
    public Iterator<Integer> iterator() {
        return new FintListIterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        Iterable.super.forEach(action);
    }

    public class FintListIterator implements Iterator<Integer> {

        int nextNodeIndex;

        public FintListIterator() {
            this.nextNodeIndex = head;
        }

        @Override
        public boolean hasNext() {
            return nextNodeIndex != -1;
        }

        @Override
        public Integer next() {
            IntNode result = elements[nextNodeIndex];
            nextNodeIndex = result.next_index;
            return result.value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn’t support removal");
        }
    }

    private static class IntNode { //Classe
        int value;
        int next_index;
        int prev_index;

        IntNode(int value, int next_index, int prev_index) {
            this.value = value;
            this.next_index = next_index;
            this.prev_index = prev_index;
        }
    }


    public FintList() {
        this.capacity = INITIAL_CAPACITY;
        this.elements = new IntNode[capacity];
        this.free_index = 0;
        this.tail = -1;
        this.head = -1;
    }

    private void resize(int new_capacity) {

    }

    boolean add(int item) {
        if (elements == null)
            return false;
        if (head == -1 && tail == -1) {
            this.head = this.free_index;
            this.tail = this.free_index;

            elements[this.free_index] = new IntNode(item, -1, -1);
            System.out.println("Esta é a " + free_index + " e adicionou " + item);
            free_index++;
        } else {
            IntNode newNode = new IntNode(item, -1, tail);

            int next_free_index = free_index;
            if (elements[free_index] != null)
                next_free_index = elements[free_index].next_index;

            elements[free_index] = newNode;
            elements[tail].next_index = free_index;
            tail = free_index;

            System.out.println("Esta é a " + free_index + " e adicionou " + newNode.value);

            if (next_free_index != free_index)
                free_index = next_free_index;
            else
                free_index++;
        }

        return true;
    }

    int remove() {
        if (head == -1)
            throw new IndexOutOfBoundsException("Lista vazia");
        IntNode node = elements[tail]; //Guarda o no para ‘posteriori’
        if (node.prev_index == -1) {
            //Lógica do free_index para subtituição no add
            elements[tail].next_index = free_index;
            free_index = tail;
            //---

            head = -1;
            tail = -1;
        } else {
            elements[elements[tail].prev_index].next_index = -1; //O no que apontava para a cauda para de apontar para qualquer no

            //Lógica do free_index para subtituição no add
            elements[tail].next_index = free_index;
            free_index = tail;
            //---

            tail = node.prev_index; //Muda a cauda para o no anterior
        }
        return node.value;
    }

    void addAt(int index, int item) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Indice invalido");
        }
        Iterator<Integer> iterator = iterator();
        while (index != 0 && iterator.hasNext()) {
            iterator.next();
            index--;
        }
        int atual = iterator.next();
        int tail_temp = this.tail;
        add(item);
        elements[elements[atual].prev_index].next_index = tail;
        elements[atual].prev_index = tail;
        tail = tail_temp;
    }

    public static void main(String[] args) {
        FintList teste = new FintList();
        teste.add(1);
        teste.add(2);
        teste.add(3);
        teste.add(4);
        teste.add(5);
        teste.add(10);
        System.out.println(teste.remove());
        System.out.println(teste.remove());
        System.out.println(teste.remove());
        teste.add(47);
        teste.add(39);
        System.out.println("Elementos: ");
        for (int v : teste)
            System.out.println(v);
    }
}

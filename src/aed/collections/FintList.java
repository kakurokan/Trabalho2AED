package aed.collections;

import java.util.Iterator;

public class FintList {
    private static final int INITIAL_CAPACITY = 10;

    private intNode[] elements; //Array de elementos da lista
    private int free_index; //Tamanho da lista de elementos vazios
    private int head; //Primeiro elemento da lista
    private int tail; //Ultimo elemento da lista
    private int capacity; //Tamanho total da lista

    private static class intNode { //Classe
        int value;
        int next_index;
        int prev_index;

        intNode(int value, int next_index, int prev_index) {
            this.value = value;
            this.next_index = next_index;
            this.prev_index = prev_index;
        }
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int current = head;

            @Override
            public boolean hasNext() {
                return current != -1;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                int valor = current;
                current = elements[current].next_index;
                return valor;
            }
        };
    }

    public FintList() {
        this.capacity = INITIAL_CAPACITY;
        this.elements = new intNode[capacity];
        this.free_index = 0;
        this.tail = -1;
        this.head = -1;
    }

    private void resize(int new_capacity) {

    }

    boolean add(int item) {
        if (elements == null)
            return false;
        if (tail == -1) {
            this.head = this.free_index;
            this.tail = this.free_index;
            elements[this.free_index] = new intNode(item, -1, -1);
            System.out.println("Esta é a " + free_index + " e adicionou" + item);
            free_index++;
        } else {
            intNode newNode = new intNode(item, -1, tail);

            int next_free_index = -1;
            if (elements[tail].next_index != -1)
                next_free_index = elements[tail].next_index;

            elements[free_index] = newNode;
            elements[tail].next_index = free_index;
            tail = free_index;
            System.out.println("Esta é a " + free_index + " e adicionou" + newNode.value);
            if (next_free_index != -1)
                free_index = next_free_index;
            else
                free_index++;
        }

        return true;
    }

    int remove() {
        if (head == -1)
            throw new IndexOutOfBoundsException("Lista vazia");
        intNode node = elements[tail]; //Guarda o no para ‘posteriori’
        if (this.head == this.tail) {
            this.free_index = 0;
            this.tail = -1;
            this.head = -1;
            return node.value;
        } else {
            elements[elements[tail].prev_index].next_index = -1; //O no que apontava para a cauda para de apontar para qualquer no

            //Lógica do free_index para subtituição no add
            elements[tail].next_index = free_index;
            free_index = tail;
            //---

            tail = node.prev_index; //Muda a cauda para o no anterior

            return node.value;
        }

    }

    void addAt(int index, int item) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Indice invalido");
        }

        Iterator<Integer> iterator = iterator();
        while (index!=0&&iterator.hasNext()) {
            iterator.next();
            index--;
        }
        int atual=iterator.next();
        add(item);

    }

    public static void main(String[] args) {
        FintList teste = new FintList();
        teste.add(1);
        teste.add(2);
        teste.add(3);
        teste.add(4);
        System.out.println(teste.remove());
        System.out.println(teste.remove());
        System.out.println(teste.remove());
        System.out.println(teste.remove());
        teste.add(2);
        System.out.println(teste.remove());
        teste.add(5);
        teste.add(10);

    }
}

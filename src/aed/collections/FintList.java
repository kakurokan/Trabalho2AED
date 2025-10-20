package aed.collections;

import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public static void main(String[] args) {
    FintList teste = new FintList();
    teste.add(1);
    teste.add(2);
    teste.add(3);
    teste.add(4);
    teste.add(5);
    teste.add(6);
    teste.add(7);
    teste.add(8);
    teste.add(9);
    teste.add(10);
    teste.remove();
    teste.remove();
    teste.remove();
    teste.remove();
    teste.remove();
    teste.remove();
    teste.remove();
    teste.remove();

    System.out.println("Capacity: " + teste.capacity);

    for (int v : teste)
        System.out.println(v);

    System.out.println("head: " + teste.elements[teste.head].value);
    System.out.println("tail: " + teste.elements[teste.tail].value);
}

public class FintList implements Iterable<Integer> {
    private static final int INITIAL_CAPACITY = 10;

    private IntNode[] elements; //Array de elementos da lista
    private int free_index; //Tamanho da lista de elementos vazios
    private int head; //Primeiro elemento da lista
    private int tail; //Ultimo elemento da lista
    private int capacity; //Tamanho total da lista
    private int removedNodes; //Quantidade de "lixo" na lista

    public FintList() {
        this.capacity = INITIAL_CAPACITY;
        this.elements = new IntNode[capacity];
        this.free_index = 0;
        this.tail = -1;
        this.head = -1;
        this.removedNodes = 0;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new FintListIterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        if (!isEmpty()) {
            int atual = head;

            while (atual != -1) {
                IntNode atualNode = elements[atual];
                action.accept(atualNode.value);
                atual = atualNode.next_index;
            }
        }
    }

    private void resize(int new_capacity) {
        IntNode[] new_array = new IntNode[new_capacity]; //Novo array para o elements

        if (new_capacity < capacity) { //Caso estejamos diminuindo o array
            int atual = -1;

            if (!isEmpty()) {
                atual = elements[head].next_index;
                int next_index = (atual != -1) ? 1 : -1;
                new_array[0] = new IntNode(elements[head].value, next_index, -1);
                head = 0;
            }

            int i = 1;
            while (atual != -1) {
                int next_index = elements[atual].next_index != -1 ? i + 1 : -1;
                IntNode node = new IntNode(elements[atual].value, next_index, i - 1);
                new_array[i] = node;
                atual = elements[atual].next_index;
                i++;
            }

            tail = isEmpty() ? -1 : i - 1;
            free_index = tail + 1;
            elements = new_array;
            capacity = new_capacity;
            removedNodes = 0;
        } else { //Caso estejamos aumentando o array
            System.arraycopy(elements, 0, new_array, 0, capacity);
            elements = new_array;
            capacity = new_capacity;
        }
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

            if (free_index >= capacity)
                resize(capacity << 1); //Dobra o tamanho quase o array não seja o suficiente

            IntNode newNode = new IntNode(item, -1, tail);

            int next_free_index = free_index;
            if (elements[free_index] != null) //Caso exista espaço lixo dentro do array
                next_free_index = elements[free_index].next_index; //Guarda o proximo espaço lixo

            elements[free_index] = newNode;
            elements[tail].next_index = free_index;
            tail = free_index;

            System.out.println("Esta é a " + free_index + " e adicionou " + newNode.value);

            if (next_free_index != free_index)
                free_index = next_free_index;
            else
                free_index++;
        }

        System.out.println("Free index: " + free_index);
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

        removedNodes++;

        if (removedNodes > (capacity * 0.75)) { //Caso mais de 3/4 do array for lixo
            resize(Math.max(capacity >> 1, INITIAL_CAPACITY)); //Diminui para metade da capacidade
        }

        return node.value;
    }

    void addAt(int index, int item) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Indice invalido");
        }
        int atual = head;
        for (int i = 1; i < index; i++) { // percorre a lista até elemento anterior do index desejado
            atual = elements[atual].next_index;
            if (atual == -1)
                throw new IndexOutOfBoundsException("Indice maior que a lista ligada");
        }

        int tail_temp = this.tail;
        tail = atual;
        System.out.println("atual: " + atual + " tail: " + tail);
        int next = elements[atual].next_index;

        add(item); //adiciona o elemento a lista

        elements[tail].next_index = next; //Faz o elemento novo apontar para o elemento que estava no index anteriormente

        if (next != -1)
            elements[next].prev_index = tail;
        else {
            elements[tail].prev_index = tail_temp;
        }
        if (index != tail)
            tail = tail_temp;

    }

    void set(int index, int value) {
        int atual = head;
        for (int i = 0; i < index; i++) {
            if (atual == -1) throw new IndexOutOfBoundsException("Index invalid");
            atual = elements[atual].next_index;
        }
        elements[atual].value = value;
    }

    int size() {
        if (head == -1) return 0;
        int atual = head;
        int size = 0;
        while (atual != -1) {
            atual = elements[atual].next_index;
            size++;
        }
        return size;
    }

    boolean isEmpty() {
        return head == -1;
    }

    int get(int index) {
        if (index < 0) { //verifica se o indice é valido
            throw new IndexOutOfBoundsException("Indice invalido");
        }
        if (isEmpty()) { //veirifica se a lista esta vazia
            throw new IndexOutOfBoundsException("Lista vazia");
        }

        int atual = head;
        for (int i = 1; i <= index; i++) { // percorre a lista até elemento anterior do index desejado
            atual = elements[atual].next_index;
            if (atual == -1)
                throw new IndexOutOfBoundsException("Indice maior que a lista ligada");
        }

        return elements[atual].value;
    }

    int getFirst() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }
        return elements[head].value;
    }

    int get() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }
        return elements[tail].value;
    }

    int indexOf(int item) {
        int atual = head;
        while (atual != -1 && elements[atual].value != item) {
            atual = elements[atual].next_index;
        }
        return atual;
    }

    boolean contains(int item) {
        return indexOf(item) != -1;
    }

    void map(UnaryOperator<Integer> op) {
        if (op == null) throw new NullPointerException("Operador invalido");
        if (isEmpty()) throw new IndexOutOfBoundsException("Lista vazia");
        int atual = head;
        while (atual != -1) {
            elements[atual].value = op.apply(elements[atual].value);
            atual = elements[atual].next_index;
        }
    }

    int reduce(BinaryOperator<Integer> op, int dfault) {
        if (isEmpty()) return dfault;
        int atual = head;
        int total = op.apply(elements[atual].value, dfault);
        while (elements[atual].next_index != -1) {
            atual = elements[atual].next_index;
            total = op.apply(total, elements[atual].value);
        }
        return total;
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
}
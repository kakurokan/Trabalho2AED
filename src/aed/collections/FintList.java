package aed.collections;

import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class FintList implements Iterable<Integer> {
    private static final int INITIAL_CAPACITY = 10;

    private IntNode[] elements; //Array de elementos da lista
    private int free_index; //Tamanho da lista de elementos vazios
    private int head; //Primeiro elemento da lista
    private int tail; //Ultimo elemento da lista
    private int capacity; //Tamanho total da lista
    private int removedNodes; //Quantidade de "lixo" na lista
    private int size; //Quantidade de elementos na lista

    public FintList() {
        this.capacity = INITIAL_CAPACITY;
        this.elements = new IntNode[capacity];
        this.free_index = 0;
        this.tail = -1;
        this.head = -1;
        this.removedNodes = 0;
        this.size = 0;
    }

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
        teste.remove();
        teste.remove();
        teste.remove();

        System.out.println("Elementos: ");
        System.out.println("Size: " + teste.size());

        for (int v : teste)
            System.out.print(v);
        System.out.println();

        FintList teste1 = teste.deepCopy();
        for (int v : teste1) {
            System.out.print(v);
        }
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

    public boolean add(int item) {
        if (elements == null)
            return false;
        if (head == -1 && tail == -1) {
            this.head = this.free_index;
            this.tail = this.free_index;

            elements[this.free_index] = new IntNode(item, -1, -1);
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

            if (next_free_index != free_index)
                free_index = next_free_index;
            else
                free_index++;
        }

        size++;
        return true;
    }

    private void trashCollector() {
        if (removedNodes > (capacity * 0.75)) { //Caso mais de 3/4 do array for lixo
            resize(Math.max(capacity >> 1, INITIAL_CAPACITY)); //Diminui para metade da capacidade
        }
    }

    public int remove() {
        if (isEmpty())
            throw new IndexOutOfBoundsException("Lista vazia");
        IntNode node = elements[tail]; //Guarda o no para ‘posteriori’

        if (node.prev_index == -1) { //Caso esteja a remover o head
            //Lógica do free_index para substituição no add
            elements[tail].next_index = free_index;
            free_index = tail;
            //---

            head = -1;
            tail = -1;
        } else {
            elements[elements[tail].prev_index].next_index = -1; //O no que apontava para a cauda para de apontar para qualquer no

            //Lógica do free_index para substituição no add
            elements[tail].next_index = free_index;
            free_index = tail;
            //---

            tail = node.prev_index; //Muda a cauda para o no anterior
        }

        removedNodes++;
        size--;

        trashCollector();

        return node.value;
    }

    public int removeAt(int index) {
        int atual = getNodeIndex(index);

        if (atual == tail)
            return remove();

        int result = elements[atual].value;

        elements[elements[atual].next_index].prev_index = elements[atual].prev_index; //O no seguinte passa a apontar para o no anterior do atual
        if (elements[atual].prev_index != -1)
            elements[elements[atual].prev_index].next_index = elements[atual].next_index; //O no anterior passa a apontar para o proximo do atual
        else //Está a tentar retirar a cabeça
            head = elements[atual].next_index;

        //Lógica do free_index para substituição no add
        elements[atual].next_index = free_index;
        free_index = atual;
        //---

        removedNodes++;
        size--;

        trashCollector();

        return result;
    }

    public boolean remove(int item) {
        int index = -1;
        try {
            index = get(item); //Busca onde está este ‘item’ na lista ligada
        } catch (IndexOutOfBoundsException e) {
            if (e.getMessage().equals("Índice maior que a lista ligada")) //Caso não seja possível encontrar estre index
                return false;
            throw new IndexOutOfBoundsException();
        }

        removeAt(index);

        return true;
    }

    public void addAt(int index, int item) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Índice invalido");
        }
        int atual = getNodeIndex(index); //Busca onde está o elemento
        atual = elements[atual].prev_index; //Muda o ponteiro para o valor anterior

        int tail_temp = this.tail;
        tail = atual;
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

    public void set(int index, int value) {
        int atual = head;
        for (int i = 0; i < index; i++) {
            if (atual == -1) throw new IndexOutOfBoundsException("Index invalid");
            atual = elements[atual].next_index;
        }
        elements[atual].value = value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == -1;
    }

    private int getNodeIndex(int index) {
        if (index < 0) { //verifica se o indice é valido
            throw new IndexOutOfBoundsException("Índice invalido");
        }
        if (isEmpty()) { //verifica se a lista esta vazia
            throw new IndexOutOfBoundsException("Lista vazia");
        }

        int atual;

        atual = head;
        for (int i = 1; i <= index; i++) { // percorre a lista até elemento anterior do index desejado
            atual = elements[atual].next_index;
            if (atual == -1)
                throw new IndexOutOfBoundsException("Índice maior que a lista ligada");
        }

        return atual;
    }

    public int get(int index) {
        int atual = getNodeIndex(index);
        return elements[atual].value;
    }

    public int getFirst() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }
        return elements[head].value;
    }

    public int get() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }
        return elements[tail].value;
    }

    public int indexOf(int item) {
        int atual = head;
        while (atual != -1 && elements[atual].value != item) {
            atual = elements[atual].next_index;
        }
        return atual;
    }

    public boolean contains(int item) {
        return indexOf(item) != -1;
    }

    public void map(UnaryOperator<Integer> op) {
        if (op == null) throw new NullPointerException("Operador invalido");
        if (isEmpty()) throw new IndexOutOfBoundsException("Lista vazia");
        int atual = head;
        while (atual != -1) {
            elements[atual].value = op.apply(elements[atual].value);
            atual = elements[atual].next_index;
        }
    }

    public int reduce(BinaryOperator<Integer> op, int dfault) {
        if (isEmpty()) return dfault;
        int atual = head;
        int total = op.apply(elements[atual].value, dfault);
        while (elements[atual].next_index != -1) {
            atual = elements[atual].next_index;
            total = op.apply(total, elements[atual].value);
        }
        return total;
    }

    public void reverse() {
        if (isEmpty()) throw new IndexOutOfBoundsException("Lista vazia");
        if (!(elements[head].next_index == -1)) {
            int atual = tail;
            tail = head; //troca o índice do tail com o do head e vise versa
            head = atual;
            int temp_index;
            elements[atual].next_index = elements[atual].prev_index; //o tail passa a ser o head;
            elements[atual].prev_index = -1;
            atual = elements[atual].next_index; //o next passou a ser o prev, assim o atual iguala o elemento anterior ao tail;
            while (elements[atual].prev_index != -1) {
                temp_index = elements[atual].next_index;
                elements[atual].next_index = elements[atual].prev_index; // o next passa a ser igual ao prev index
                elements[atual].prev_index = temp_index; //o prev passa a ser igual ao next
                atual = elements[atual].next_index;
            }
            elements[atual].next_index = elements[atual].prev_index; //cria o novo head
        }
    }

    public FintList deepCopy() {
        FintList newList = new FintList();

        if (isEmpty()) //Caso esteja vazio, retorna uma lista vazia
            return newList;

        int atual = head;
        while (atual != -1) {
            newList.add(elements[atual].value);
            atual = elements[atual].next_index;
        }

        return newList;
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
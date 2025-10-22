package aed.collections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class FintList implements Iterable<Integer> {
    private static final int INITIAL_CAPACITY = 10;

    private int[] elements; //Array de elementos da lista
    private int[] next_index;
    private int[] prev_index;
    private int free_index; //Tamanho da lista de elementos vazios
    private int head; //Primeiro elemento da lista
    private int tail; //Ultimo elemento da lista
    private int capacity; //Tamanho total da lista
    private int removedNodes; //Quantidade de "lixo" na lista
    private int size; //Quantidade de elementos na lista

    public FintList() {
        this.capacity = INITIAL_CAPACITY;
        this.elements = new int[capacity];
        this.next_index = new int[capacity];
        this.prev_index = new int[capacity];
        this.free_index = 0;
        this.tail = -1;
        this.head = -1;
        this.removedNodes = 0;
        this.size = 0;
    }

    public static void main(String[] args) throws IOException {
        FintList teste = new FintList();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String linha;
        String[] partes;
        FintList teste_deep = new FintList();
        int a, b;
        BinaryOperator<Integer> op_soma = Integer::sum;
        BinaryOperator<Integer> op_mult = (x, y) -> x * y;
        BinaryOperator<Integer> op_sub = (x, y) -> x - y;
        BinaryOperator<Integer> op_div = (x, y) -> x / y;

        System.out.println("Available operations:");
        System.out.println("addAt <index> <value> - Add value at specific index");
        System.out.println("add <value> - Add value at the end");
        System.out.println("get - Get last value");
        System.out.println("get <index> - Get value at index");
        System.out.println("getFirst - Get first value");
        System.out.println("set <index> <value> - Set value at index");
        System.out.println("isEmpty - Check if list is empty");
        System.out.println("remove - Remove last element");
        System.out.println("remove <value> - Remove first occurrence of value");
        System.out.println("removeAt <index> - Remove element at index");
        System.out.println("contains <value> - Check if value exists");
        System.out.println("indexOf <value> - Get index of first occurrence");
        System.out.println("reverse - Reverse the list");
        System.out.println("deepCopy - Create and print copy of list");
        System.out.println("print - Print the list");
        System.out.println("soma - Sum all elements");
        System.out.println("mult - Multiply all elements");
        System.out.println("div - Divide all elements");
        System.out.println("sub - Subtract all elements");
        System.out.println("map - Square all elements in the list\n");

        while ((linha = br.readLine()) != null) {
            partes = linha.split(" ");

            if (linha.contains("addAt")) {
                a = Integer.parseInt(partes[1]);
                b = Integer.parseInt(partes[2]);
                teste.addAt(a, b);
            } else if (linha.contains("add")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.add(a));
            } else if (linha.contains("get") && partes.length == 1) {
                System.out.println(teste.get());
            } else if (linha.contains("get")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.get(a));
            } else if (linha.contains("getFirst")) {
                System.out.println(teste.getFirst());
            } else if (linha.contains("set")) {
                a = Integer.parseInt(partes[1]);
                b = Integer.parseInt(partes[2]);
                teste.set(a, b);
            } else if (linha.contains("isEmpty")) {
                System.out.println(teste.isEmpty());
            } else if (linha.contains("remove") && partes.length == 1) {
                teste.remove();
            } else if (linha.contains("remove")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.remove(a));
            } else if (linha.contains("removeAt")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.removeAt(a));
            } else if (linha.contains("contains")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.contains(a));
            } else if (linha.contains("indexOf")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.indexOf(a));
            } else if (linha.contains("reverse")) {
                teste.reverse();
            } else if (linha.contains("deepCopy")) {
                teste_deep = teste.deepCopy();
                teste_deep.printList();
            } else if (linha.contains("print")) {
                teste.printList();
            } else if (linha.contains("soma")) {
                System.out.println(teste.reduce(op_soma, 0));
            } else if (linha.contains("mult")) {
                System.out.println(teste.reduce(op_mult, 1));
            } else if (linha.contains("div")) {
                System.out.println(teste.reduce(op_div, 1));
            } else if (linha.contains("sub")) {
                System.out.println(teste.reduce(op_sub, 0));
            } else if (linha.contains("map")) {
                teste.map(x -> x * x);
            }
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
                action.accept(elements[atual]);
                atual = next_index[atual];
            }
        }
    }

    private void resize(int new_capacity) {
        //Novos arrays
        int[] new_elements = new int[new_capacity];
        int[] new_next = new int[new_capacity];
        int[] new_prev = new int[new_capacity];

        if (new_capacity < capacity) { //Caso esteja diminuindo o array
            int atual = -1;

            if (!isEmpty()) {
                atual = next_index[head];

                //Cria uma nova cabeça
                new_elements[0] = elements[head];
                new_next[0] = (atual != -1) ? 1 : -1;
                prev_index[0] = -1;
                head = 0;
            }

            int i = 1;
            while (atual != -1) {
                new_elements[i] = elements[atual];
                new_next[i] = (next_index[atual] != -1) ? i + 1 : -1;
                new_prev[i] = i - 1;

                atual = next_index[atual];
                i++;
            }

            tail = isEmpty() ? -1 : i - 1;
            free_index = tail + 1; //O proximo espaço livre é depois do tail
            removedNodes = 0; //Não há mais lixo
        } else { //Caso estejamos aumentando o array
            System.arraycopy(elements, 0, new_elements, 0, capacity);
            System.arraycopy(next_index, 0, new_next, 0, capacity);
            System.arraycopy(prev_index, 0, new_prev, 0, capacity);
        }

        //Substitui os arrays antigos pelos novos
        elements = new_elements;
        next_index = new_next;
        prev_index = new_prev;
        capacity = new_capacity;
    }

    public boolean add(int item) {
        if (elements == null)
            return false;
        if (head == -1 && tail == -1) {
            this.head = this.free_index;
            this.tail = this.free_index;

            elements[this.free_index] = item;
            next_index[this.free_index] = -1;
            prev_index[this.free_index] = -1;

            free_index++;
        } else {

            if (free_index >= capacity)
                resize(capacity << 1); //Dobra o tamanho quase o array não seja o suficiente

            int next_free_index;
            if (removedNodes > 0) //Caso exista espaço lixo dentro do array
            {
                next_free_index = next_index[free_index]; //Guarda o proximo espaço lixo
                removedNodes--;
            } else {
                next_free_index = free_index + 1;
            }

            elements[free_index] = item;
            next_index[free_index] = -1;
            prev_index[free_index] = tail;

            next_index[tail] = free_index;
            tail = free_index;

            free_index = next_free_index;
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
        int node_value = elements[tail]; //Guarda o no para ‘posteriori’
        int node_prev = prev_index[tail];
        if (node_prev == -1) { //Caso esteja a remover o head
            //Lógica do free_index para substituição no add
            next_index[tail] = free_index;
            free_index = tail;
            //---

            head = -1;
            tail = -1;
        } else {
            next_index[prev_index[tail]] = -1; //O no que apontava para a cauda para de apontar para qualquer no

            //Lógica do free_index para substituição no add
            next_index[tail] = free_index;
            free_index = tail;
            //---

            tail = node_prev; //Muda a cauda para o no anterior
        }

        removedNodes++;
        size--;

        trashCollector();

        return node_value;
    }

    public int removeAt(int index) {
        int atual = getNodeIndex(index);
        if (atual == -1)
            throw new IndexOutOfBoundsException();
        if (atual == tail)
            return remove();

        int result = elements[atual];

        prev_index[next_index[atual]] = prev_index[atual]; //O no seguinte passa a apontar para o no anterior do atual
        if (prev_index[atual] != -1)
            next_index[prev_index[atual]] = next_index[atual]; //O no anterior passa a apontar para o proximo do atual
        else //Está a tentar retirar a cabeça
            head = next_index[atual];

        //Lógica do free_index para substituição no add
        next_index[atual] = free_index;
        free_index = atual;
        //---

        removedNodes++;
        size--;

        trashCollector();

        return result;
    }

    public boolean remove(int item) {
        int index = indexOf(item);

        if (index == -1)
            return false;
        removeAt(index);

        return true;
    }

    public void addAt(int index, int item) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Índice invalido");
        }
        if (isEmpty() && index != 0)
            throw new IndexOutOfBoundsException("Lista vazia");
        if (index == size) {
            if (add(item))
                return;
            else
                throw new IndexOutOfBoundsException("Índice inválido");
        }

        if (free_index >= capacity)
            resize(capacity << 1); //Dobra o tamanho caso o array não seja o suficiente

        int next_free_index;
        if (removedNodes > 0) //Caso exista espaço lixo dentro do array
        {
            next_free_index = next_index[free_index]; //Guarda o proximo espaço lixo
            removedNodes--;
        } else {
            next_free_index = free_index + 1;
        }

        int atual = getNodeIndex(index); //Busca onde está o elemento
        if (atual == -1)
            return;

        elements[free_index] = item;
        next_index[free_index] = atual;
        prev_index[free_index] = prev_index[atual];

        if (prev_index[free_index] == -1) {
            head = free_index;
        } else {
            next_index[prev_index[atual]] = free_index;
        }

        prev_index[atual] = free_index;

        free_index = next_free_index;
        size++;
    }

    public void set(int index, int value) {
        int atual = getNodeIndex(index);
        if (atual == -1)
            throw new IndexOutOfBoundsException("Lista vazia");
        elements[atual] = value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == -1;
    }

    private int getNodeIndex(int index) {
        if (index < 0 || index >= size) { //verifica se o indice é valido
            throw new IndexOutOfBoundsException("Índice invalido");
        }
        if (isEmpty()) { //verifica se a lista esta vazia
            return -1;
        }

        int atual;

        if (index < (size >> 1)) {
            atual = head;
            for (int i = 0; i < index; i++) { // percorre a lista até elemento anterior do index desejado
                atual = next_index[atual];
            }
        } else {
            atual = tail;
            for (int i = size - 1; i > index; i--) { // percorre a lista até elemento anterior do index desejado
                atual = prev_index[atual];
            }
        }

        return atual;
    }

    public int get(int index) {
        int atual = getNodeIndex(index);
        if (atual == -1)
            throw new IndexOutOfBoundsException("Lista vazia");
        return elements[atual];
    }

    public int getFirst() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }
        return elements[head];
    }

    public int get() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }
        return elements[tail];
    }

    public int indexOf(int item) {
        int index = 0, atual = head;
        while (atual != -1) {
            if (elements[atual] == item)
                return index;
            atual = next_index[atual];
            index++;
        }
        return -1;
    }

    public boolean contains(int item) {
        return indexOf(item) != -1;
    }

    public void map(UnaryOperator<Integer> op) {
        if (op == null) throw new NullPointerException("Operador invalido");
        if (isEmpty()) throw new IndexOutOfBoundsException("Lista vazia");
        int atual = head;
        while (atual != -1) {
            elements[atual] = op.apply(elements[atual]);
            atual = next_index[atual];
        }
    }

    public int reduce(BinaryOperator<Integer> op, int dfault) {
        if (isEmpty()) return dfault;

        int atual = head;
        int total = dfault;

        while (atual != -1) {
            total = op.apply(total, elements[atual]);
            atual = next_index[atual];
        }

        return total;
    }

    public void reverse() {
        if (isEmpty()) throw new IndexOutOfBoundsException("Lista vazia");
        if (!(next_index[head] == -1)) {
            int atual = head, next, prev = -1;
            while (atual != -1) {
                next = next_index[atual];  //guarda o next para depois alterar o prev
                next_index[atual] = prev;
                prev_index[atual] = next; //inverte o prev para ser o next e vise versa
                prev = atual;
                atual = next;
            }
            atual = head; //inverte o tail e o head
            head = tail;
            tail = atual;
        }

    }

    public FintList deepCopy() {
        FintList newList = new FintList();

        if (isEmpty()) //Caso esteja vazio, retorna uma lista vazia
            return newList;

        for (int v : this)
            newList.add(v);

        return newList;
    }

    private void printList() {
        System.out.print("Elementos: ");
        for (int v : this)
            System.out.print(v + "->");
        System.out.println();
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
            return next_index[nextNodeIndex];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn’t support removal");
        }
    }
}
package aed.collections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class FintList implements Iterable<Integer> {
    private static final int INITIAL_CAPACITY = 10;

    //Arrays de elementos da lista
    private int[] elements;
    private int[] next_index;
    private int[] prev_index;

    private int free_index; //Tamanho da lista de elementos vazios
    private int head; //Primeiro elemento da lista
    private int tail; //Ultimo elemento da lista
    private int capacity; //Tamanho total da lista
    private int size; //Quantidade de elementos na lista
    private int lastUsedNode; //
    private int lastArrayPosition;

    public FintList() {
        this.capacity = INITIAL_CAPACITY;
        this.elements = new int[capacity];
        this.next_index = new int[capacity];
        this.prev_index = new int[capacity];
        this.free_index = -1;
        this.tail = -1;
        this.head = -1;
        this.size = 0;
        this.lastUsedNode = -1;
        this.lastArrayPosition = -1;
    }

    private FintList(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.next_index = new int[capacity];
        this.prev_index = new int[capacity];
        this.free_index = -1;
        this.tail = -1;
        this.head = -1;
        this.size = 0;
        this.lastUsedNode = -1;
        this.lastArrayPosition = -1;
    }


    public static void main(String[] args) throws IOException {
        FintList teste = new FintList();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String linha;
        String[] partes;
        FintList teste_deep;
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

            if (linha.startsWith("addAt")) {
                a = Integer.parseInt(partes[1]);
                b = Integer.parseInt(partes[2]);
                teste.addAt(a, b);
            } else if (linha.startsWith("add")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.add(a));
            } else if (linha.startsWith("getFirst")) {
                System.out.println(teste.getFirst());
            } else if (linha.startsWith("get") && partes.length == 1) {
                System.out.println(teste.get());
            } else if (linha.startsWith("get")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.get(a));
            } else if (linha.startsWith("set")) {
                a = Integer.parseInt(partes[1]);
                b = Integer.parseInt(partes[2]);
                teste.set(a, b);
            } else if (linha.startsWith("isEmpty")) {
                System.out.println(teste.isEmpty());
            } else if (linha.startsWith("removeAt")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.removeAt(a));
            } else if (linha.startsWith("remove") && partes.length == 1) {
                teste.remove();
            } else if (linha.startsWith("remove")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.remove(a));
            } else if (linha.startsWith("contains")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.contains(a));
            } else if (linha.startsWith("indexOf")) {
                a = Integer.parseInt(partes[1]);
                System.out.println(teste.indexOf(a));
            } else if (linha.startsWith("reverse")) {
                teste.reverse();
            } else if (linha.startsWith("deepCopy")) {
                teste_deep = teste.deepCopy();
                teste_deep.printList();
            } else if (linha.startsWith("print")) {
                teste.printList();
            } else if (linha.startsWith("soma")) {
                System.out.println(teste.reduce(op_soma, 0));
            } else if (linha.startsWith("mult")) {
                System.out.println(teste.reduce(op_mult, 1));
            } else if (linha.startsWith("div")) {
                System.out.println(teste.reduce(op_div, 1));
            } else if (linha.startsWith("sub")) {
                System.out.println(teste.reduce(op_sub, 0));
            } else if (linha.startsWith("map")) {
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

        int atual = -1;

        if (!isEmpty()) {
            atual = next_index[head];

            //Cria uma nova cabeça
            new_elements[0] = elements[head];
            new_next[0] = (atual != -1) ? 1 : -1;
            new_prev[0] = -1;
            head = 0;
        }

        int i = 1;
        while (atual != -1) {
            new_elements[i] = elements[atual];
            new_next[i] = i + 1;
            new_prev[i] = i - 1;

            atual = next_index[atual];
            i++;
        }

        if (!isEmpty()) {
            new_next[i - 1] = -1;
        }

        new_next[i - 1] = -1;

        tail = isEmpty() ? -1 : i - 1;
        free_index = -1; //O proximo espaço livre é depois do tail

        //Substitui os arrays antigos pelos novos
        elements = new_elements;
        next_index = new_next;
        prev_index = new_prev;
        capacity = new_capacity;
        resetState();
    }

    public boolean add(int item) {
        if (elements == null)
            return false;

        if (free_index == -1 && size == capacity)
            resize(capacity << 1); //Dobra o tamanho quase o array não seja o suficiente

        int next_free_index = -1;
        int slot = size;
        if (free_index != -1) //Caso exista espaço lixo dentro do array
        {
            next_free_index = next_index[free_index]; //Guarda o proximo espaço lixo
            slot = free_index;
        }

        elements[slot] = item;
        next_index[slot] = -1;
        prev_index[slot] = tail;

        if (tail != -1)
            next_index[tail] = slot;
        else
            head = slot;

        tail = slot;

        free_index = next_free_index;

        size++;
        return true;
    }

    private void resetState() {
        lastArrayPosition = -1;
        lastUsedNode = -1;
    }

    private void trashCollector() {
        if (capacity >> 2 > size && capacity > INITIAL_CAPACITY) { //Caso mais de 3/4 do array for lixo
            resize(Math.max(capacity >> 1, INITIAL_CAPACITY)); //Diminui para metade da capacidade
        }
    }

    public int remove() {
        if (isEmpty())
            throw new IndexOutOfBoundsException("Lista vazia");

        if (lastArrayPosition == tail)
            resetState();

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

        size--;

        return node_value;
    }

    public int removeAt(int index) {
        int atual = getNodeIndex(index);
        if (atual == tail)
            return remove();

        lastArrayPosition = next_index[atual]; //O último no que buscamos foi apagado, então o que vai estar na sua posição é o seguinte

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

        size--;

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
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Índice invalido");
        }
        if (index == size) {
            if (add(item))
                return;
            else
                throw new IndexOutOfBoundsException("Índice inválido");
        }

        if (free_index == -1 && size >= capacity)
            resize(capacity << 1); //Dobra o tamanho caso o array não seja o suficiente

        int next_free_index = -1;
        int slot = size;
        if (free_index != -1) //Caso exista espaço lixo dentro do array
        {
            next_free_index = next_index[free_index]; //Guarda o proximo espaço lixo
            slot = free_index;
        }

        int atual;

        atual = getNodeIndex(index); //Busca onde está o elemento

        elements[slot] = item;
        next_index[slot] = atual;
        prev_index[slot] = prev_index[atual];

        lastUsedNode = index;
        lastArrayPosition = slot;

        if (index == 0) {
            head = slot;
        } else {
            next_index[prev_index[atual]] = slot;
        }

        prev_index[atual] = slot;

        free_index = next_free_index;
        size++;

        System.out.println("head: " + head);
    }

    public void set(int index, int value) {
        int atual = getNodeIndex(index);
        elements[atual] = value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == -1;
    }

    private int getNodeIndex(int index) {
        if (index < 0 || index >= size) { //verifica se o índice é valido
            throw new IndexOutOfBoundsException("Índice invalido");
        }
        if (index == lastUsedNode && lastArrayPosition != -1)
            return lastArrayPosition;

        int atual;

        int indexProximity = Math.abs(index - lastUsedNode);
        int tailProximity = (size - 1 - index);

        if (lastArrayPosition != -1 && indexProximity <= index && indexProximity <= tailProximity) {

            atual = lastArrayPosition;
            if (index < lastUsedNode) {
                for (int i = lastUsedNode; i > index; i--) { // percorre a lista até elemento anterior do index desejado
                    atual = prev_index[atual];
                }
            } else {
                for (int i = lastUsedNode; i < index; i++) { // percorre a lista até elemento anterior do index desejado
                    atual = next_index[atual];
                }
            }

            lastArrayPosition = atual;
            lastUsedNode = index;
            return lastArrayPosition;
        }
        if (index <= tailProximity) {
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

        lastArrayPosition = atual;
        lastUsedNode = index; //Ultimo nó utilizado foi o que buscamos
        return atual;
    }

    public int get(int index) {
        int atual = getNodeIndex(index);
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
        int atual = head;
        while (atual != -1) {
            if (elements[atual] == item)
                return true;
            atual = next_index[atual];
        }
        return false;
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
            int atual = head, next;
            while (atual != -1) {
                next = next_index[atual];  //guarda o next para depois alterar o prev
                next_index[atual] = prev_index[atual];
                prev_index[atual] = next; //inverte o prev para ser o next e vise versa
                atual = next;
            }
            atual = head; //inverte o tail e o head
            head = tail;
            tail = atual;
            resetState();
        }

    }

    public FintList deepCopy() {
        if (isEmpty()) //Caso esteja vazio, retorna uma lista vazia
            return new FintList();

        FintList new_list = new FintList(this.size);
        int atual = head;

        for (int i = 0, j = -1, k = 1; i < size; i++, j++, k++) {
            new_list.elements[i] = elements[atual];
            new_list.next_index[i] = k;
            new_list.prev_index[i] = j;

            atual = next_index[atual];
        }

        new_list.head = 0;
        new_list.size = size;
        new_list.tail = size - 1;
        new_list.next_index[new_list.tail] = -1;

        return new_list;
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
            if (nextNodeIndex == -1) throw new NoSuchElementException();

            int result = elements[nextNodeIndex];
            nextNodeIndex = next_index[nextNodeIndex];
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn’t support removal");
        }
    }
}
package aed.collections;

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
            elements[this.free_index] = new intNode(item, -1, -1);
            free_index++;
        } else {
            intNode newNode = new intNode(item, -1, tail);

            int next_free_index = -1;
            if (elements[tail].next_index != -1)
                next_free_index = elements[tail].next_index;

            elements[free_index] = newNode;
            elements[tail].next_index = free_index;
            tail = free_index;

            if (next_free_index != -1)
                free_index = next_free_index;
            else
                free_index++;
        }

        return true;
    }
}

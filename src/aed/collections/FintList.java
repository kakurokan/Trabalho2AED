package aed.collections;

public class FintList {
    private static final int INITIAL_CAPACITY = 10;

    private intNode[] elements; //Array de elementos da lista
    private int[] free_stack; //"Stack" dos elementos vazios da lista
    private int last_free_index; //Tamanho da lista de elementos vazios
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
        this.free_stack = new int[capacity];
        free_stack[0] = 0;
        this.last_free_index = 0;
        this.tail = -1;
        this.head = -1;
    }

    private void resize(int new_capacity) {

    }

    boolean add(int item) {
        if (elements == null)
            return false;
        int free_index = free_stack[last_free_index--];
        if (tail == -1) {
            this.head = 0;
            elements[free_index] = new intNode(item, -1, -1);
        } else {
            intNode newNode = new intNode(item, -1, tail);
            elements[free_index] = newNode;
            elements[tail].next_index = free_index;
        }
        
        tail++;
        return true;
    }
}

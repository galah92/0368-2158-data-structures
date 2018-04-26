package dheap;

/**
 * A given implementation of distinct items for a D-Heap
 * 
 * @author ID : 203521984
 * @author ID : 203774849
 */
public class DHeap_Item {

    private String name;
    private int key;
    private int pos; // Position in the heap (if inserted into a heap.)

    public DHeap_Item(String name1, int key1) {
        name = name1;
        key = key1;
        pos = -1;
    }

    public void setKey(int key1) {
        key = key1;
    }

    public void setPos(int pos1) {
        pos = pos1;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public int getPos() {
        return pos;
    }
}
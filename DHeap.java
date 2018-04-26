package dheap;

/**
 * An implementation of a D-Heap with distinct items
 * 
 * @author ID : 203521984
 * @author ID : 203774849
 */
public class DHeap {

    private int size;
    private int max_size;
    private int d;
    private DHeap_Item[] array;

    DHeap(int m_d, int m_size) {
        max_size = m_size;
        d = m_d;
        array = new DHeap_Item[max_size];
        size = 0;
    }

    /**
     * @return the number of elements in the heap.
     * @complexity O(1)
     */
    public int getSize() {
        return size;
    }

    /**
     * Build a new heap from the given array, while previous data of the heap is
     * being erased.
     * 
     * @param array1
     *            the array to build a heap from
     * @return the number of comparisons along the function run
     * @precondition array1.length() <= max_size
     * @precondition isHeap()
     * @precondition size = array.length
     * @complexity O(n) where n == size
     */
    public int arrayToHeap(DHeap_Item[] array1) {
        int comparisonsCount = 0;
        System.arraycopy(array1, 0, array, 0, array1.length);
        for (int i = 0; i < array1.length; i++) {
            array[i].setPos(i);
        }
        size = array1.length; // don't have to as it's preconditioned
        for (int i = size / d; i >= 0; i--) {
            comparisonsCount += heapifyDown(i);
        }
        return comparisonsCount;
    }

    /**
     * @return true if the array satisfies the d-heap property, false otherwise.
     * @complexity O(n) where n == size
     */
    public boolean isHeap() {
        for (int i = size - 1; i > 0; i--) {
            if (array[i].getKey() < array[parent(i, d)].getKey()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param i
     *            the index
     * @param d
     *            the heap factor
     * @return the index of the parent of i in a heap with factor d
     * @precondition i >= 0
     * @precondition d >= 2
     * @complexity O(1)
     */
    public static int parent(int i, int d) {
        return (i - 1) / d;
    }

    /**
     * @param i
     *            the index
     * @param d
     *            the heap factor
     * @return the index of the k-th child of vertex i in a heap with factor d
     * @precondition i >= 0
     * @precondition d >= 2
     * @precondition 1 <= k <= d
     * @complexity O(1)
     */
    public static int child(int i, int k, int d) {
        return i * d + k;
    }

    /**
     * Insert the given item to the heap.
     * 
     * @param item
     *            the item to insert
     * @return the number of comparisons along the function run
     * @precondition: item != null
     * @precondition isHeap()
     * @precondition size < max_size
     * @complexity O(logd(n)) due to heapifyUp()
     */
    public int Insert(DHeap_Item item) {
        size++;
        array[size - 1] = item;
        item.setPos(size - 1);
        return heapifyUp(size - 1);
    }

    /**
     * Delete the minimum item in the heap.
     * 
     * @return the number of comparisons along the function run
     * @precondition size > 0 isHeap()
     * @complexity O(d * logd(n)) due to heapifyDown()
     */
    public int Delete_Min() {
        array[0] = array[size - 1];
        array[0].setPos(0);
        size--;
        return heapifyDown(0);
    }

    /**
     * @return the minimum item in the heap
     * @precondition size > 0 isHeap() size > 0
     * @precondition isHeap()
     * @complexity O(1)
     */
    public DHeap_Item Get_Min() {
        return array[0];
    }

    /**
     * Decrease the key of the given item by delta.
     * 
     * @param item
     *            the item to decrease
     * @param delta
     *            the factor to decrease in
     * @return the number of comparisons along the function run
     * @precondition: item.pos < size
     * @precondition item != null
     * @precondition isHeap()
     * @complexity O(logd(n)) due to heapifyUp()
     */
    public int Decrease_Key(DHeap_Item item, int delta) {
        item.setKey(delta == Integer.MAX_VALUE ? Integer.MIN_VALUE : item.getKey() - delta);
        return heapifyUp(item.getPos());
    }

    /**
     * Delete the given item from the heap.
     * 
     * @param item
     *            the item to delete
     * @return the number of comparisons along the function run
     * @precondition: item.pos < size
     * @precondition item != null
     * @precondition isHeap()
     * @complexity O(d * logd(n)) due to Delete_Min()
     */
    public int Delete(DHeap_Item item) {
        return Decrease_Key(item, Integer.MAX_VALUE) + Delete_Min();
    }

    /**
     * Sort the input array using heap-sort (build a heap, and perform n times
     * Get_Min() and Delete_Min()).
     * 
     * @param array1
     *            the array to sort
     * @param d
     *            the heap factor
     * @return the number of comparisons along the function run
     * @complexity O(d * n * logd(n)) where n is array1.length
     */
    public static int DHeapSort(int[] array1, int d) {
        int comparisonsCount = 0;
        DHeap_Item[] items = new DHeap_Item[array1.length];
        for (int i = 0; i < array1.length; i++) {
            items[i] = new DHeap_Item(null, array1[i]);
        } // now build the heap and do the magic
        DHeap heap = new DHeap(d, array1.length);
        comparisonsCount += heap.arrayToHeap(items);
        for (int i = 0; i < array1.length; i++) {
            array1[i] = heap.Get_Min().getKey();
            comparisonsCount += heap.Delete_Min();
        }
        return comparisonsCount;
    }

    /**
     * Restore the heap property from top to bottom.
     * 
     * @param i
     *            the item
     * @return the number of comparisons along the function run
     * @complexity O(d * logd(n)) where d is the heap factor and n == size
     */
    private int heapifyDown(int i) {
        int comparisonsCount = 0;
        // get the index of the direct child of i with minimum key
        int min = child(i, 1, d) < size ? child(i, 1, d) : -1; // leftmost child
        for (int k = 2; k < 1 + d && child(i, k, d) < size; k++, comparisonsCount++) {
            if (array[child(i, k, d)].getKey() < array[min].getKey()) {
                min = child(i, k, d);
            }
        }
        if (min != -1 && array[i].getKey() > array[min].getKey()) {
            swapItems(i, min);
            return comparisonsCount + 1 + heapifyDown(min);
        } else {
            return comparisonsCount + 1;
        }
    }

    /**
     * Restore the heap property from bottom to top.
     * 
     * @param i
     *            the item
     * @return the number of comparisons along the function run
     * @complexity O(logd(n)) where d is the heap factor and n == size
     */
    private int heapifyUp(int i) {
        if (i == 0) {
            return 0;
        }
        int comparisonsCount = 1;
        while (i > 0 && array[i].getKey() < array[parent(i, d)].getKey()) {
            swapItems(i, parent(i, d));
            i = parent(i, d);
            comparisonsCount++;
        }
        return comparisonsCount;
    }

    /**
     * Swap items in the array and update their pos property.
     * 
     * @param i
     *            an item
     * @param j
     *            another item
     * @complexity O(1)
     */
    private void swapItems(int i, int j) {
        DHeap_Item temp = array[i];
        array[i] = array[j];
        array[i].setPos(i);
        array[j] = temp;
        array[j].setPos(j);
    }

}

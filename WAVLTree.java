package wavltree;

/**
 * An implementation of a WAVL Tree with distinct integer keys and string info
 * 
 * @author ID : 203521984
 * @author ID : 203774849
 */
public class WAVLTree {

    public static WAVLNode EXT_NODE; // used as sentinel

    public WAVLNode root;
    public WAVLNode minNode;
    public WAVLNode maxNode;

    /**
     * Initialize an empty tree.
     */
    public WAVLTree() {
        EXT_NODE = new WAVLNode();
        root = EXT_NODE;
        minNode = root;
        maxNode = root;
    }

    /**
     * @return true if the tree is empty
     * @complexity O(1) clearly
     */
    public boolean empty() {
        return root == EXT_NODE;
    }

    /**
     * Returns the value associated with the given key.
     * 
     * @param k
     *            the key
     * @return the value associated with the given key if the key is in the
     *         tree, else null
     * @complexity O(log(n)) where n is the size of the tree, as search(int k)
     *             takes O(log(h))
     */
    public String search(int k) {
        return search(root, k).value;
    }

    /**
     * Returns the node associated with the given key in the subtree or null if
     * no such node.
     * 
     * @param x
     *            the subtree
     * @param k
     *            the key
     * @return node associated with the given key in the subtree or null if no
     *         such node
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree from bottom to top
     */
    private WAVLNode search(WAVLNode x, int k) {

        if (x == EXT_NODE) {
            return EXT_NODE;
        } else if (k < x.key) {
            return search(x.left, k);
        } else if (k > x.key) {
            return search(x.right, k);
        } else { // must be equal
            return x;
        }
    }

    /**
     * Inserts the specified key-value pair into the tree. Does not change the
     * tree if the key already exists.
     * 
     * @param k
     *            the key
     * @param i
     *            the value
     * @return the number of re-balancing operations, 0 if no re-balancing
     *         operations were necessary, -1 if an item with key k already
     *         exists in the tree
     * @complexity O(log(n)) where n is the size of the tree, as treePosition()
     *             and rebalanceAfterInsertion() and updateSizeOfAllParents()
     *             all take O(log(n))
     */
    public int insert(int k, String i) {
        WAVLNode z = treePosition(root, k);
        if (z == EXT_NODE) {
            root = new WAVLNode(k, i, null);
            minNode = root;
            maxNode = root;
            return 0;
        } else if (k == z.key) {
            return -1;
        } else if (k < z.key) {
            z.left = new WAVLNode(k, i, z);
        } else {
            z.right = new WAVLNode(k, i, z);
        }
        updateSizeOfAllParents(z);
        if (k < minNode.key) {
            minNode = z.left;
        } else if (k > maxNode.key) {
            maxNode = z.right;
        }
        return rebalanceAfterInsertion(z);
    }

    /**
     * Look for a key in a subtree, returns the last node encountered.
     * 
     * @param x
     *            the subtree
     * @param k
     *            the key
     * @return the last node encountered before the node with key k
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree from bottom to top
     */
    private WAVLNode treePosition(WAVLNode x, int k) {
        WAVLNode y = x;
        while (x != EXT_NODE) {
            y = x;
            if (k == x.key) {
                return x;
            } else if (k < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return y;
    }

    /**
     * Restores the WAVL tree property of the subtree after insertion was made.
     * 
     * @param z
     *            the subtree
     * @return the number of re-balance operations did
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree from bottom to top
     */
    private int rebalanceAfterInsertion(WAVLNode z) {
        int operationCount = 0;
        while (z != null && hasRankPotentialZero(z)) {
            if (balanceFactor(z) == 1 || balanceFactor(z) == -1) { // case 1
                promote(z);
                operationCount++;
            } else if (balanceFactor(z) == 2) { // 'left' cases
                if (balanceFactor(z.left) == 1) { // 'left' case 2;
                    z = insertionCase2Left(z);
                    operationCount += 2;
                } else { // 'left' case 3
                    z = insertionCase3Left(z);
                    operationCount += 5;
                }
            } else if (balanceFactor(z) == -2) {
                if (balanceFactor(z.right) == -1) { // 'right' case 2
                    z = insertionCase2Right(z);
                    operationCount += 2;
                } else { // 'right' case 3
                    z = insertionCase3Right(z);
                    operationCount += 5;
                }
            }
            if (z.parent == null) {
                root = z;
                break;
            } else {
                z = z.parent;
            }
        }
        return operationCount;
    }

    /**
     * Increase the rank of a node by 1.
     * 
     * @param x
     *            the node
     * @complexity O(1) clearly
     */
    private void promote(WAVLNode x) {
        x.rank++;
    }

    /**
     * Decrease the rank of a node by 1.
     * 
     * @param x
     *            the node
     * @complexity O(1) clearly
     */
    private void demote(WAVLNode x) {
        x.rank--;
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode insertionCase2Left(WAVLNode z) {
        demote(z);
        return rotateRight(z);
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode insertionCase2Right(WAVLNode z) {
        demote(z);
        return rotateLeft(z);
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode insertionCase3Left(WAVLNode z) {
        demote(z);
        demote(z.left);
        promote(z.left.right);
        rotateLeft(z.left);
        return rotateRight(z);
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode insertionCase3Right(WAVLNode z) {
        demote(z);
        demote(z.right);
        promote(z.right.left);
        rotateRight(z.right);
        return rotateLeft(z);
    }

    /**
     * @param x
     *            the node
     * @return true if and only if the node has an equal rank as his left/right
     *         subtrees, false otherwise
     * @complexity O(1) clearly
     */
    private boolean hasRankPotentialZero(WAVLNode x) {
        return x.rank == x.left.rank || x.rank == x.right.rank;
    }

    /**
     * @param x
     *            the node
     * @return the rank difference between the left and right subtrees of x
     * @complexity O(1) clearly
     */
    private int balanceFactor(WAVLNode x) {
        return x.left.rank - x.right.rank;
    }

    /**
     * Rotate the subtree to the right, increasing the height of the right
     * subtree by 1 while decreasing the height of the left subtree by 1.
     * 
     * @param y
     *            the subtree
     * @return the node that replaced y as the subtree root
     * @complexity O(1)
     */
    private WAVLNode rotateRight(WAVLNode y) {
        WAVLNode x = y.left;
        WAVLNode b = x.right;
        WAVLNode p = y.parent;
        y.left = b;
        b.parent = y;
        x.right = y;
        x.parent = p;
        y.parent = x;
        if (p != null) {
            if (p.left == y) {
                p.left = x;
            } else {
                p.right = x;
            }
        }
        x.size = y.size;
        y.size = y.left.size + y.right.size + 1;
        return x;
    }

    /**
     * Rotate the subtree to the left, increasing the height of the left subtree
     * by 1 while decreasing the height of the right subtree by 1.
     * 
     * @param y
     *            the subtree
     * @return the node that replaced y as the subtree root
     * @complexity O(1)
     */
    private WAVLNode rotateLeft(WAVLNode y) {
        WAVLNode x = y.right;
        WAVLNode b = x.left;
        WAVLNode p = y.parent;
        y.right = b;
        b.parent = y;
        x.left = y;
        x.parent = p;
        y.parent = x;
        if (p != null) {
            if (p.left == y) {
                p.left = x;
            } else {
                p.right = x;
            }
        }
        x.size = y.size;
        y.size = y.left.size + y.right.size + 1;
        return x;
    }

    /**
     * Removes the specified key and its value from the tree, if it is there.
     * 
     * @param k
     * @return the number of re-balancing operation, or -1 if a node with a key
     *         k was not found in the tree
     * @complexity O(log(n)) where n is the size of the tree, as treePosition()
     *             and rebalanceAfterDeletion() and updateSizeOfAllParents() all
     *             take O(log(n))
     */
    public int delete(int k) {
        WAVLNode y = treePosition(root, k);
        if (k != y.key) { // k not in tree
            return -1;
        }
        if (k == minNode.key) {
            minNode = y.right == EXT_NODE ? y.parent : successor(y);
        } else if (k == maxNode.key) {
            maxNode = y.left == EXT_NODE ? y.parent : predecessor(y);
        }
        if (y.left != EXT_NODE && y.right != EXT_NODE) { // y is binary
            y = replaceWithSuccessorOrPredecessor(y);
        }
        WAVLNode z = y.parent;
        if (y.left != EXT_NODE || y.right != EXT_NODE) { // y is unary
            removeUnaryNode(y);
        } else { // y is leaf
            removeLeafNode(y);
        }
        updateSizeOfAllParents(z);
        return rebalanceAfterDeletion(z);
    }

    /**
     * Restores the WAVL tree property of the subtree after deletion was made.
     * 
     * @param z
     *            the subtree
     * @return the number of re-balance operations did
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree from bottom to top
     */
    private int rebalanceAfterDeletion(WAVLNode z) {
        int operationCount = 0;
        if (z != null && is22Leaf(z)) { // leftover from case 3
            demote(z);
            operationCount++;
            z = z.parent;
        }
        while (z != null && hasRankPotentialThree(z)) {
            if (balanceFactor(z) == 1 || balanceFactor(z) == -1) { // case 1
                demote(z);
                operationCount++;
            } else if (balanceFactor(z) == -2) {
                if (z.right.rank - z.right.right.rank == 2) {
                    if (z.right.rank - z.right.left.rank == 2) { // case 2
                        z = deletionCase2Left(z);
                        operationCount += 2;
                    } else { // case 4
                        z = deletionCase4Left(z);
                        operationCount += 7;
                    }
                } else { // case 3
                    if (z.right.rank - z.right.left.rank == 2) {
                        demote(z);
                        operationCount++;
                    }
                    demote(z);
                    promote(z.right);
                    z = rotateLeft(z);
                    operationCount += 3;
                }
            } else if (balanceFactor(z) == 2) {
                if (z.left.rank - z.left.left.rank == 2) {
                    if (z.left.rank - z.left.right.rank == 2) { // case 2
                        z = deletionCase2Right(z);
                        operationCount += 2;
                    } else { // case 4
                        z = deletionCase4Right(z);
                        operationCount += 7;
                    }
                } else { // case 3
                    if (z.left.rank - z.left.right.rank == 2) {
                        demote(z);
                        operationCount++;
                    }
                    demote(z);
                    promote(z.left);
                    z = rotateRight(z);
                    operationCount += 3;
                }
            }
            if (z.parent == null) {
                root = z;
                break;
            } else {
                z = z.parent;
            }
        }
        return operationCount;
    }

    /**
     * @param z
     *            the node
     * @return true if z is a 2,2 leaf, false otherwise
     */
    private boolean is22Leaf(WAVLNode z) {
        return z.left == EXT_NODE && z.right == EXT_NODE && z.rank == 1;
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode deletionCase2Left(WAVLNode z) {
        demote(z);
        demote(z.right);
        return z;
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode deletionCase2Right(WAVLNode z) {
        demote(z);
        demote(z.left);
        return z;
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode deletionCase4Left(WAVLNode z) {
        demote(z);
        demote(z);
        promote(z.right.left);
        promote(z.right.left);
        demote(z.right);
        rotateRight(z.right);
        return rotateLeft(z);
    }

    /**
     * Execute the desired operations on a node as the method name states.
     * 
     * @param z
     *            the node
     * @return
     * @complexity O(1)
     */
    private WAVLNode deletionCase4Right(WAVLNode z) {
        demote(z);
        demote(z);
        promote(z.left.right);
        promote(z.left.right);
        demote(z.left);
        rotateLeft(z.left);
        return rotateRight(z);
    }

    /**
     * @param x
     *            the node
     * @return true if and only if the node has a rank difference of 3 with his
     *         left/right subtrees, false otherwise
     * @complexity O(1) clearly
     */
    private boolean hasRankPotentialThree(WAVLNode z) {
        return z.rank == z.left.rank + 3 || z.rank == z.right.rank + 3;
    }

    /**
     * Replace a node with his successor if he has one (if he is not the maximum
     * in the tree), else with his predecessor.
     * 
     * @param y
     *            the node
     * @return the node y in his new position in the tree
     * @complexity O(log(n)) where n is the size of the tree, as
     *             successor()/predecessor() takes O(log(h))
     */
    private WAVLNode replaceWithSuccessorOrPredecessor(WAVLNode y) {
        WAVLNode x = y == maxNode ? predecessor(y) : successor(y);
        int k = y.key;
        String i = y.value;
        y.key = x.key;
        y.value = x.value;
        x.key = k;
        x.value = i;
        return x;
    }

    /**
     * Remove a node from the tree
     * 
     * @param y
     *            the node
     * @return y parent node
     * @complexity O(1)
     */
    private WAVLNode removeUnaryNode(WAVLNode y) {
        WAVLNode z = y.parent;
        WAVLNode x = y.left == EXT_NODE ? y.right : y.left;
        x.parent = z;
        if (z == null) {
            root = EXT_NODE;
            minNode = root;
            maxNode = root;
        } else {
            if (z.left == y) {
                z.left = x;
            } else {
                z.right = x;
            }
        }
        return y;
    }

    /**
     * Remove a node from the tree. Update the root/minNode/maxNode if needed.
     * 
     * @param y
     *            the node
     * @return y parent node
     * @complexity O(1)
     */
    private WAVLNode removeLeafNode(WAVLNode y) {
        WAVLNode z = y.parent;
        if (z == null) {
            root = EXT_NODE;
            minNode = root;
            maxNode = root;
        } else {
            if (z.left == y) {
                z.left = EXT_NODE;
            } else {
                z.right = EXT_NODE;
            }
        }
        if (y == minNode) {
            minNode = z;
        } else if (y == maxNode) {
            maxNode = z;
        }
        return y;
    }

    /**
     * @param x
     *            the node
     * @return the node following x according to the sorted order of keys
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree
     */
    private WAVLNode successor(WAVLNode x) {
        if (x.right != null && x.right != EXT_NODE) {
            return min(x.right);
        }
        WAVLNode y = x.parent;
        while (y != null && x == y.getRight()) {
            x = y;
            y = x.parent;
        }
        return y;
    }

    /**
     * @param x
     *            the node
     * @return the node previous to x according to the sorted order of keys
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree
     */
    private WAVLNode predecessor(WAVLNode x) {
        if (x.left != null && x.left != EXT_NODE) {
            return max(x.left);
        }
        WAVLNode y = x.parent;
        while (y != null && x == y.getLeft()) {
            x = y;
            y = x.parent;
        }
        return y;
    }

    /**
     * Update the size field of all parents of a node
     * 
     * @param x
     *            the node
     * @complexity O(log(n)) where n is the size of the tree
     */
    private void updateSizeOfAllParents(WAVLNode x) {
        while (x != null && x != EXT_NODE) {
            x.size = x.left.size + x.right.size + 1;
            x = x.parent;
        }
    }

    /**
     * @return the info string of the item with the smallest key in the tree,
     *         null otherwise
     * @complexity: O(1) clearly
     */
    public String min() {
        return minNode.value;
    }

    /**
     * @param x
     *            the subtree
     * @return the info string of the item with the smallest key in the subtree
     *         x, null otherwise
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree
     */
    private WAVLNode min(WAVLNode x) {
        while (x.left != EXT_NODE) {
            x = x.left;
        }
        return x;
    }

    /**
     * @return the info string of the item with the greatest key in the tree,
     *         null otherwise
     * @complexity: O(1) clearly
     */
    public String max() {
        return maxNode.value;
    }

    /**
     * @param x
     *            the subtree
     * @return the info string of the item with the greatest key in the subtree
     *         x, null otherwise
     * @complexity O(log(n)) where n is the size of the tree, as we traverse the
     *             tree
     */
    private WAVLNode max(WAVLNode node) {
        while (node.right != EXT_NODE) {
            node = node.right;
        }
        return node;
    }

    /**
     * @return a sorted array which contains all keys in the tree
     * @complexity O(n) where n is the size of the tree
     */
    public int[] keysToArray() {
        int[] keys = new int[size()];
        if (root.size == 0) {
            return keys;
        }
        keysToArray(keys, root, 0);
        return keys;
    }

    /**
     * @param arr
     *            the array
     * @param x
     *            the subtree
     * @param i
     *            the index
     * @return a sorted array which contains all keys in the subtree x
     * @complexity O(n) where n is the size of the subtree
     */
    public int keysToArray(int[] arr, WAVLNode x, int i) {
        if (root.left != EXT_NODE) {
            i = keysToArray(arr, x.left, i);
        }
        arr[i] = x.key;
        if (root.right != EXT_NODE) {
            i = keysToArray(arr, x.right, i);
        }
        return i;
    }

    /**
     * @return a sorted array which contains all the info of the nodes, sorted
     *         by the respective keys in the tree
     * @complexity O(n) where n is the size of the tree
     */
    public String[] infoToArray() {
        String[] infos = new String[size()];
        if (root.size == 0) {
            return infos;
        }
        infoToArray(infos, root, 0);
        return infos;
    }

    /**
     * @param arr
     *            the array
     * @param x
     *            the subtree
     * @param i
     *            the index
     * @return a sorted array which contains all the info in the subtree x
     * @complexity O(n) where n is the size of the subtree
     */
    public int infoToArray(String[] arr, WAVLNode node, int i) {
        if (root.left != EXT_NODE) {
            i = infoToArray(arr, node.left, i);
        }
        arr[i] = node.value;
        if (root.right != EXT_NODE) {
            i = infoToArray(arr, node.right, i);
        }
        return i;
    }

    /**
     * @return the number of nodes in the tree
     * @complexity O(1) clearly
     */
    public int size() {
        return size(root);
    }

    /**
     * Returns the number of nodes in the subtree.
     * 
     * @param x
     *            the subtree
     * @return the number of nodes in the subtree
     * @complexity O(1) clearly
     */
    private int size(WAVLNode x) {
        return x.size;
    }

    /**
     * @return the root node, or null if the tree is empty
     * @complexity O(1) clearly
     */
    public IWAVLNode getRoot() {
        return root;
    }

    /**
     * @param i
     *            the key
     * @return the value of the i'th smallest key, -1 if the tree is empty
     * @complexity O(log(n)) where n is the size of the tree
     */
    public String select(int i) {
        return select(root, i).value;
    }

    private WAVLNode select(WAVLNode x, int i) {
        int r = x.left == EXT_NODE ? 1 : x.left.size;
        if (i == r) {
            return x;
        } else if (i < r) {
            return select(x.left, i);
        } else {
            return select(x.right, i - r - 1);
        }
    }

    /**
     * public interface which encapsulates and simplifies the implementation of
     * a tree node
     */
    public interface IWAVLNode {

        /**
         * @return the node's key (-1 for virtual node)
         */
        public int getKey();

        /**
         * @return the node's value [info] (null for virtual node)
         */
        public String getValue();

        /**
         * @return the left child (null if there is no left child)
         */
        public IWAVLNode getLeft();

        /**
         * @return the right child (null if there is no left child)
         */
        public IWAVLNode getRight();

        /**
         * @return true if this is the sentinel WAVL node, false otherwise
         */
        public boolean isRealNode();

        /**
         * @return the number of real nodes in this node's subtree
         */
        public int getSubtreeSize();

        /**
         * @return the node's rank (-1 for virtual node)
         */
        public int getRank();

    }

    public class WAVLNode implements IWAVLNode {

        public int key;
        public String value;
        public WAVLNode left;
        public WAVLNode right;
        public WAVLNode parent;
        public int rank;
        public int size;

        public WAVLNode(int key, String value, WAVLNode parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = EXT_NODE;
            this.right = EXT_NODE;
            this.rank = 0;
            this.size = 1;
        }

        /**
         * Initialize EXT_NODE, should be called only once.
         */
        public WAVLNode() {
            key = -1;
            value = null;
            rank = -1;
            size = 0;
        }

        /**
         * @return the node's key (-1 for virtual node)
         */
        public int getKey() {
            return key;
        }

        /**
         * @return the node's value [info] (null for virtual node)
         */
        public String getValue() {
            return value;
        }

        /**
         * @return the left child (null if there is no left child)
         */
        public IWAVLNode getLeft() {
            return left == EXT_NODE ? null : left;
        }

        /**
         * @return the right child (null if there is no left child)
         */
        public IWAVLNode getRight() {
            return right == EXT_NODE ? null : right;
        }

        /**
         * @return true if this is the sentinel WAVL node, false otherwise
         */
        public boolean isRealNode() {
            return this != EXT_NODE;
        }

        /**
         * @return the number of real nodes in this node's subtree
         */
        public int getSubtreeSize() {
            return size;
        }

        /**
         * @return the node's rank (-1 for virtual node)
         */
        public int getRank() {
            return rank;
        }

    }

}

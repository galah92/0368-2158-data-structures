package dheap;

import java.util.Random;

public class DHeap_Tester {

	private static int RAND_MAX = 1000;
	private static int[] M = { 1000, 10000, 100000 };
	private static int[] X = { 1, 100, 1000 };
	private static int[] D = { 2, 3, 4 };

	public static void main(String[] args) {
		doMeasurements();
//		DHeapTester_Tamir TamirTester = new DHeapTester_Tamir(150,150);
	}

	private static void doMeasurements() {
//		doTest1();
		doTest2();
	}

	private static void doTest1() {
		int[] arr;
		int comparisonsCount;
		double avg;
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < D.length; j++) {
				avg = 0;
				for (int l = 0; l < 10; l++) {
					arr = initRandArr(M[i]);
					comparisonsCount = DHeap.DHeapSort(arr, D[j]);
					// System.out.print("M = " + String.format("%6d", M[i]) + "
					// | ");
					// System.out.print("D = " + String.format("%1d", D[j]) + "
					// | ");
					// System.out.println("Count = " + comparisonsCount);
					avg += comparisonsCount;
				}
				System.out.println("The Avg of M = " + M[i] + " and D = " + D[j] + " is : " + avg / 10);
			}

		}
		System.out.println();
	}

	private static void doTest2() {
		int comparisonsCount;
		int[] arr;
		double avg;
		DHeap_Item[] items;
		for (int i = 0; i < X.length; i++) {
			for (int j = 0; j < D.length; j++) {
				avg = 0;
				for (int l = 0; l < 10; l++) {
					comparisonsCount = 0;
					arr = initRandArr(100000);
					items = intArrToDHeapItemArr(arr);
					DHeap heap = new DHeap(D[j], arr.length);
					for (int k = 0; k < arr.length; k++) {
						heap.Insert(items[k]);
					}
					for (int k = 0; k < arr.length; k++) {
						comparisonsCount += heap.Decrease_Key(items[k], X[i]);
					}
//					System.out.print("X = " + String.format("%6d", X[i]) + " | ");
//					System.out.print("D = " + String.format("%1d", D[j]) + " | ");
//					System.out.println("Count = " + comparisonsCount);
					avg += comparisonsCount;
				}
				System.out.println("The Avg of X = " + X[i] + " and D = " + D[j] + " is : " + avg / 10);
			}
		}
	}

	private static int[] initRandArr(int size) {
		int[] arr = new int[size];
		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			arr[i] = rand.nextInt(RAND_MAX);
		}
		return arr;
	}

	private static DHeap_Item[] intArrToDHeapItemArr(int[] arr) {
		DHeap_Item[] items = new DHeap_Item[arr.length];
		for (int i = 0; i < arr.length; i++) {
			items[i] = new DHeap_Item(null, arr[i]);
		}
		return items;
	}

}

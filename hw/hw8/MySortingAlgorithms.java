import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms, 
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }        
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            sort(array, 0, k);
        }

        public static void sort(int[] array, int start, int end) {
            end = Math.min(array.length, end);
            for (int i = start; i < end; i += 1) {
                for (int j = i; j > start; j -= 1) {
                    if (array[j] < array[j-1]) {
                        swap(array, j-1, j);
                    }
                    else
                        break;
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(array.length, k);
            for (int i = 0; i < k; i += 1) {
                int min = i;
                for (int j = i+1; j < k; j += 1) {
                    if (array[min] > array[j])
                        min = j;
                }
            }

        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {            
            k = Math.min(array.length, k);
            int[] x = new int[k];
            mergeSort(array, 0, k, x);
        }

        private void mergeSort(int[] array, int left, int right, int[] combined) {
            int mid = (right + left) / 2;
            if (right - left > 1) {
                mergeSort(array, left, mid, combined);
                mergeSort(array, right, mid, combined);
                merge(array, left, mid, right, combined);
            }
        }

        private void merge(int[] array, int left, int mid, int right, int[] combined) {
            for (int i = left, j = mid; i < mid || j < right;) {
                if ((j == right) || (i < mid && array[i] < array[j])) {
                    combined[i + j - mid] = array[i];
                    i += 1;
                }
                else if ((i == mid) || (j < right && array[i] >= array[j])) {
                    combined[i+j-mid] = array[j];
                    j += 1;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(array.length, k);
            if (k <= 1)
              return;

            int max = getMax(array);
            int[] array2 = new int[max + 1];

            for (int i = 0; i < k; i += 1)
              array2[array[i]] += 1;

            int index = 0;
            for (int i = 0; i < max + 1; i += 1) {
                int end = array2[i] + index;
                if (end != index)
                    Arrays.fill(array, index, end, i);
            }
        }

        private int getMax(int[] array) {
            return 0;
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {

      /** Cannot for the life of me figure this out. */

        @Override
        public void sort(int[] array, int k) {
            int N = Math.min(array.length, k);

            for (int m = N/2; m >= 1; m--) 
                sink(array, m, N);
            
            while (N > 1) {
                exch(array, 1, N);
                N -= 1;
                sink(array, 1, N);
            }
        }

        private static void sink(int[] pq, int m, int N) {
            while (2*m <= N) {
                int j = 2*m;
                if (j < N && less(pq, j, j+1)) j++;
                if (!less(pq, m, j)) break;
                exch(pq, m, j);
                m = j;
            }
        }

        private static boolean less(int[] pq, int i, int j) {
            return pq[i-1] < pq[j-1];
        }

        private static void exch(int[] pq, int i, int j) {
            int swap = pq[i-1];
            pq[i-1] = pq[j-1];
            pq[j-1] = swap;
        }

        private void heapify(int[] array, int k) {

        }

        @Override
        public String toString() {
            return "Heap Sort";
        }        
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(array.length, k);
            quickSort(array, 0, k);
        }

        private void quickSort(int[] array, int start, int end) {
            if (end - start > 5) {
                int pivot = array[0];
                quickSort(array, start, pivot);
                quickSort(array, pivot + 1, end);
            }
            else if (end - start > 1)
                InsertionSort.sort(array, start, end);
        }

        @Override
        public String toString() {
            return "Quicksort";
        }        
    }

    /*
     * Your LSD Sort implementation, treating ints
     * as a sequence of 8 bit characters. Or if you want 
     * to do less bit-hacking, you can treat them as strings
     * of decimal digits.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        /** For an example implementation, see Kevin Wayne and 
          * Bob Sedgewick's Algorithms textbook.
          *
          * http://algs4.cs.princeton.edu/51radix/LSD.java.html
          */
        public void sort(int[] a, int k) {
            // TODO
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * Tricky: Your MSD Sort implementation, treating ints
     * as a string of 8 bit characters.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        /** For an example implementation, see Kevin Wayne and 
          * Bob Sedgewick's Algorithms textbook.
          *
          * http://algs4.cs.princeton.edu/51radix/MSD.java.html
          */
        public void sort(int[] a, int k) {
            // TODO
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    // swap a[i] and a[j]
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}



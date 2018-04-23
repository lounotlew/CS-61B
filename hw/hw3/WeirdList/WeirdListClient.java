/** Functions to sum and increment the elements of a WeirdList. */
class WeirdListClient {
    /** Returns the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        Adder adder = new Adder(n);
        return L.map(adder);
    }

    /** Returns the sum of the elements in L */
    static int sum(WeirdList L) { 
        Summer summer = new Summer();
        L.map(summer);
        return summer.getS();
    }

}

import java.util.Arrays;

public class P3 {

    public static void main(String... ignored) {

    	// System.out.println("The smallest good numeral of " + n + " is " + k);
    }

    boolean isGoodNumeral(int[] number) {
    	for (int i = 0; i < number.length; i += 1) {
    		if (i + 2 > number.length) {
    			return true;
    		}
    		int j = i + 2;
    		if (number[i] == (number[i + 1]))
    			return false;
    		else if (number[i] == (number[i + 2]))
    			return false;
    	}
        return true;
    }

    void findSmallestGood(int[] number) {
    	int smallestGood = number[0];
    	if (!isGoodNumeral(number)) {
    		return;
    	}
    	for (int i = 1; i < number.length; i += 1) {
    		if (smallestGood > number[i]) {
    			smallestGood = number[i];
    		}
    	}

    }
}

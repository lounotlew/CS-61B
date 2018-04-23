import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    static final double DELTA = 1e-3;

    @Test
    public void testNumYears() {
        assertEquals(CompoundInterest.numYears(2015), 0);
        assertEquals(CompoundInterest.numYears(2017), 2);
        assertEquals(CompoundInterest.numYears(30000), 27985);
    }

    @Test
    public void testFutureValue() {
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2017), DELTA);
        assertEquals(7.744, CompoundInterest.futureValue(10, -12, 2017), DELTA);
    }

    @Test
    public void testFutureValueReal() {
        assertEquals(11.803, CompoundInterest.futureValueReal(10, 12, 2017, 3), DELTA);
        assertEquals(8.069, CompoundInterest.futureValueReal(10, -5, 2018, 2), DELTA);
        assertEquals(11.471, CompoundInterest.futureValueReal(12, -4, 2019, -3), DELTA);
    }

    @Test
    public void testTotalSavings() {
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2017, 10), DELTA);
        assertEquals(38117.084, CompoundInterest.totalSavings(6000, 2019, 12), DELTA);
        assertEquals(15065.344, CompoundInterest.totalSavings(4000, 2018, -4), DELTA);
    }

    @Test
    public void testTotalSavingsReal() {
        assertEquals(1602.744 , CompoundInterest.totalSavingsReal(500, 2017, 10, 3), DELTA);
        assertEquals(5019.596 , CompoundInterest.totalSavingsReal(1000, 2018, 12, -3), DELTA);
        assertEquals(4950.701 , CompoundInterest.totalSavingsReal(1500, 2018, -10, 3), DELTA);       
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}

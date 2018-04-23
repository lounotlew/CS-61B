import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(0, CompoundInterest.numYears(2015));
        assertEquals(1, CompoundInterest.numYears(2016));
        assertEquals(30, CompoundInterest.numYears(2045));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(1.1, CompoundInterest.futureValue(1, 10, 2016),
                     tolerance);
        assertEquals(1.21, CompoundInterest.futureValue(1, 10, 2017),
                     tolerance);
        assertEquals(12.1, CompoundInterest.futureValue(10, 10, 2017),
                     tolerance);
        assertEquals(0.97, CompoundInterest.futureValue(1, -3, 2016),
                     tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(1*1.1*0.97,
                     CompoundInterest.futureValueReal(1, 10, 2016, 3),
                     tolerance);
        assertEquals(1*1.1*1.1*0.97*0.97,
                     CompoundInterest.futureValueReal(1, 10, 2017, 3),
                     tolerance);
        assertEquals(10*1.1*1.1*0.97*0.97,
                     CompoundInterest.futureValueReal(10, 10, 2017, 3),
                     tolerance);
        assertEquals(0.97*0.97*0.97*0.97,
                     CompoundInterest.futureValueReal(1, -3, 2017, 3),
                     tolerance);
    }

    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2017, 10),
                     tolerance);
        assertEquals(21472, CompoundInterest.totalSavings(4000, 2018, 20),
                     tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(16550*0.97*0.97,
                     CompoundInterest.totalSavingsReal(5000, 2017, 10, 3),
                     tolerance);
        assertEquals(16550*0.95*0.95,
                     CompoundInterest.totalSavingsReal(5000, 2017, 10, 5),
                     tolerance);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}

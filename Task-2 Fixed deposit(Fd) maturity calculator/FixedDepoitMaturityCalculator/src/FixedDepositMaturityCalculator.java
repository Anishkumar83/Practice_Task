import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class FixedDepositMaturityCalculator {

    private static final int SCALE = 2;

    //A = P × (1 + R/(n×100))^(n×T)
    /*
        P	Principal (deposit amount)
        R	Annual interest rate (%)
        T	Time in years
        n	Compounding frequency (per year)
     */

    //Interest = A − P
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the deposit amount(principal)");
        BigDecimal principal = readBigDecimal(sc);

        System.out.println("Enter the annual interest rate");
        BigDecimal annualRate= readBigDecimal(sc);

        System.out.println("Enter the tenure in years");
        int years = readInt(sc);

        System.out.println("Enter the compounding per year");
        int compoundPerYear = readInt(sc);

        BigDecimal ratePerPeriod= annualRate.
                divide(BigDecimal.valueOf(compoundPerYear * 100L),
                        10, RoundingMode.HALF_UP);

        int totalPeriod = compoundPerYear * years;

        BigDecimal maturityAmount= principal.multiply(
                BigDecimal.ONE.add(ratePerPeriod)
                        .pow(totalPeriod, MathContext.DECIMAL64)
        ).setScale(SCALE,RoundingMode.HALF_UP)
                ;

        BigDecimal interestEarned = maturityAmount.subtract(principal);

        System.out.println("----- Fixed Deposit Details -----");
        System.out.println("Principal        :" + principal);
        System.out.println("Maturity Amount  :" + maturityAmount);
        System.out.println("Interest Earned  :" + interestEarned);


    }

    static BigDecimal readBigDecimal(Scanner sc){
        String input = sc.nextLine().trim();
        if(!input.matches("\\d+(\\.\\d+)?")){
            throw new IllegalArgumentException("Enter numeric input");
        }
        return new BigDecimal(input);
    }

    static int readInt(Scanner sc){
        String input = sc.nextLine().trim();
        if(!input.matches("\\d+")){
            throw new IllegalArgumentException("Enter numeric input");
        }
        return Integer.parseInt(input);
    }
}

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class LoanEmiCalculator {

    private static final int SCALE = 2;  // final 2-decimal financial rounding

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {


            System.out.print("Enter Loan Amount (Principal): ");
            BigDecimal principal = readBigDecimal(sc);

            System.out.print("Enter Annual Interest Rate (%): ");
            BigDecimal annualRate = readBigDecimal(sc);

            System.out.print("Enter Loan Tenure (Years): ");
            int years = readInt(sc);


            if (principal.compareTo(BigDecimal.ZERO) <= 0 ||
                    annualRate.compareTo(BigDecimal.ZERO) <= 0 ||
                    years <= 0) {
                System.out.println("Invalid inputs. All values must be greater than zero.");
                return;
            }


            if (principal.compareTo(new BigDecimal("100000000")) > 0) {
                System.out.println("Principal too large. Enter below 10 crore.");
                return;
            }

            if (annualRate.compareTo(new BigDecimal("50")) > 0) {
                System.out.println("Interest rate too high. Enter below 50%.");
                return;
            }

            if (years > 40) {
                System.out.println("Tenure too long. Enter ≤ 40 years.");
                return;
            }

            calculateEmi(principal, annualRate, years);

        } catch (Exception e) {
            System.out.println("Invalid input format. Please enter numeric values only.");
        }
    }


    private static BigDecimal readBigDecimal(Scanner sc) {
        String input = sc.nextLine().trim();
        if (!input.matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("Invalid numeric input");
        }
        return new BigDecimal(input);
    }


    private static int readInt(Scanner sc) {
        String input = sc.nextLine().trim();
        if (!input.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid integer input");
        }
        return Integer.parseInt(input);
    }


    static void calculateEmi(BigDecimal principal, BigDecimal annualRate, int years) {

        BigDecimal monthlyRate =
                annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

        int totalMonths = years * 12;

        BigDecimal emi;


        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            emi = principal
                    .divide(BigDecimal.valueOf(totalMonths), SCALE, RoundingMode.HALF_UP);
        } else {
            BigDecimal onePlusRPowerN =
                    BigDecimal.ONE.add(monthlyRate)
                            .pow(totalMonths, MathContext.DECIMAL64);

            BigDecimal denominator =
                    onePlusRPowerN.subtract(BigDecimal.ONE);

            if (denominator.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(" EMI cannot be calculated due to invalid parameters.");
                return;
            }

            emi = principal.multiply(monthlyRate).multiply(onePlusRPowerN)
                    .divide(denominator, SCALE, RoundingMode.HALF_UP);
        }


        if (emi.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println(" EMI calculation failed. Check loan parameters.");
            return;
        }

        System.out.println("\n+-------+--------------+--------------+--------------+----------------+");
        System.out.println("| Month |     EMI      |   Interest   |  Principal   |    Balance     |");
        System.out.println("+-------+--------------+--------------+--------------+----------------+");

        BigDecimal balance = principal;

        for (int month = 1; month <= totalMonths; month++) {

            BigDecimal interest = balance.multiply(monthlyRate)
                    .setScale(SCALE, RoundingMode.HALF_UP);

            BigDecimal principalPaid = emi.subtract(interest)
                    .setScale(SCALE, RoundingMode.HALF_UP);


            if (month == totalMonths) {

                principalPaid = balance;
                emi = interest.add(balance).setScale(SCALE, RoundingMode.HALF_UP);
                balance = BigDecimal.ZERO;

                printRow(month, emi, interest, principalPaid, balance);
                break;
            }


            balance = balance.subtract(principalPaid)
                    .setScale(SCALE, RoundingMode.HALF_UP);

            printRow(month, emi, interest, principalPaid, balance);
        }

        System.out.println("+-------+--------------+--------------+--------------+----------------+");
    }


    private static void printRow(int month, BigDecimal emi,
                                 BigDecimal interest,
                                 BigDecimal principalPaid,
                                 BigDecimal balance) {

        System.out.printf("| %5d | %12.2f | %12.2f | %12.2f | %14.2f |\n",
                month, emi, interest, principalPaid, balance);
    }
}

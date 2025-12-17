import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class LoanEmiCalculator {
    private static final int SCALE = 2;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the loan amount(Principal): ");
        BigDecimal principal =sc.nextBigDecimal();

        System.out.println("Enter Annual Interest Rate(%): ");
        BigDecimal annualRate= sc.nextBigDecimal();

        System.out.println("Enter Loan Tenure(years)");
        int years = sc.nextInt();

        if(principal.compareTo(BigDecimal.ZERO) <=0 ||
                annualRate.compareTo(BigDecimal.ZERO) <=0 ||
                years <=0){
            System.out.println("Invalid inputs the values should be greater than zero");
        }

        calculateEmi(principal,annualRate,years);
        sc.close();
    }

    static void calculateEmi(BigDecimal principal, BigDecimal annualRate, int years){
        //1.convert annual interest into monthly interest rate
        BigDecimal monthlyRate = annualRate.
                divide(BigDecimal.valueOf(12*100), 10, RoundingMode.HALF_UP);

        //2.loan tenure to months
        int totalMonths=years*12;

        //EMI = P * r * (1+r)^n/ (1+r)^n -1

        BigDecimal onePlusRPowerN = BigDecimal.ONE.add(monthlyRate).pow(totalMonths, MathContext.DECIMAL64);

        BigDecimal emi =principal.multiply(monthlyRate).multiply(onePlusRPowerN).
                divide(onePlusRPowerN.subtract(BigDecimal.ONE),
                        SCALE
                        ,RoundingMode.HALF_UP);

        BigDecimal balance = principal;

        System.out.println("Month | EMI | Interest | Principal | Balance");
        System.out.println("------------------------------------------------");

        for(int month=1; month<=totalMonths; month++){

            //interest = outstandingBalance * monthlyRate

            BigDecimal interest = balance.multiply(monthlyRate).setScale(SCALE,RoundingMode.HALF_UP);

            //Monthly principal paid
            //principalPaid = totalEMI - interest;
            BigDecimal principalPaid = emi.subtract(interest).setScale(SCALE,RoundingMode.HALF_UP);

            //outstanding balance after paying emi
            //outstandingBalance=outstandingBalance - principalPaid;
            balance = balance.subtract(principalPaid).setScale(SCALE,RoundingMode.HALF_UP);


            if(balance.compareTo(BigDecimal.ZERO) <0){
                balance= BigDecimal.ZERO;
            }

            System.out.printf("%5d | %8.2f | %8.2f | %9.2f | %9.2f%n",
                    month,
                    emi,
                    interest,
                    principalPaid,
                    balance);
        }
    }
}

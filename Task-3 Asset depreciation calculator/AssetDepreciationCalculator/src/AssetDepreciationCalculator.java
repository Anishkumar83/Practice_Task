import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class AssetDepreciationCalculator {

    //Depreciation per year = (Cost × Rate) / 100
    //Current value = Cost − (Depreciation × Years)

    public static final int SCALE = 2;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the asset cost");
        BigDecimal assetCost=sc.nextBigDecimal();

        System.out.println("Enter the depreciation rate");
        BigDecimal depreciationRate = sc.nextBigDecimal();

        System.out.println("Enter the year: ");
        int years = sc.nextInt();

        BigDecimal depreciationPerYear = assetCost.multiply(depreciationRate).
                divide(BigDecimal.valueOf(100), MathContext.DECIMAL64).setScale(SCALE,RoundingMode.HALF_UP);

        BigDecimal currentValue = assetCost.subtract(depreciationPerYear.multiply(BigDecimal.valueOf(years)))
                        .setScale(SCALE,RoundingMode.HALF_UP);

        System.out.println("Depreciation per year: " +depreciationPerYear);

        System.out.println("Current value "+currentValue);

    }
}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class NumbersToWords
{
    private static final String[] currencyNames = {
            "قرش", "قروش"
    };

    private static final String[] tensNames = {
            "", " و عشرة", "و عشرون ", "و ثلاثون", "و أربعون", "و خمسون", "و ستون",
            "و سبعون", " و ثمانون", " و تسعون"
    };

    private static final String[] numNames = {
            "", " واحد", " أثنان", " ثلاثة", " أربعة", " خمسة", " ست", " سبعة", " ثمانية", "   تسعة",
            " عشرة", " احدى عشرة", " اثنا عشر", " ثلاثة عشر", " أربعة عشر", " خمسة عشر", " ست عشر",
            " سبعة عشر", " ثمانية عشر", " تسعة عشر"
    };
/*
    private static final String[] hundredNames = {
            "", " مائة", " مائتان", " تلاث مائة", " أربع مائة", " خمسة مائة", " ست مائة", " سبع مائة", " ثماني مائة", "   تسع مائة"

    };
    private static final String[] ThousandNames = {
            "", " ألف", " ألفان", " ثلاثة ألاف", " أربعة ألاف", " خمسة ألاف", " ست ألاف", " سبعة ألاف", " ثمانية ألاف", "   تسعة ألاف",
            " عشرة ألاف", " احدى عشرة", " اثنا عشر", " ثلاثة عشر", " أربعة عشر", " خمسة عشر", " ست عشر",
            " سبعة عشر", " ثمانية عشر", " تسعة عشر"
    };
*/

    private NumbersToWords() {
    }

    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 20) {
            soFar = numNames[number % 100];
            number /= 100;
        } else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = soFar + tensNames[number % 10];
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNames[number] + " مائة " + soFar;
    }

    public static String convert(long number) {
        if (number < 0 || number > 999_999_999_999L) {
            return "الرقم خارج النطاق المسموح.";
        }

        if (number == 0) {
            return "صفر";
        }

        String paddedNumber = new DecimalFormat("000000000000").format(number);

        int billions = Integer.parseInt(paddedNumber.substring(0, 3));
        int millions = Integer.parseInt(paddedNumber.substring(3, 6));
        int hundredThousands = Integer.parseInt(paddedNumber.substring(6, 9));
        int thousands = Integer.parseInt(paddedNumber.substring(9, 12));

        String billionsWords = (billions == 0) ? "" : convertLessThanOneThousand(billions) + " مليار ";
        String millionsWords = (millions == 0) ? "" : convertLessThanOneThousand(millions) + " مليون ";
        String hundredThousandsWords = (hundredThousands == 0) ? "" :
                (hundredThousands == 1) ? "ألف " : convertLessThanOneThousand(hundredThousands) + " ألف  ";
        String thousandsWords = convertLessThanOneThousand(thousands);

        double decimalPart = (number % 1) * 100; // قيمة القروش

        String decimalPartWords = "";
        if (decimalPart > 0) {
            int decimalValue = (int) decimalPart;
            String currencyName = currencyNames[0]; // القرش
            if (decimalValue > 1) {
                currencyName = currencyNames[1]; // القروش
            }
            decimalPartWords = " و " + convertLessThanOneThousand(decimalValue) + " " + currencyName;
            // للتعبير عن الأرقام العشرية بشكل صحيح
            String decimalString = String.valueOf(decimalPart);
            int decimalLength = decimalString.length();
            decimalString = decimalString.substring(2, decimalLength);
            decimalPartWords += " و " + decimalString + " من " + currencyName;
        }


        String result = billionsWords + millionsWords + hundredThousandsWords + thousandsWords + decimalPartWords;

        return result.trim();
    }

    public static void main(String[] args) {
        System.out.print("ادخل الرقم المراد تحويله: ");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            double number = Double.parseDouble(reader.readLine());
            String convertedNumber = NumbersToWords.convert((long) number);
            String currency = "جنيهًا";
            if (number % 1 != 0) {
                currency = "جنيهًا و " +NumbersToWords.convert ((int)((number % 1) * 100)) + " قرشًا";
            }
            System.out.println(convertedNumber + " " + currency + " فقط لا غير");
        } catch (IOException | NumberFormatException e) {
            System.err.println("خطأ في الإدخال: " + e.getMessage());
        }
    }

}

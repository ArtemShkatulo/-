import java.util.*;
import java.util.stream.Collectors;
import static java.lang.Integer.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение в одном из следующих вариантах:");
        System.out.println("a + b или a - b или a * b или a / b, где ");
        System.out.println("a и b любые арабские или римские числа");
        System.out.println(calc(input.nextLine()));
    }
    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }
    static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " не в диапазоне (0,4000]");
        }
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }
        return sb.toString();
    }
    static int romanToArabic(String input) {
        String romanNumeral = input;
        int result = 0;
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        int i = 0;
        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }
        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException("Неправильный формат числа: " + input);
        }
        return result;
    }
    static String findOperation (String input){
        char [] mathOperations = new char[]{'-', '+', '*', '/'};
        char operation = 0;
        int scoreMathOperations = 0;

        for (int i = 0; i < input.length(); i++)
            for (int y = 0; y < mathOperations.length; y++){
                if ((input.charAt(i)) == mathOperations[y]) {
                    operation = mathOperations[y];
                    scoreMathOperations++;
                }
            }
        if (scoreMathOperations == 1 )
            return(String.valueOf(operation));
        else
            throw new IllegalArgumentException("Формат математической операции не удовлетворяет заданию");
    }
    public static String calc(String input){
        String [] romanSymbols = new String[]{"I", "V", "X", "L", "C", "D", "M"};
        String result = "";
        int res = 0;
        String currentOperation = null;
        String [] operands = new String[2];
        String a, b;
        boolean aIsRoman = false;
        boolean bIsRoman = false;

        currentOperation = findOperation(input);
        if (currentOperation != null)
            operands = input.split("\\" + currentOperation);
        else
            throw new IllegalArgumentException("Строка не является математической операцией");

        a = operands[0].trim();
        b = operands[1].trim();

        for (String i: romanSymbols) {
            if (a.contains(i))
                aIsRoman = true;
            if (b.contains(i))
                bIsRoman = true;
        }
        if (aIsRoman != bIsRoman)
            throw new IllegalArgumentException("Используются разные системы счисления");
        else if (aIsRoman && bIsRoman) {
            a = Integer.toString(romanToArabic(a));
            b = Integer.toString(romanToArabic(b));
        }

        try {
            switch (currentOperation) {
                case "+":
                    res = parseInt(a) + parseInt(b);
                    break;
                case "-":
                    res = parseInt(a) - parseInt(b);
                    break;
                case "*":
                    res = parseInt(a) * parseInt(b);
                    break;
                case "/":
                    res = Math.round(parseInt(a) + parseInt(b));
                    break;
                default:
                    ;
                    break;
            }
        }
        catch (NumberFormatException e) {
           throw new NumberFormatException("Операнды не являются числами");
        }

        if (aIsRoman)
            if(res >= 1)
                result = arabicToRoman(res);
            else
                throw new IllegalArgumentException("В римской системе нет отрицательных чисел");
        else
            result = Integer.toString(res);

        return result;
    }
}
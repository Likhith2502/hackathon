import java.io.FileReader;
import java.util.*;
import java.math.BigInteger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PC{

    // Converts a string value in given base to decimal (supports alphanumeric)
    private static BigInteger convertToDecimal(String value, int base) {
        BigInteger result = BigInteger.ZERO;
        value = value.toUpperCase(); // handle both lowercase and uppercase
        BigInteger bigBase = BigInteger.valueOf(base);

        for (char c : value.toCharArray()) {
            int digit;
            if (Character.isDigit(c)) {
                digit = c - '0';          // 0-9
            } else {
                digit = c - 'A' + 10;     // A=10, B=11, ... Z=35
            }

            if (digit >= base) {
                throw new IllegalArgumentException(
                    "Invalid digit '" + c + "' for base " + base
                );
            }

            result = result.multiply(bigBase).add(BigInteger.valueOf(digit));
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            // Parse JSON file
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("input2.json"));

            // Extract degree (n) and minimum roots (k)
            JSONObject keys = (JSONObject) jsonObject.get("keys");
            int n = Integer.parseInt(keys.get("n").toString());
            int k = Integer.parseInt(keys.get("k").toString());

            // Collect roots
            List<BigInteger> roots = new ArrayList<>();
            for (Object key : jsonObject.keySet()) {
                String keyStr = key.toString();
                if (!keyStr.equals("keys")) {
                    JSONObject rootObj = (JSONObject) jsonObject.get(keyStr);
                    int base = Integer.parseInt(rootObj.get("base").toString());
                    String value = rootObj.get("value").toString();
                    roots.add(convertToDecimal(value, base));
                }
            }

            // Sort roots
            Collections.sort(roots);

            // Ensure at least k roots
            if (roots.size() < k) {
                System.out.println("Not enough roots available.");
                return;
            }

            // Pick exactly n roots (or first n if more are present)
            List<BigInteger> chosenRoots = roots.subList(0, Math.min(n, roots.size()));

            // Compute constant term = (-1)^n * product of chosen roots
            BigInteger constant = BigInteger.ONE;
            for (BigInteger root : chosenRoots) {
                constant = constant.multiply(root);
            }
            if (n % 2 != 0) constant = constant.negate();  // apply sign if degree is odd

           // System.out.println("Chosen Roots: " + chosenRoots);
            System.out.println("Constant Term of Polynomial: " + constant);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Decoding function for values in different bases
    public static BigInteger decode(String val, int base) {
        return new BigInteger(val, base);
    }

    // Lagrange interpolation to find f(0)
    public static BigInteger interpolate(List<Integer> x, List<BigInteger> y, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger term = y.get(i);

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger num = BigInteger.valueOf(-x.get(j)); // 0 - x[j]
                    BigInteger denom = BigInteger.valueOf(x.get(i) - x.get(j)); // x[i] - x[j]
                    term = term.multiply(num).divide(denom);
                }
            }

            result = result.add(term);
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("test_case.json"));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line.trim());
            }
            reader.close();

            String json = sb.toString();

            // Parse n and k values
            int n = Integer.parseInt(json.split("\"n\":")[1].split(",")[0].trim());
            int k = Integer.parseInt(json.split("\"k\":")[1].split("}")[0].trim());

            if (k > n) return;

            List<Integer> x = new ArrayList<>();
            List<BigInteger> y = new ArrayList<>();

            for (int i = 1; i < n; i++) {
                String index = "\"" + i + "\":";
                int startIdx = json.indexOf(index) + index.length();
                int endIdx = json.indexOf("}", startIdx);

                String point = json.substring(startIdx, endIdx + 1);

                String baseStr = point.split("\"base\":")[1].split(",")[0].trim();
                if (baseStr.isEmpty()) continue;

                baseStr = baseStr.replaceAll("\"", "").trim();
                int base = Integer.parseInt(baseStr);

                String valStr = point.split("\"value\":")[1].split("\"")[1].trim();
                if (valStr.isEmpty()) continue;

                valStr = valStr.replaceAll("\"", "").trim();

                x.add(i);
                y.add(decode(valStr, base));
            }

            if (x.size() < k || y.size() < k) return;

            BigInteger result = interpolate(x.subList(0, k), y.subList(0, k), k);
            System.out.println("Constant term: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

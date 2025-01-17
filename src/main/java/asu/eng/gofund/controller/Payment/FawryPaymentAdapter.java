package asu.eng.gofund.controller.Payment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FawryPaymentAdapter implements IPaymentAdapter {
    private static final String apiUrl = "https://atfawry.fawrystaging.com/ECommerceWeb/Fawry/payments/charge";

    @Override
    public boolean executePayment(Map<String, String> paymentInfo, double amount) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("fawryCode", paymentInfo.get("fawryCode"));
            params.put("fawryAccount", paymentInfo.get("fawryAccount"));
            params.put("fawryPassword", paymentInfo.get("fawryPassword"));
            params.put("amount", String.valueOf(amount));
            params.put("currency", paymentInfo.getOrDefault("currency", "EGP"));
            params.put("description", "Fawry Payment");

            String response = sendPostRequest(apiUrl, params.toString());

            if (response.contains("\"status\":\"success\"")) {
                return true;
            } else {
                System.out.println("Payment failed with response: " + response);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String sendPostRequest(String urlString, String jsonInputString) throws Exception {
        HttpURLConnection connection = getHttpURLConnection(urlString, jsonInputString);

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream(), "utf-8")) {
            char[] buffer = new char[1024];
            StringBuilder response = new StringBuilder();
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                response.append(buffer, 0, bytesRead);
            }
            return response.toString();
        }
    }

    private static HttpURLConnection getHttpURLConnection(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        return connection;
    }
}

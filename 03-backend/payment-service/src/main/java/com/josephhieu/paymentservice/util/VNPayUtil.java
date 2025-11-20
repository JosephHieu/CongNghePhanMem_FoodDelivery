package com.josephhieu.paymentservice.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VNPayUtil {

    /**
     * Hàm tạo chuỗi hash SHA512.
     * @param key Secret Key được cung cấp bởi VNPAY.
     * @param data Chuỗi dữ liệu cần hash.
     * @return Chuỗi hash hex.
     */
    public static String hmacSHA512(final String key, final String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate HMAC", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte));
        }
        return sb.toString();
    }

    /**
     * Hàm nối các tham số thành chuỗi query string.
     * LƯU Ý QUAN TRỌNG: Hàm này KHÔNG SẮP XẾP. Nó chỉ nối chuỗi.
     * Việc sắp xếp (theo A-Z) được thực hiện bằng cách truyền vào một Map đã được sắp xếp (ví dụ: TreeMap).
     * @param params Map chứa các tham số.
     * @return Chuỗi query string đã được URL-encode.
     */
    public static String buildQuery(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // Key và Value đều cần được URLEncode
                sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
                sb.append('=');
                sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
                sb.append('&');
            }
        } catch (Exception e) {
            // Nên xử lý ngoại lệ encoding tốt hơn trong ứng dụng thực tế
            throw new RuntimeException(e);
        }

        // Xóa ký tự '&' cuối cùng
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Hàm tạo chuỗi để Hash (bao gồm Secret Key) từ Map tham số.
     * Đây là hàm quan trọng nhất để sửa lỗi "Sai chữ ký".
     *
     * @param fields Map chứa tất cả tham số VNPAY (trừ vnp_SecureHash).
     * @param secretKey Hash Secret Key của Merchant.
     * @return Chuỗi dữ liệu cuối cùng dùng để hash.
     */
    public static String hashAllFields(Map<String, String> fields, String secretKey) {
        // 1. Sắp xếp các tham số theo thứ tự A-Z bằng TreeMap
        Map<String, String> sortedFields = new TreeMap<>(fields);

        // 2. Nối chuỗi
        StringBuilder sb = new StringBuilder();
        sb.append(secretKey); // Thêm Secret Key vào đầu chuỗi (theo tài liệu VNPAY)

        for (Map.Entry<String, String> entry : sortedFields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            // Bỏ qua tham số Secure Hash nếu nó còn sót lại
            if (fieldValue != null && fieldValue.length() > 0) {
                sb.append('&');
                sb.append(fieldName);
                sb.append('=');
                // Giá trị KHÔNG được URLEncode khi tạo chuỗi Hash (chỉ URLEncode khi tạo URL)
                sb.append(fieldValue);
            }
        }
        return sb.toString();
    }
}
package com.josephhieu.paymentservice.service;

import com.josephhieu.paymentservice.client.OrderClient;
import com.josephhieu.paymentservice.config.VNPayConfig;
import com.josephhieu.paymentservice.entity.Payment;
import com.josephhieu.paymentservice.repository.PaymentRepository;
import com.josephhieu.paymentservice.util.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VNPayConfig vnpConfig;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private PaymentRepository paymentRepo;

    public Map<String, Object> createCOD(String orderId, Long amount) {

        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .status("COD_PENDING")
                .method("COD")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        paymentRepo.save(payment);

        // Cập nhật order-service
        orderClient.updateStatus(orderId, Map.of("status", "PENDING_DELIVERY"));

        return Map.of(
                "message", "COD created successfully",
                "paymentId", payment.getId(),
                "status", "COD_PENDING"
        );
    }

    public String createPaymentUrl(Long amount, String orderId) {

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnpConfig.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount * 100));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnpConfig.getReturnUrl());
        params.put("vnp_IpAddr", "127.0.0.1");

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();

        params.put("vnp_CreateDate", fmt.format(cal.getTime()));

        cal.add(Calendar.MINUTE, 15);
        params.put("vnp_ExpireDate", fmt.format(cal.getTime()));

        // sort A-Z
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (value != null && value.length() > 0) {

                // hash data (raw)
                hashData.append(key).append("=").append(value);

                // query string (encoded)
                query.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
                query.append("=");
                query.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }

            if (i < keys.size() - 1) {
                hashData.append("&");
                query.append("&");
            }
        }

        String secureHash = VNPayUtil.hmacSHA512(vnpConfig.getHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return vnpConfig.getPayUrl() + "?" + query.toString();
    }

    public void handleIpnCallback(String orderId, String responseCode, String transactionNo) {

        Payment payment = paymentRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if ("00".equals(responseCode)) {
            payment.setStatus("SUCCESS");
            payment.setTransactionNo(transactionNo);
            payment.setUpdatedAt(new Date());
            paymentRepo.save(payment);

            // notify order service
            orderClient.markOrderPaid(
                    orderId,
                    Map.of("paymentId", transactionNo)
            );

        } else {
            payment.setStatus("FAILED");
            paymentRepo.save(payment);
        }
    }
}

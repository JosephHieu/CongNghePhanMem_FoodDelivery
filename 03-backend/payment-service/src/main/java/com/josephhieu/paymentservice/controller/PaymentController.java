package com.josephhieu.paymentservice.controller;

import com.josephhieu.paymentservice.client.OrderClient;
import com.josephhieu.paymentservice.config.VNPayConfig;
import com.josephhieu.paymentservice.entity.Payment;
import com.josephhieu.paymentservice.repository.PaymentRepository;
import com.josephhieu.paymentservice.service.PaymentService;
import com.josephhieu.paymentservice.util.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final VNPayConfig vnpConfig;
    private final OrderClient orderClient;
    private final PaymentRepository paymentRepo;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> body) {

        Long amount = Long.parseLong(body.get("amount").toString());
        String orderId = body.get("orderId").toString();
        String method = body.get("method").toString(); // NEW

        if (method.equalsIgnoreCase("COD")) {
            return ResponseEntity.ok(paymentService.createCOD(orderId, amount));
        }

        // default = VNPAY
        String paymentUrl = paymentService.createPaymentUrl(amount, orderId);

        return ResponseEntity.ok(Map.of(
                "paymentUrl", paymentUrl
        ));
    }

    @PostMapping("/vnpay-ipn")
    public ResponseEntity<?> vnpIpnCallback(@RequestParam Map<String, String> params) {

        String vnpSecureHash = params.get("vnp_SecureHash");

        // Remove hash fields for verify
        Map<String, String> fields = new HashMap<>(params);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        // Sort A-Z
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder signData = new StringBuilder();
        for (String key : fieldNames) {
            signData.append(key).append("=").append(fields.get(key)).append("&");
        }
        signData.deleteCharAt(signData.length() - 1);

        // Calculate hash
        String calculatedHash = VNPayUtil.hmacSHA512(
                vnpConfig.getHashSecret(),
                signData.toString()
        );

        if (!calculatedHash.equals(vnpSecureHash)) {
            return ResponseEntity.badRequest().body("INVALID HASH");
        }

        String orderId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionNo = params.get("vnp_TransactionNo");

        // Update payment
        paymentService.handleIpnCallback(orderId, responseCode, transactionNo);

        return ResponseEntity.ok("OK");
    }

    @PostMapping("/fake-vnpay-success")
    public ResponseEntity<?> fakeVnpaySuccess(@RequestBody Map<String, String> body) {

        String orderId = body.get("orderId");
        if (orderId == null) {
            return ResponseEntity.badRequest().body("Missing orderId");
        }

        // Fake transaction number
        String fakeTransactionNo = "VNPAY_" + System.currentTimeMillis();

        // Gọi sang order-service để cập nhật đơn hàng thành PAID
        orderClient.markOrderPaid(orderId, Map.of("paymentId", fakeTransactionNo));

        return ResponseEntity.ok(Map.of(
                "message", "VNPay payment success",
                "orderId", orderId,
                "paymentId", fakeTransactionNo
        ));
    }
}

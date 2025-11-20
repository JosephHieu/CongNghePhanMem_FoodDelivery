package com.josephhieu.paymentservice.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;


@Document(collection = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    private String id;
    private String orderId;
    private Long amount;
    private String method;
    private String status; // PENDING, SUCCESS, FAILED
    private String transactionNo;

    private Date createdAt;
    private Date updatedAt;
}
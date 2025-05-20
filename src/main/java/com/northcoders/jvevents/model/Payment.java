package com.northcoders.jvevents.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chargeId;

    private Integer amount;

    private String currency;

    private String email;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}


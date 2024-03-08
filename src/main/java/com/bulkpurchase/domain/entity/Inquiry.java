package com.bulkpurchase.domain.entity;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.inquiry.InquiryStatus;
import com.bulkpurchase.domain.enums.inquiry.InquiryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Inquiries")
@Getter
@Setter
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status = InquiryStatus.PENDING;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String inquiryContent;

    @Column(columnDefinition = "TEXT")
    private String responseContent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime inquiryDate = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime responseDate;
}
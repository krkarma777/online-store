package com.bulkpurchase.domain.entity.review;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.FeedbackType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ReviewFeedback")
@Getter
@Setter
public class ReviewFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReviewFeedbackID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewID", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackType feedbackType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date feedbackDate;
}

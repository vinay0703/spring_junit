package com.example.junit.entity;

import lombok.*;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;

@Entity
@Table(name = "Books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String summary;

    @NonNull
    private int rating;
}

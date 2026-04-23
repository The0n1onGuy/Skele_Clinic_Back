package com.nexuscore.models.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "http_status_codes")
@Getter
@Setter
public class HttpStatusCode {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private int code;

        @Column(nullable = false)
        private String name;

        private String description;
}
package com.nexuscoreserver.models.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "audit_logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuditLogsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String concept_audit;

    @Column(nullable = false)
    private String user_blamed;

    @Column(nullable = false)
    private String date_ocurrence;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel status;
}

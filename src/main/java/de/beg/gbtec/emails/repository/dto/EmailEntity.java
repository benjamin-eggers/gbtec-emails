package de.beg.gbtec.emails.repository.dto;

import de.beg.gbtec.emails.model.EmailStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

import static de.beg.gbtec.emails.util.DateTimeUtil.getCurrentDateTimeUTC;

@Getter
@Setter
@Entity
@Table(name = "email")
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "sender")
    private String from;

    @Type(JsonType.class)
    @Column(name = "recipient", columnDefinition = "jsonb")
    private List<String> to;

    @Type(JsonType.class)
    @Column(name = "cc", columnDefinition = "jsonb")
    private List<String> cc;

    @Type(JsonType.class)
    @Column(name = "bcc", columnDefinition = "jsonb")
    private List<String> bcc;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailStatus state;

    @PrePersist
    public void prePersist() {
        created = getCurrentDateTimeUTC();
    }

    @PreUpdate
    public void preUpdate() {
        updated = getCurrentDateTimeUTC();
    }

}

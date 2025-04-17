package de.beg.gbtec.emails.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private List<Recipient> to;

    @Column(name = "cc")
    private List<Recipient> cc;

    @Column(name = "bcc")
    private List<Recipient> bcc;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailStatus state;

}

package de.beg.gbtec.emails;

import de.beg.gbtec.emails.model.EmailStatus;
import de.beg.gbtec.emails.repository.dto.EmailEntity;

import java.util.List;

public final class EmailEntityBuilder {
    private String from;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;
    private EmailStatus state;

    private EmailEntityBuilder() {
    }

    public static EmailEntityBuilder anEmailEntity() {
        return new EmailEntityBuilder();
    }

    public EmailEntityBuilder from(String from) {
        this.from = from;
        return this;
    }

    public EmailEntityBuilder to(List<String> to) {
        this.to = to;
        return this;
    }

    public EmailEntityBuilder cc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public EmailEntityBuilder bcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public EmailEntityBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailEntityBuilder body(String body) {
        this.body = body;
        return this;
    }

    public EmailEntityBuilder state(EmailStatus state) {
        this.state = state;
        return this;
    }

    public EmailEntity build() {
        var emailEntity = new EmailEntity();
        emailEntity.setFrom(from);
        emailEntity.setTo(to);
        emailEntity.setCc(cc);
        emailEntity.setBcc(bcc);
        emailEntity.setSubject(subject);
        emailEntity.setBody(body);
        emailEntity.setState(state);
        return emailEntity;
    }
}

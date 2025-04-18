package de.beg.gbtec.emails.repository;

import de.beg.gbtec.emails.repository.dto.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
}

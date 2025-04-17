package de.beg.gbtec.emails.repository;

import de.beg.gbtec.emails.model.EmailEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmailRepository extends PagingAndSortingRepository<EmailEntity, Long> {
}

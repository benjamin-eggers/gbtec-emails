package de.beg.gbtec.emails.repository;

import de.beg.gbtec.emails.repository.dto.EmailEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

    @Query(
            value = """
                    SELECT *
                    FROM email
                    WHERE (sender = ?1
                        OR recipient @> to_jsonb((?1)::text)
                        OR cc @> to_jsonb((?1)::text)
                        OR bcc @> to_jsonb((?1)::text))
                        AND state != 'SPAM'
                    LIMIT ?2
                    """,
            nativeQuery = true
    )
    Slice<EmailEntity> findEmailEntitiesContainingAddress(String emailAddress, int limit);

}

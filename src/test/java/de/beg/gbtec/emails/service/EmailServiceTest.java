package de.beg.gbtec.emails.service;

import de.beg.gbtec.emails.repository.EmailRepository;
import de.beg.gbtec.emails.repository.dto.EmailEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Set;

import static de.beg.gbtec.emails.EmailEntityBuilder.anEmailEntity;
import static de.beg.gbtec.emails.RandomFactory.randomEmail;
import static de.beg.gbtec.emails.RandomFactory.randomLong;
import static de.beg.gbtec.emails.model.EmailStatus.DRAFT;
import static de.beg.gbtec.emails.model.EmailStatus.SENT;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    private final int knownSpamQueryLimit = 2;
    private final EmailRepository emailRepository = mock(EmailRepository.class);
    private final EmailService emailService = new EmailService(emailRepository, Set.of(), knownSpamQueryLimit);

    @Test
    void markEmailsAsSpam_shouldReturnIfNoElementsExist() {
        var spamAddress = randomEmail();

        when(emailRepository.findEmailEntitiesContainingAddress(spamAddress, knownSpamQueryLimit))
                .thenReturn(new SliceImpl<>(
                        List.of(),
                        Pageable.unpaged(),
                        false
                ));

        emailService.markEmailsAsSpam(spamAddress);

        verify(emailRepository, times(1))
                .findEmailEntitiesContainingAddress(spamAddress, knownSpamQueryLimit);
        verify(emailRepository, times(0))
                .save(any());
        verifyNoMoreInteractions(emailRepository);
    }

    @Test
    void markEmailsAsSpam_shouldReturnIfNoFurtherElementsExist() {
        var spamAddress = randomEmail();

        when(emailRepository.findEmailEntitiesContainingAddress(spamAddress, knownSpamQueryLimit))
                .thenReturn(generateSlice(spamAddress, true))
                .thenReturn(generateSlice(spamAddress, true))
                .thenReturn(generateSlice(spamAddress, false));

        emailService.markEmailsAsSpam(spamAddress);

        verify(emailRepository, times(3))
                .findEmailEntitiesContainingAddress(spamAddress, knownSpamQueryLimit);
        verify(emailRepository, times(6))
                .save(any());
        verifyNoMoreInteractions(emailRepository);
    }

    private Slice<EmailEntity> generateSlice(
            String spamAddress,
            boolean hasNext
    ) {
        return new SliceImpl<>(
                List.of(
                        anEmailEntity()
                                .id(randomLong())
                                .from(spamAddress)
                                .state(DRAFT)
                                .build(),
                        anEmailEntity()
                                .id(randomLong())
                                .from(spamAddress)
                                .state(SENT)
                                .build()
                ),
                Pageable.unpaged(),
                hasNext
        );
    }

}
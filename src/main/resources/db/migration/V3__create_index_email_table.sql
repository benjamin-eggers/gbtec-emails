CREATE INDEX IF NOT EXISTS email_sender_idx ON email (sender);
CREATE INDEX IF NOT EXISTS email_state_idx ON email (state);
CREATE INDEX IF NOT EXISTS email_recipient_gin_idx ON email USING gin (recipient);
CREATE INDEX IF NOT EXISTS email_cc_gin_idx ON email USING gin (cc);
CREATE INDEX IF NOT EXISTS email_bcc_gin_idx ON email USING gin (bcc);

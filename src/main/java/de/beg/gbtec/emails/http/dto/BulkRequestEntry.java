package de.beg.gbtec.emails.http.dto;

public sealed interface BulkRequestEntry<T> permits RequestEntry, IdentifiedRequestEntry {

    T data();

}

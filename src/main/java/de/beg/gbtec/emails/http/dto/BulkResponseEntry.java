package de.beg.gbtec.emails.http.dto;

public sealed interface BulkResponseEntry<T>
        permits BulkSuccess, BulkError {

    int statusCode();

}
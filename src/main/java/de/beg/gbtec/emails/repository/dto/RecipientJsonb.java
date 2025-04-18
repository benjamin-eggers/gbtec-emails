package de.beg.gbtec.emails.repository.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RecipientJsonb implements Serializable {
    private String niceName;
    private String email;
}

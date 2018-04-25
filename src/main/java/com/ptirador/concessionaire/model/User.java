package com.ptirador.concessionaire.model;

import com.ptirador.concessionaire.model.annotation.EmailUnique;
import com.ptirador.concessionaire.util.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Bean that represents an application user.
 *
 * @author ptirador
 */
@Document(collection = "users")
@Getter
@Setter
@EmailUnique
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(message = "{msg.mandatory.field}")
    private String email;

    private String username;

    private String password;

    @Builder.Default
    private int role = Constants.ROLE_USER_ID;

    private String language;

    @Builder.Default
    private Boolean active = Boolean.TRUE;
}

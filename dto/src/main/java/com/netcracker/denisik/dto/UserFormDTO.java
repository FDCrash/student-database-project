package com.netcracker.denisik.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFormDTO extends BaseDTO {
    private String password;
    private String login;
    private String name;

    public UserFormDTO(long id, String password, String login, String name) {
        super(id);
        this.password = password;
        this.login = login;
        this.name = name;
    }
}

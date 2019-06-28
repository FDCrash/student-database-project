package com.netcracker.denisik.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserDTO extends UserFormDTO {
    private RoleDTO roleDTO;

    @Builder
    public UserDTO(long id, RoleDTO roleDTO, String password, String login, String name) {
        super(id, password, login, name);
        this.roleDTO = roleDTO;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }

}

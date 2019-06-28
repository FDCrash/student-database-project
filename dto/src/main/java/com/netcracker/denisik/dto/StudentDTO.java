package com.netcracker.denisik.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@XmlRootElement(name = "Student")
@XmlSeeAlso(ArrayList.class)
public class StudentDTO extends UserDTO {
    private long groupId;
    private String speciality;
    private long specialityId;
    private WriteBookDTO writeBook;

    @Builder(builderMethodName = "builderStudent")
    public StudentDTO(UserDTO userDTO, long groupId, String speciality, long specialityId, WriteBookDTO writeBook) {
        super(userDTO.getId(), userDTO.getRoleDTO(), userDTO.getPassword(), userDTO.getLogin(), userDTO.getName());
        this.groupId = groupId;
        this.speciality = speciality;
        this.writeBook = writeBook;
        this.specialityId = specialityId;
    }

    @Override
    @JsonIgnore
    public RoleDTO getRoleDTO() {
        return super.getRoleDTO();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    @JsonIgnore
    public void setRoleDTO(RoleDTO roleDTO) {
        super.setRoleDTO(roleDTO);
    }

    @Override
    @JsonIgnore
    public void setLogin(String login) {
        super.setLogin(login);
    }

    @Override
    @JsonIgnore
    public void setPassword(String password) {
        super.setPassword(password);
    }
}

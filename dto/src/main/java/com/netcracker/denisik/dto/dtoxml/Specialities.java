package com.netcracker.denisik.dto.dtoxml;

import com.netcracker.denisik.dto.SpecialityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "specialities")
@XmlAccessorType(XmlAccessType.FIELD)
public class Specialities {
    @XmlElement(name = "speciality")
    private List<SpecialityDTO> specialityDTOS;
}

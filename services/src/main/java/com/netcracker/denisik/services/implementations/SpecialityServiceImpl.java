package com.netcracker.denisik.services.implementations;

import com.google.gson.Gson;
import com.netcracker.denisik.converters.SpecialityConverter;
import com.netcracker.denisik.dao.FacultyRepository;
import com.netcracker.denisik.dao.SpecialityRepository;
import com.netcracker.denisik.dto.SpecialityDTO;
import com.netcracker.denisik.dto.dtoxml.Specialities;
import com.netcracker.denisik.entities.Faculty;
import com.netcracker.denisik.entities.Speciality;
import com.netcracker.denisik.exteption.ResourceAlreadyExistException;
import com.netcracker.denisik.exteption.ResourceNotFoundException;
import com.netcracker.denisik.services.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Transactional
@Service
public class SpecialityServiceImpl implements CrudService<SpecialityDTO> {
    private SpecialityConverter specialityConverter;
    private SpecialityRepository specialityRepository;
    private FacultyRepository facultyRepository;

    @Autowired
    public SpecialityServiceImpl(SpecialityRepository specialityRepository, SpecialityConverter specialityConverter,
                                 FacultyRepository facultyRepository) {
        this.specialityConverter = specialityConverter;
        this.specialityRepository = specialityRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public long add(SpecialityDTO specialityDTO) {
        log.debug("Check free name for speciality");
        if (specialityRepository.existsByName(specialityDTO.getName())) {
            log.error("Speciality exist with name: " + specialityDTO.getName());
            throw new ResourceAlreadyExistException("Speciality exist with name : " + specialityDTO.getName());
        }
        log.debug("Check binding faculty with speciality");
        if (!facultyRepository.existsById(specialityDTO.getId())) {
            log.error("Faculty not binding with speciality");
            throw new ResourceNotFoundException("Faculty not found");
        }
        log.debug("Add/update speciality :" + specialityDTO.getName());
        return specialityRepository.save(specialityConverter.convert(specialityDTO)).getId();
    }

    @Override
    public void delete(long id) {
        log.debug("Deleting speciality");
        if (!specialityRepository.existsById(id)) {
            log.error("Not found speciality for delete by id:" + id);
            throw new ResourceNotFoundException("Deleting speciality by id: " + id);
        }
        specialityRepository.delete(id);
    }

    @Override
    public List<SpecialityDTO> getAll() {
        List<SpecialityDTO> specialityDTOS = StreamSupport.stream(specialityRepository.findAll().spliterator(), false)
                .map(speciality -> specialityConverter.convert(speciality))
                .collect(Collectors.toList());
        log.debug("To Json operation specialities");
        convertToJson(specialityDTOS);
        log.debug("To XML operation students");
        convertToXml(specialityDTOS);
        log.debug("To Excel operation specialities");
        convertToExcel(specialityDTOS);
        log.debug("Getting all specialities from DB");
        return specialityDTOS;
    }

    @Override
    public void convertToJson(List<SpecialityDTO> specialityDTOS) {
        try(FileWriter writer = new FileWriter("services/src/main/resources/json/jsonformatspeciality.json")) {
            new Gson().toJson(specialityDTOS, writer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void convertToXml(List<SpecialityDTO> specialityDTOS) {
        try (FileWriter writer = new FileWriter("services/src/main/resources/xml/xmlformatspeciality.xml")) {
            Specialities specialities = new Specialities(specialityDTOS);
            JAXBContext context = JAXBContext.newInstance(Specialities.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(specialities, writer);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void convertToExcel(List<SpecialityDTO> specialityDTOS) {
        try {
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("Specialities");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Специальность");
            row.createCell(1).setCellValue("Факультет");
            int i = 1;
            for (SpecialityDTO specialityDTO : specialityDTOS) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(specialityDTO.getName());
                row.createCell(1).setCellValue(specialityDTO.getFaculty());
                i++;
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            book.write(new FileOutputStream("services/src/main/resources/excel/specialities.xlsx"));
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SpecialityDTO get(long id) {
        log.debug("Start getting speciality by id");
        Speciality speciality = specialityRepository.findOne(id);
        return specialityConverter.convert(speciality);
    }
}

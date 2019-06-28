package com.netcracker.denisik.services.implementations;

import com.google.gson.Gson;
import com.netcracker.denisik.converters.FacultyConverter;
import com.netcracker.denisik.dao.FacultyRepository;
import com.netcracker.denisik.dao.SpecialityRepository;
import com.netcracker.denisik.dto.FacultyDTO;
import com.netcracker.denisik.dto.dtoxml.Faculties;
import com.netcracker.denisik.entities.Faculty;
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
public class FacultyServiceImpl implements CrudService<FacultyDTO> {
    private FacultyConverter facultyConverter;
    private FacultyRepository facultyRepository;
    private SpecialityRepository specialityRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository, FacultyConverter facultyConverter,
                              SpecialityRepository specialityRepository) {
        this.facultyConverter = facultyConverter;
        this.facultyRepository = facultyRepository;
        this.specialityRepository = specialityRepository;
    }

    @Override
    public long add(FacultyDTO facultyDTO) {
        log.debug("Check free name for new faculty");
        if (facultyRepository.existsByName(facultyDTO.getName())) {
            log.error("Faculty exist with name : " + facultyDTO.getName());
            throw new ResourceAlreadyExistException("Faculty exist with name : " + facultyDTO.getName());
        }
        log.debug("Check specialities for new faculty");
        checkSpecialities(facultyDTO.getSpecialities());
        log.info("Add/update faculty : " + facultyDTO.getName());
        return facultyRepository.save(facultyConverter.convert(facultyDTO)).getId();
    }

    public void checkSpecialities(List<String> specialities) {
        for (String name : specialities) {
            if (specialityRepository.existsByName(name)) {
                log.error("Speciality exist with name : " + name);
                throw new ResourceAlreadyExistException("Speciality exist with name : " + name);
            }
        }
    }

    @Override
    public void delete(long id) {
        log.debug("Deleting faculty");
        if (!facultyRepository.existsById(id)) {
            log.error("Not found faculty for delete by id:" + id);
            throw new ResourceNotFoundException("Deleting faculty by id: " + id);
        }
        facultyRepository.delete(id);
    }

    @Override
    public List<FacultyDTO> getAll() {
        List<FacultyDTO> facultyDTOS = StreamSupport.stream(facultyRepository.findAll().spliterator(), false)
                .map(faculty -> facultyConverter.convert(faculty))
                .collect(Collectors.toList());
        log.debug("To Json operation faculties");
        convertToJson(facultyDTOS);
        log.debug("To XML operation faculties");
        convertToXml(facultyDTOS);
        log.debug("To Excel operation faculties");
        convertToExcel(facultyDTOS);
        log.debug("Getting all faculties from DB");
        return facultyDTOS;
    }

    @Override
    public void convertToJson(List<FacultyDTO> facultyDTOS) {
        try (FileWriter writer = new FileWriter("services/src/main/resources/json/jsonformatfaculty.json")) {
            new Gson().toJson(facultyDTOS, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void convertToXml(List<FacultyDTO> facultyDTOS) {
        try (FileWriter writer = new FileWriter("services/src/main/resources/xml/xmlformatfaculty.xml")) {
            Faculties faculties = new Faculties(facultyDTOS);
            JAXBContext context = JAXBContext.newInstance(Faculties.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(faculties, writer);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void convertToExcel(List<FacultyDTO> facultyDTOS) {
        try {
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("Faculties");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Факультеты");
            row.createCell(1).setCellValue("Специальности");
            int i = 1;
            for (FacultyDTO facultyDTO : facultyDTOS) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(facultyDTO.getName());
                for (String s : facultyDTO.getSpecialities()) {
                    row.createCell(1).setCellValue(s);
                    i++;
                    row = sheet.createRow(i);
                }
                i++;
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            book.write(new FileOutputStream("services/src/main/resources/excel/faculties.xlsx"));
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FacultyDTO get(long id) {
        log.debug("Start getting faculty by id");
        Faculty faculty = facultyRepository.findOne(id);
        return facultyConverter.convert(faculty);
    }
}

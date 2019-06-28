package com.netcracker.denisik.services.implementations;

import com.google.gson.Gson;
import com.netcracker.denisik.converters.StudentConverter;
import com.netcracker.denisik.dao.SpecialityRepository;
import com.netcracker.denisik.dao.StudentRepository;
import com.netcracker.denisik.dao.SubjectRepository;
import com.netcracker.denisik.dto.SubjectMarkDTO;
import com.netcracker.denisik.dto.StudentDTO;
import com.netcracker.denisik.dto.dtoxml.Students;
import com.netcracker.denisik.entities.Student;
import com.netcracker.denisik.exteption.ResourceNotFoundException;
import com.netcracker.denisik.exteption.ServiceException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Transactional
@Service
public class StudentServiceImpl implements CrudService<StudentDTO> {
    private StudentConverter studentConverter;
    private StudentRepository studentRepository;
    private SpecialityRepository specialityRepository;
    private SubjectRepository subjectRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentConverter studentConverter,
                              SpecialityRepository specialityRepository, SubjectRepository subjectRepository) {
        this.studentConverter = studentConverter;
        this.studentRepository = studentRepository;
        this.specialityRepository = specialityRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public long add(StudentDTO studentDTO) {
        boolean checkRange = studentDTO.getWriteBook().getSubjectMarkDTOS().stream()
                .anyMatch(semesterDTO -> semesterDTO.getMark() > 10 || semesterDTO.getMark() < 0);
        log.debug("Check speciality for student");
        if (!specialityRepository.existsById(studentDTO.getSpecialityId())) {
            log.error("Not found speciality for student");
            throw new ResourceNotFoundException("Speciality not found");
        }
        log.debug("Check range of marks");
        if (checkRange) {
            log.error("Range out of mark");
            throw new ServiceException("Range out of mark");
        }
        log.debug("Check exist subjects in DB");
        checkSubjects(studentDTO.getWriteBook().getSubjectMarkDTOS());
        log.debug("Add/update student :" + studentDTO.getName());
        return studentRepository.save(studentConverter.convert(studentDTO)).getId();
    }

    @Override
    public void delete(long id) {
        log.debug("Deleting student");
        if (!studentRepository.existsById(id)) {
            log.error("Not found student for delete by id: " + id);
            throw new ResourceNotFoundException("Deleting student by id: " + id);
        }
        studentRepository.delete(id);
    }

    public List<StudentDTO> getAllByGroup(long number) {
        List<StudentDTO> studentDTOS = studentRepository.getAllByGroupId(number).stream()
                .map(student -> studentConverter.convert(student))
                .collect(Collectors.toList());
        convertTo(studentDTOS);
        log.debug("Start get students by group id: " + number);
        return studentDTOS;
    }

    public List<StudentDTO> getAllBySpeciality(String speciality) {
        List<StudentDTO> studentDTOS = studentRepository.getAllBySpecialityName(speciality).stream()
                .map(student -> studentConverter.convert(student))
                .collect(Collectors.toList());
        convertTo(studentDTOS);
        log.debug("Start get students by speciality name : " + speciality);
        return studentDTOS;
    }

    public List<StudentDTO> getAllByFaculty(String faculty) {
        List<StudentDTO> studentDTOS = studentRepository.getAllBySpecialityFacultyName(faculty).stream()
                .map(student -> studentConverter.convert(student))
                .collect(Collectors.toList());
        convertTo(studentDTOS);
        log.debug("Start get students by faculty name : " + faculty);
        return studentDTOS;
    }

    public void checkSubjects(List<SubjectMarkDTO> subjectMarkDTOS) {
        for (SubjectMarkDTO subjectMarkDTO : subjectMarkDTOS) {
            if (!subjectRepository.existsById(subjectMarkDTO.getSubject().getId())) {
                log.error("Subject not found for student");
                throw new ResourceNotFoundException("Subject not found");
            }
        }
    }

    public List<StudentDTO> getAllStudents() {
        List<StudentDTO> studentDTOS = getAll();
        convertTo(studentDTOS);
        return studentDTOS;
    }

    @Override
    public List<StudentDTO> getAll() {
        List<StudentDTO> studentDTOS = StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                .map(student -> studentConverter.convert(student))
                .collect(Collectors.toList());
        log.debug("Getting students from DB");
        return studentDTOS;
    }

    public void convertTo(List<StudentDTO> studentDTOS) {
        log.debug("To Json operation students");
        convertToJson(studentDTOS);
        log.debug("To XML operation students");
        convertToXml(studentDTOS);
        log.debug("To Excel operation students");
        convertToExcel(studentDTOS);
    }

    @Override
    public void convertToJson(List<StudentDTO> studentDTOS) {
        try (FileWriter writer = new FileWriter("services/src/main/resources/json/jsonformatstudent.json")) {
            new Gson().toJson(studentDTOS, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void convertToXml(List<StudentDTO> studentDTOS) {
        try (FileWriter writer = new FileWriter("services/src/main/resources/xml/xmlformatstudent.xml")) {
            Students students = new Students(studentDTOS);
            students.getStudentDTOS().forEach(studentDTO -> studentDTO.setPassword(null));
            JAXBContext context = JAXBContext.newInstance(Students.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(students, writer);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void convertToExcel(List<StudentDTO> studentDTOS) {
        try {
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("Students");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Специальность");
            row.createCell(1).setCellValue("Группа");
            row.createCell(2).setCellValue("Имя");
            row.createCell(3).setCellValue("Номер зачетки");
            row.createCell(4).setCellValue("Бюджет");
            row.createCell(5).setCellValue("Предмет");
            row.createCell(6).setCellValue("Оценка");
            int i = 1;
            for (StudentDTO studentDTO : studentDTOS) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(studentDTO.getSpeciality());
                row.createCell(1).setCellValue(studentDTO.getGroupId());
                row.createCell(2).setCellValue(studentDTO.getName());
                row.createCell(3).setCellValue(studentDTO.getWriteBook().getId());
                row.createCell(4).setCellValue(studentDTO.getWriteBook().isBudget() ? "+" : "-");
                for (SubjectMarkDTO subjectMarkDTO : studentDTO.getWriteBook().getSubjectMarkDTOS()) {
                    i++;
                    row = sheet.createRow(i);
                    row.createCell(5).setCellValue(subjectMarkDTO.getSubject().getName());
                    row.createCell(6).setCellValue(subjectMarkDTO.getMark());
                }
                i++;
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            book.write(new FileOutputStream("services/src/main/resources/excel/students.xlsx"));
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  List<StudentDTO> getTop(int size,int type, String filter){
        List<StudentDTO> studentDTOS = new ArrayList<>();
        if (type == 1) {
            studentDTOS = getAll();
        }
        if (type == 2) {
            studentDTOS = getAllByFaculty(filter);
        }
        if(type == 3) {
            studentDTOS = getAllBySpeciality(filter);
        }
        if(type == 4) {
            studentDTOS = getAllByGroup(Long.parseLong(filter));
        }
        return getTopBySpecification(studentDTOS, size);
    }

    public List<StudentDTO> getTopBySpecification(List<StudentDTO> studentDTOS,int size) {
        List<Double> avgMarks = new ArrayList<>();
        for (StudentDTO studentDTO : studentDTOS) {
            avgMarks.add(getAverage(studentDTO));
        }
        studentDTOS = getFilteredStudents(studentDTOS, avgMarks, size);
        convertTo(studentDTOS);
        return studentDTOS;
    }

    public Double getAverage(StudentDTO studentDTO) {
        long sumOfMarks = 0;
        for (SubjectMarkDTO subjectMarkDTO : studentDTO.getWriteBook().getSubjectMarkDTOS()) {
            sumOfMarks += subjectMarkDTO.getMark();
        }
        return ((double) sumOfMarks) / studentDTO.getWriteBook().getSubjectMarkDTOS().size();
    }

    public List<StudentDTO> getFilteredStudents(List<StudentDTO> studentDTOS, List<Double> avgMarks, int size) {
        List<Integer> list = getIdsForMark(avgMarks, size);
        for (int i = 0; i < studentDTOS.size(); i++) {
            if (!list.contains(i)) {
                studentDTOS.remove(i);
            }
        }
        return studentDTOS;
    }

    public List<Integer> getIdsForMark(List<Double> avgMarks, int size) {
        List<Integer> list = new ArrayList<>();
        int k = 0;
        double max;
        for (int j = 0; j < size; j++) {
            max = 0;
            for (int i = 0; i < avgMarks.size(); i++) {
                if (max < avgMarks.get(i) && !list.contains(i)) {
                    max = avgMarks.get(i);
                    k = i;
                }
            }
            list.add(k);
        }
        return list;
    }

    @Override
    public StudentDTO get(long id) {
        log.debug("Start getting student by id");
        Student student = studentRepository.findOne(id);
        return studentConverter.convert(student);
    }
}

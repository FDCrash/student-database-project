package com.netcracker.denisik.services.implementations;

import com.google.gson.Gson;
import com.netcracker.denisik.converters.UserConverter;
import com.netcracker.denisik.dao.StudentRepository;
import com.netcracker.denisik.dao.UserRepository;
import com.netcracker.denisik.dto.UserDTO;
import com.netcracker.denisik.dto.dtoxml.Users;
import com.netcracker.denisik.entities.Role;
import com.netcracker.denisik.entities.Student;
import com.netcracker.denisik.entities.User;
import com.netcracker.denisik.exteption.ResourceAlreadyExistException;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements CrudService<UserDTO> {
    private UserConverter userConverter;
    private StudentRepository studentRepository;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserConverter userConverter, StudentRepository studentRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.userConverter = userConverter;
    }

    public long save(UserDTO userDTO) {
        User user = userRepository.save(userConverter.convert(userDTO));
        return user.getId();
    }

    @Override
    public long add(UserDTO userDTO) {
        log.debug("Check free login for user");
        if (userRepository.existsByLogin(userDTO.getLogin())) {
            log.error("User already exist with login: " + userDTO.getLogin());
            throw new ResourceAlreadyExistException("User exist by login: " + userDTO.getLogin());
        }
        return save(userDTO);
    }

    @Override
    public void delete(long id) {
        log.debug("Deleting user");
        if (!userRepository.existsById(id)) {
            log.error("Not found admin for delete by id: " + id);
            throw new ResourceNotFoundException("User not by id: " + id);
        } else if (getAllAdmins().size() < 1) {
            log.error("Cant delete last admin by id:" + id);
            throw new ServiceException("Cant delete last admin" + id);
        }
        userRepository.delete(id);
    }

    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> userDTOS = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(user -> userConverter.convert(user))
                .collect(Collectors.toList());
        log.debug("To Json operation users");
        convertToJson(userDTOS);
        log.debug("To XML operation users");
        convertToXml(userDTOS);
        log.debug("To Excel operation users");
        convertToExcel(userDTOS);
        log.debug("Getting users from DB");
        return userDTOS;
    }

    public List<UserDTO> getAllAdmins() {
        List<UserDTO> userDTOS = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(user -> user.getRole().equals(Role.ADMIN))
                .map(user -> userConverter.convert(user))
                .collect(Collectors.toList());
        log.debug("To Json operation admins");
        convertToJson(userDTOS);
        log.debug("To XML operation admins");
        convertToXml(userDTOS);
        log.debug("To Excel operation admins");
        convertToExcel(userDTOS);
        log.debug("Start getting admins");
        return userDTOS;
    }

    public List<UserDTO> getAllEmployees() {
        List<UserDTO> userDTOS = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(user -> user.getRole().equals(Role.EMPLOYEE))
                .map(employee -> userConverter.convert(employee))
                .collect(Collectors.toList());
        log.debug("To Json operation employees");
        convertToJson(userDTOS);
        log.debug("To XML operation employees");
        convertToXml(userDTOS);
        log.debug("To Excel operation employees");
        convertToExcel(userDTOS);
        log.debug("Start getting employees");
        return userDTOS;
    }

    @Override
    public void convertToJson(List<UserDTO> userDTOS) {
        try (FileWriter writer = new FileWriter("services/src/main/resources/json/jsonformatuser.json")) {
            new Gson().toJson(userDTOS, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void convertToXml(List<UserDTO> userDTOS) {
        try (FileWriter writer = new FileWriter("services/src/main/resources/xml/xmlformatuser.xml")) {
            Users users = new Users(userDTOS);
            users.getUserDTOS().forEach(studentDTO -> studentDTO.setPassword(null));
            JAXBContext context = JAXBContext.newInstance(Users.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(users, writer);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void convertToExcel(List<UserDTO> userDTOS) {
        try {
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("Users");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Логин");
            row.createCell(1).setCellValue("Имя");
            row.createCell(2).setCellValue("Роль");
            int i = 1;
            for (UserDTO userDTO : userDTOS) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(userDTO.getLogin());
                row.createCell(1).setCellValue(userDTO.getName());
                row.createCell(2).setCellValue(userDTO.getRoleDTO().toString());
                i++;
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            book.write(new FileOutputStream("services/src/main/resources/excel/users.xlsx"));
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDTO get(long id) {
        log.debug("Start getting user by id: " + id);
        User user = userRepository.findOne(id);
        return userConverter.convert(user);
    }

    public boolean registration(long id, String login, String pass) {
        log.debug("Start registration student");
        Student student = studentRepository.getByWriteBookId(id);
        User user = userRepository.findOne(student.getId());
        if (user != null) {
            if (!userRepository.existsByLogin(login)) {
                user.setLogin(login);
                user.setPassword(pass);
                userRepository.save(user);
                log.debug("Registration was successful!");
                return true;
            }
            log.error("User already exist with login : " + login);
            throw new ResourceAlreadyExistException("User exist by login: " + login);
        }
        log.error("Not found student with wtiteBookId: " + id);
        throw new ResourceNotFoundException("Student not found by writeBookId: " + id);
    }
}

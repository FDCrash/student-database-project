package com.netcracker.denisik.services.implementantions;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dao.UserRepository;
import com.netcracker.denisik.dto.RoleDTO;
import com.netcracker.denisik.dto.UserDTO;
import com.netcracker.denisik.entities.Role;
import com.netcracker.denisik.entities.User;
import com.netcracker.denisik.services.implementations.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @Before
    public void setUp() {
        user = User.builder()
                .id(13)
                .login("test")
                .password("test")
                .login("test")
                .role(Role.ADMIN)
                .build();
        userDTO = UserDTO.builder()
                .id(13)
                .login("test")
                .password("test")
                .login("test")
                .roleDTO(RoleDTO.ADMIN)
                .build();
    }

    @After
    public void tearDown() {
        user = null;
        userDTO = null;
    }

    @Test
    public void testGetAll() {
        List<User> mockList = Collections.singletonList(user);
        List<UserDTO> expectedAnswer = Collections.singletonList(userDTO);

        when(userRepository.findAll()).thenReturn(mockList);
        assertEquals(expectedAnswer, userService.getAll());
    }

    @Test
    public void testGetAllAdmins() {
        List<User> mockList = Collections.singletonList(user);
        List<UserDTO> expectedAnswer = Collections.singletonList(userDTO);

        when(userRepository.findAll()).thenReturn(mockList);
        assertEquals(expectedAnswer, userService.getAllAdmins());
    }

    @Test
    public void testGetAllEmployees() {
        User user = this.user;
        user.setRole(Role.EMPLOYEE);
        UserDTO userDTO = this.userDTO;
        userDTO.setRoleDTO(RoleDTO.EMPLOYEE);
        List<User> mockList = Collections.singletonList(user);
        List<UserDTO> expectedAnswer = Collections.singletonList(userDTO);

        when(userRepository.findAll()).thenReturn(mockList);
        assertEquals(expectedAnswer, userService.getAllEmployees());
    }

    @Test
    public void testDelete() {
        when(userRepository.existsById(13L)).thenReturn(true);
        when(userRepository.findAll()).thenReturn(Collections.singleton(user));
        userService.delete(13L);
        verify(userRepository, times(1)).delete(13L);
    }

    @Test
    public void testAdd() {
        when(userRepository.save(user)).thenReturn(user);
        long id = userService.add(userDTO);
        assertEquals(id, user.getId());
    }

    @Test
    public void get() {
        when(userRepository.findOne(13L)).thenReturn(user);
        assertEquals(userDTO, userService.get(13L));
    }
}

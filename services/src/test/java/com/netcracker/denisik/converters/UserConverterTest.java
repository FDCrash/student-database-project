package com.netcracker.denisik.converters;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dto.RoleDTO;
import com.netcracker.denisik.dto.UserDTO;
import com.netcracker.denisik.entities.Role;
import com.netcracker.denisik.entities.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class UserConverterTest {

    @Autowired
    private UserConverter userConverter;

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
        userDTO = null;
        user = null;
    }

    @Test
    public void toDTO() {
        UserDTO userDTO = userConverter.convert(user);
        Assert.assertEquals(userDTO.hashCode(), this.userDTO.hashCode());
    }

    @Test
    public void fromDTO() {
        User user = userConverter.convert(userDTO);
        Assert.assertEquals(user.hashCode(), this.user.hashCode());
    }
}

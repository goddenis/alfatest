package com.alfatest;


import com.alfatest.domain.User;
import com.alfatest.jpa.UserRepository;
import com.alfatest.rest.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class) // Изолируем тестирование контроллеров от репозиториев для уменьшения времени прохождениятевтов
//@SpringBootTest(classes = Application.class)
public class UserControllerTest {
    @Autowired
    private MockMvc server;

    private ObjectMapper mapper  = new ObjectMapper();

    @MockBean
    private UserRepository userRepository;
//    @Before
//    public void setUp() throws Exception {
//        UserController userController = new UserController();
//        server = MockMvcBuilders.standaloneSetup(userController).build();
//    }


    @Test
    public void getUsersTest() throws Exception {

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1l,"user","user",true));
        users.add(new User(2l,"admin","admin",true));
        users.add(new User(3l,"client","client",true));

        given(userRepository.findAll()).willReturn(users);

        server.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
        ;
        verify(userRepository).findAll();
    }

    @Test
    public void postUserTest() throws Exception {

        User user = new User();
        user.setLogin("newLogin");
        user.setPassword("newPassword");

        given(userRepository.save(user)).willReturn(new User(4L,"newLogin","newPassword",true));
        server.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsBytes(user)))
                .andExpect(status().isCreated());
        verify(userRepository).save(user);

        User dublicateUser = new User();
        dublicateUser.setLogin("admin");
        dublicateUser.setPassword("admin");
        given(userRepository.save(dublicateUser)).willThrow(DataIntegrityViolationException.class);

        server.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsBytes(dublicateUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByLoginAndPassword() throws Exception {

        given(userRepository.findByLoginAndPassword("admin","admin"))
                .willReturn(Optional.of(new User(1L,"admin","admin",true)));
        given(userRepository.findByLoginAndPassword("dmin","admin"))
                .willReturn(Optional.empty());

       server.perform(get("/users/admin/admin"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.login").value("admin"))
               .andExpect(jsonPath("$.password").value("admin"));

       server.perform(get("/users/dmin/admin"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void test() throws Exception {
        server.perform(get("/").accept(MediaType.TEXT_PLAIN)).
                andExpect(status().isOk())
                .andExpect(content().string("Hello test"));

    }


}

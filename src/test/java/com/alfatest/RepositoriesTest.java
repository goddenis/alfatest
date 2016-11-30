package com.alfatest;


import com.alfatest.domain.User;
import com.alfatest.jpa.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;


@RunWith(SpringRunner.class)
@DataJpaTest
@EnableAspectJAutoProxy
public class RepositoriesTest {

    private static final String CLIENT_LOGIN_CONST = "client";
    private static final String DEL_USER_LOGIN_CONST = "delUser";
    @Autowired
    private UserRepository userRepository;

    @Test
    public void selectAllTest() throws Exception {

        assertThat(userRepository.findAll())
                .as("Not null returned").isNotNull()
                .as("All 3 users").hasSize(3);

    }

    @Test
    public void userAddTest() throws Exception {

        User user = new User();
        user.setLogin("Denis");
        user.setPassword("Bogomolov");
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull().isGreaterThan(3);

    }

    @Test
    public void userSelectByLoginTest() throws Exception {

        User client = userRepository.findByLogin(CLIENT_LOGIN_CONST);
        assertThat(client).isNotNull();
        assertThat(client.getLogin()).matches(CLIENT_LOGIN_CONST);

    }

    @Test
    public void updateTest() throws Exception {

        //User byLogin = userRepository.findByLogin(CLIENT_LOGIN_CONST);
        User user = new User();
        user.setId(3l);
        user.setLogin("client");
        user.setPassword("newPass");
        userRepository.save(user);

    }

    @Test
    public void dublicateTest() throws Exception {

        User user = new User();
        user.setLogin("admin");
        user.setPassword("admin");

        assertThatExceptionThrownBy(()->userRepository.save(user)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void userSelectByPasswordTest() throws Exception {

        User client = userRepository.findByPassword(CLIENT_LOGIN_CONST);
        assertThat(client).isNotNull();
        assertThat(client.getLogin()).matches(CLIENT_LOGIN_CONST);

    }

    @Test
    public void userDeleteTest() throws Exception {

        User user = new User();
        user.setLogin(DEL_USER_LOGIN_CONST);
        user.setPassword(DEL_USER_LOGIN_CONST);
        userRepository.save(user);

        User byLogin = userRepository.findByLogin(DEL_USER_LOGIN_CONST);
        assertThat(byLogin).isNotNull();
        assertThat(byLogin.getLogin()).matches(DEL_USER_LOGIN_CONST);

        userRepository.delete(byLogin);

    }

    @Test
    public void getByLoginAndPasswordTest() throws Exception {

        Optional<User> user = userRepository.findByLoginAndPassword("admin", "admin");
        assertThat(user.isPresent()).isTrue();
    }

    @Test
    public void validationShortLoginTest() throws Exception {

        final User user = new User();
        user.setLogin("Den");
        user.setPassword("Bogomolov");


        assertThatExceptionThrownBy(()->userRepository.save(user))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Validation failed");

    }
    @Test
    public void validationLongLoginTest() throws Exception {

        final User user = new User();
        user.setLogin("DenisDenisDenisDenisDenis");
        user.setPassword("Bogomolov");


        assertThatExceptionThrownBy(()->userRepository.save(user))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Validation failed");

    }
    @Test
    public void validationShortPasswordTest() throws Exception {

        final User user = new User();
        user.setLogin("Denis");
        user.setPassword("Bog");


        assertThatExceptionThrownBy(()->userRepository.save(user))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Validation failed");

    }
    @Test
    public void validationLongPasswordTest() throws Exception {

        final User user = new User();
        user.setLogin("Denis");
        user.setPassword("BogomolovBogomolov");


        assertThatExceptionThrownBy(()->userRepository.save(user))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Validation failed");

    }
}

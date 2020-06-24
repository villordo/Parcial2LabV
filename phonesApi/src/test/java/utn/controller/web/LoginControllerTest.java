package utn.controller.web;
/*
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utn.controller.PhoneCallController;
import utn.controller.UserController;
import utn.dto.LoginRequestDto;
import utn.exceptions.InvalidLoginException;
import utn.exceptions.UserNotExistsException;
import utn.exceptions.ValidationException;
import utn.model.City;
import utn.model.Province;
import utn.model.User;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;
import utn.session.SessionManager;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoginControllerTest {

    LoginController loginController;
    @Mock
    UserController userController;
    @Mock
    SessionManager sessionManager;
    @Mock
    RestUtils restUtils;

    @Before
    public void setUp() {
        initMocks(this);
        loginController = new LoginController(userController, sessionManager);
    }
//******************************************************login***********************************************************************
    @Test
    public void testLoginOk() throws UserNotExistsException, ValidationException, InvalidLoginException {
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005,05,05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2,"mdp",223,
                        new Province(3,"bsas")));
        when(userController.login("asd","dsa")).thenReturn(u);
        when(sessionManager.createSession(u)).thenReturn("123");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "123");
        ResponseEntity response = ResponseEntity.ok().headers(responseHeaders).build();
        LoginRequestDto loginRequestDto = new LoginRequestDto("asd","dsa");
        ResponseEntity responseEntity = loginController.login(loginRequestDto);
        assertEquals(response, responseEntity);
        verify(userController, times(1)).login("asd","dsa");
    }
    @Test(expected = InvalidLoginException.class)
    public void testLoginValidation() throws InvalidLoginException, ValidationException, UserNotExistsException {
        when(userController.login(null,"dsa")).thenThrow(new ValidationException());
        LoginRequestDto loginRequestDto = new LoginRequestDto(null,"dsa");
        loginController.login(loginRequestDto);
    }
    @Test(expected = InvalidLoginException.class)
    public void testLoginUserNotExists() throws UserNotExistsException, ValidationException, InvalidLoginException {
        when(userController.login("asd","dsa")).thenThrow(new UserNotExistsException());
        LoginRequestDto loginRequestDto = new LoginRequestDto("asd","dsa");
        loginController.login(loginRequestDto);
    }


 //******************************************************logout************************************************************************
    @Test
    public void testLogoutOk(){
        doNothing().when(sessionManager).removeSession("asd");
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.OK).build();
        ResponseEntity response = loginController.logout("asd");
        assertEquals(responseRta, response);
        verify(sessionManager, times(1)).removeSession("asd");
    }
 //******************************************************************************************************************************

 }*/

package utn.controller;

import org.junit.Before;
import org.junit.Test;
import utn.dto.UserDto;
import utn.dto.UserMostCalledNumberDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.UserNotExistsException;
import utn.exceptions.ValidationException;
import utn.model.City;
import utn.model.User;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;
import utn.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    UserController userController;
    UserService userService;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void testLoginOk() throws UserNotExistsException, ValidationException {

        User loggedUser = new User(1, "carlos", "lolo", 38888765, null, "username", "password", "email", null, null, null);
        //cuando el mock llama a login con estos parametros devuelve loggedUser
        when(userService.login("user", "pwd")).thenReturn(loggedUser);
        User returnedUser = userController.login("user", "pwd");
        //verifica que los campos coincidan
        assertEquals(loggedUser.getId(), returnedUser.getId());
        assertEquals(loggedUser.getFirstname(), returnedUser.getFirstname());
        assertEquals(loggedUser.getSurname(), returnedUser.getSurname());
        assertEquals(loggedUser.getDni(), returnedUser.getDni());
        assertEquals(loggedUser.getBirthdate(), returnedUser.getBirthdate());
        assertEquals(loggedUser.getUsername(), returnedUser.getUsername());
        assertEquals(loggedUser.getPwd(), returnedUser.getPwd());
        assertEquals(loggedUser.getEmail(), returnedUser.getEmail());
        assertEquals(loggedUser.getUserType(), returnedUser.getUserType());
        assertEquals(loggedUser.getUserStatus(), returnedUser.getUserStatus());
        assertEquals(loggedUser.getCity(), returnedUser.getCity());
        //verifica que el metodo login ha sido llamado una vez
        verify(userService, times(1)).login("user", "pwd");

    }

    @Test(expected = UserNotExistsException.class)
    public void testLoginUserNotFound() throws UserNotExistsException, ValidationException {
        when(userService.login("user", "pwd")).thenThrow(new UserNotExistsException());
        userController.login("user", "pwd");
    }

    @Test(expected = ValidationException.class)
    public void testLoginIvalidadData() throws UserNotExistsException, ValidationException {
        userController.login(null, null);
    }

    @Test
    public void testAddOk() throws AlreadyExistsException {
        User loggedUser = new User(1, "carlos", "lolo", 38888765, null, "username", "password", "email", null, null, null);
        User user = new User(2, "ivan", "graciarena", 1, new Date(), "username", "pass", "email", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        when(userService.add(user)).thenReturn(loggedUser);
        User returnedUser = userController.add(user);
        assertEquals(loggedUser.getId(), returnedUser.getId());
        assertEquals(loggedUser.getFirstname(), returnedUser.getFirstname());
        assertEquals(loggedUser.getSurname(), returnedUser.getSurname());
        assertEquals(loggedUser.getDni(), returnedUser.getDni());
        assertEquals(loggedUser.getBirthdate(), returnedUser.getBirthdate());
        assertEquals(loggedUser.getUsername(), returnedUser.getUsername());
        assertEquals(loggedUser.getPwd(), returnedUser.getPwd());
        assertEquals(loggedUser.getEmail(), returnedUser.getEmail());
        assertEquals(loggedUser.getUserType(), returnedUser.getUserType());
        assertEquals(loggedUser.getUserStatus(), returnedUser.getUserStatus());
        assertEquals(loggedUser.getCity(), returnedUser.getCity());
    }

    @Test(expected = AlreadyExistsException.class)
    public void testAddAlreadyExistsException() throws AlreadyExistsException {
        User user = new User(2, "ivan", "graciarena", 1, new Date(), "username", "pass", "email", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        when(userService.add(user)).thenThrow(new AlreadyExistsException());
        userController.add(user);
    }

    @Test
    public void testRemoveUserOk() throws UserNotExistsException {
        doNothing().when(userService).removeUser(1);
        userController.removeUser(1);
        verify(userService, times(1)).removeUser(1);
    }

    @Test(expected = UserNotExistsException.class)
    public void testRemoveUserUserNotExistsException() throws UserNotExistsException {
        doThrow(new UserNotExistsException()).when(userService).removeUser(null);
        userController.removeUser(null);
    }

    @Test
    public void testUpdateUserOk() throws UserNotExistsException {
        User user = new User(2, "ivan", "graciarena", 1, new Date(), "username", "pass", "email", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        doNothing().when(userService).updateUser(user);
        userController.updateUser(user);
        verify(userService, times(1)).updateUser(user);
    }

    @Test(expected = UserNotExistsException.class)
    public void testUpdateUserUserNotExistsException() throws UserNotExistsException {
        User user = new User(2, "ivan", "graciarena", 1, new Date(), "username", "pass", "email", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        doThrow(new UserNotExistsException()).when(userService).updateUser(user);
        userController.updateUser(user);
    }

    @Test
    public void testGetByIdOk() throws UserNotExistsException {
        UserDto userDto = new UserDto("ivan", "graciarena", 333333, "ivanmdq22", "ivan@ivan.com", "mdp");
        when(userService.getById(1)).thenReturn(userDto);
        UserDto user = userController.getById(1);
        assertEquals(userDto.getCityName(), user.getCityName());
        assertEquals(userDto.getDni(), user.getDni());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getSurname(), userDto.getSurname());
        verify(userService, times(1)).getById(1);
    }

    @Test(expected = UserNotExistsException.class)
    public void testGetByIdUserNotExistsException() throws UserNotExistsException {
        when(userService.getById(1)).thenThrow(new UserNotExistsException());
        userController.getById(1);
    }

    @Test
    public void testGetMostCalledNumberOk() {
        UserMostCalledNumberDto userMostCalledNumberDto = new UserMostCalledNumberDto("123", "ivan", "graciarena");
        when(userService.getMostCalledNumber("111")).thenReturn(userMostCalledNumberDto);
        UserMostCalledNumberDto mostCalledNumberDto = userController.getMostCalledNumber("111");
        assertEquals(mostCalledNumberDto.getLineNumber(), userMostCalledNumberDto.getLineNumber());
        assertEquals(mostCalledNumberDto.getName(), userMostCalledNumberDto.getName());
        assertEquals(mostCalledNumberDto.getSurname(), userMostCalledNumberDto.getSurname());
        verify(userService, times(1)).getMostCalledNumber("111");
    }

    @Test
    public void testGetAllOk() {
        List<UserDto> list = new ArrayList<>();
        UserDto userDto = new UserDto("ivan", "graciarena", 333333, "ivanmdq22", "ivan@ivan.com", "mdp");
        list.add(userDto);
        when(userService.getAll()).thenReturn(list);
        List<UserDto> returnedList = userController.getAll();
        assertEquals(returnedList.size(), list.size());
        verify(userService, times(1)).getAll();
    }
}

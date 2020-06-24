package utn.service;

import org.junit.Before;
import org.junit.Test;
import utn.dao.UserDao;
import utn.dto.UserDto;
import utn.dto.UserMostCalledNumberDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.exceptions.UserNotExistsException;
import utn.model.City;
import utn.model.User;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    UserService userService;
    UserDao userDao;

    @Before
    public void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    public void testLoginOk() throws UserNotExistsException {
        User userAux = new User(1, "carlos", "lolo", 38888765, null, "username", "password", "email", null, null, null);
        when(userDao.getByUserName("user", "pwd")).thenReturn(userAux);
        User returnedUser = userService.login("user", "pwd");
        assertEquals(userAux.getId(), returnedUser.getId());
        assertEquals(userAux.getFirstname(), returnedUser.getFirstname());
        assertEquals(userAux.getSurname(), returnedUser.getSurname());
        assertEquals(userAux.getDni(), returnedUser.getDni());
        assertEquals(userAux.getBirthdate(), returnedUser.getBirthdate());
        assertEquals(userAux.getUsername(), returnedUser.getUsername());
        assertEquals(userAux.getPwd(), returnedUser.getPwd());
        assertEquals(userAux.getEmail(), returnedUser.getEmail());
        assertEquals(userAux.getUserType(), returnedUser.getUserType());
        assertEquals(userAux.getUserStatus(), returnedUser.getUserStatus());
        assertEquals(userAux.getCity(), returnedUser.getCity());
        verify(userDao, times(1)).getByUserName("user", "pwd");
    }

    @Test(expected = UserNotExistsException.class)
    public void testLoginUserNotFound() throws UserNotExistsException {
        when(userDao.getByUserName(any(), any())).thenReturn(null);
        userService.login("asd", "asd");
    }

    @Test
    public void testAddOk() throws AlreadyExistsException {
        User userAux = new User(1, "carlos", "lolo", 38888765, new Date(), "username", "password", "email", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        User newUser = new User(2, "ivan", "gracianrea", 38794382, new Date(), "ivanmdq22", "123", "ivan@ivan.com", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        when(userDao.getByUsername(anyString())).thenReturn(false);
        when(userDao.add(userAux)).thenReturn(newUser);
        User add = userService.add(userAux);
        assertEquals(add.getId(), newUser.getId());
        assertEquals(add.getDni(), newUser.getDni());
        assertEquals(add.getEmail(), newUser.getEmail());
        verify(userDao, times(1)).add(userAux);
    }

    @Test(expected = AlreadyExistsException.class)
    public void testAddAlreadyExistsException() throws AlreadyExistsException {
        User newUser = new User(2, "ivan", "gracianrea", 38794382, new Date(), "ivanmdq22", "123", "ivan@ivan.com", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        when(userDao.getByUsername(newUser.getUsername())).thenReturn(true);
        userService.add(newUser);
    }

    @Test
    public void testGetMostCalledNumberOk() {
        UserMostCalledNumberDto userMost = new UserMostCalledNumberDto("12345678", "javier", "cenicero");
        when(userDao.getMostCalledNumber("2234770659")).thenReturn(userMost);
        UserMostCalledNumberDto mostCalledNumber = userService.getMostCalledNumber("2234770659");
        assertEquals(mostCalledNumber.getSurname(), userMost.getSurname());
        assertEquals(mostCalledNumber.getName(), userMost.getName());
        assertEquals(mostCalledNumber.getLineNumber(), userMost.getLineNumber());
        verify(userDao, times(1)).getMostCalledNumber("2234770659");
    }

    @Test
    public void testUpdateUserOk() throws UserNotExistsException {
        UserDto user = new UserDto("ivan", "graciarena", 38744938, "ivanmdq22", "ivan@ivan.com", "mdq");
        User userAux = new User(1, "ivan", "gracianrea", 38794382, new Date(), "ivanmdq22", "123", "ivan@ivan.com", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        when(userDao.getById(1)).thenReturn(user);
        doNothing().when(userDao).update(userAux);
        userService.updateUser(userAux);
        verify(userDao, times(1)).update(userAux);
    }

    @Test(expected = UserNotExistsException.class)
    public void testUpdateUserUserNotExistsException() throws UserNotExistsException {
        User userAux = new User(1, "ivan", "gracianrea", 38794382, new Date(), "ivanmdq22", "123", "ivan@ivan.com", UserType.EMPLOYEE, UserStatus.ACTIVE, new City());
        when(userDao.getById(1)).thenReturn(null);
        userService.updateUser(userAux);
    }

    @Test
    public void testRemoveUserOk() throws UserNotExistsException {
        UserDto user = new UserDto("ivan", "graciarena", 38744938, "ivanmdq22", "ivan@ivan.com", "mdq");
        when(userDao.getById(1)).thenReturn(user);
        doNothing().when(userDao).delete(1);
        userService.removeUser(1);
        verify(userDao, times(1)).delete(1);
    }

    @Test(expected = UserNotExistsException.class)
    public void testRemoveUserUserNotExistsException() throws UserNotExistsException {
        when(userDao.getById(anyInt())).thenReturn(null);
        userService.removeUser(anyInt());
    }

    @Test
    public void testGetByIdOk() throws NoExistsException {
        UserDto user = new UserDto("ivan", "graciarena", 38744938, "ivanmdq22", "ivan@ivan.com", "mdq");
        when(userDao.getById(1)).thenReturn(user);
        UserDto byId = userService.getById(1);
        assertEquals(byId.getSurname(), user.getSurname());
        assertEquals(byId.getFirstName(), user.getFirstName());
        assertEquals(byId.getEmail(), user.getEmail());
        assertEquals(byId.getDni(), user.getDni());
        assertEquals(byId.getCityName(), user.getCityName());
        verify(userDao, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdNoExistsException() throws NoExistsException {
        when(userDao.getById(anyInt())).thenReturn(null);
        userService.getById(1);
    }

    @Test
    public void testGetAllOk() {
        List<UserDto> list = new ArrayList<>();
        UserDto userDto = new UserDto("ivan", "graciarena", 333333, "ivanmdq22", "ivan@ivan.com", "mdp");
        list.add(userDto);
        when(userDao.getAll()).thenReturn(list);
        List<UserDto> all = userService.getAll();
        assertEquals(all.size(), list.size());
        verify(userDao, times(1)).getAll();
    }
}

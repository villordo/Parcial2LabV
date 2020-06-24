package utn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import utn.dto.UserDto;
import utn.dto.UserMostCalledNumberDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.UserNotExistsException;
import utn.exceptions.ValidationException;
import utn.model.User;
import utn.service.UserService;

import java.util.List;

@Controller
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User login(String username, String password) throws UserNotExistsException, ValidationException {
        if ((username != null) && (password != null)) {
            return userService.login(username, password);
        } else {
            throw new ValidationException();
        }
    }

    public User add(User user) throws AlreadyExistsException {
        return userService.add(user);
    }

    public void removeUser(Integer idUser) throws UserNotExistsException {
        userService.removeUser(idUser);
    }


    public void updateUser(User user) throws UserNotExistsException {
        userService.updateUser(user);
    }

    public UserDto getById(Integer id) throws UserNotExistsException {
        return userService.getById(id);
    }

    public UserMostCalledNumberDto getMostCalledNumber(String lineNumber) {
        return userService.getMostCalledNumber(lineNumber);
    }

    public List<UserDto> getAll() {
        return userService.getAll();
    }
}

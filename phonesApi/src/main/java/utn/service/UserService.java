package utn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.dao.UserDao;
import utn.dto.UserDto;
import utn.dto.UserMostCalledNumberDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.UserNotExistsException;
import utn.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserDao dao;

    @Autowired
    public UserService(UserDao dao) {
        this.dao = dao;
    }

    public User login(String userName, String password) throws UserNotExistsException {
        User user = dao.getByUserName(userName, password);
        return Optional.ofNullable(user).orElseThrow(UserNotExistsException::new);

    }

    public User add(User value) throws AlreadyExistsException {
        if (dao.getByUsername(value.getUsername())) {
            throw new AlreadyExistsException();
        }
        return dao.add(value);
    }

    public UserMostCalledNumberDto getMostCalledNumber(String lineNumber) {
        return dao.getMostCalledNumber(lineNumber);
    }

    public void updateUser(User user) throws UserNotExistsException {
        UserDto byId = dao.getById(user.getId());
        Optional.ofNullable(byId).orElseThrow(UserNotExistsException::new);
        dao.update(user);
    }

    public void removeUser(Integer idUser) throws UserNotExistsException {
        UserDto user = dao.getById(idUser);
        Optional.ofNullable(user).orElseThrow(UserNotExistsException::new);
        dao.delete(idUser);
    }

    public UserDto getById(Integer id) throws UserNotExistsException {
        UserDto user = dao.getById(id);
        Optional.ofNullable(user).orElseThrow(UserNotExistsException::new);
        return user;
    }

    public List<UserDto> getAll() {
        return dao.getAll();
    }
}

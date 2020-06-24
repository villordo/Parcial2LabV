package utn.dao;


import utn.dto.UserDto;
import utn.dto.UserMostCalledNumberDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.UserNotExistsException;
import utn.model.City;
import utn.model.User;

import java.util.List;

//todavia no voy a definir las operaciones, esta clase va a decir que operaciones voy a poder realizar sobre un usuario
//la vamos a codificar en la clase concreta EJ: UserMySqlDao
public interface UserDao extends AbstractDao<User> {

    User getByUserName(String username, String password);

    List<UserDto> getByCity(City city);

    void update(User u);

    User add(User u) throws AlreadyExistsException;

    UserMostCalledNumberDto getMostCalledNumber(String lineNumber);

    boolean getByUsername(String username);

    List<UserDto> getAll();

    UserDto getById(Integer id);
}

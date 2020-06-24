package utn.dao;

import utn.dto.UserLineDto;
import utn.exceptions.AlreadyExistsException;
import utn.model.UserLine;

import java.util.List;

public interface UserLineDao extends AbstractDao<UserLine> {
    UserLine add(UserLine userLine) throws AlreadyExistsException;

    UserLineDto getById(Integer id);

    List<UserLineDto> getAll();

    boolean getUserLineByNumber(String lineNumber);
}

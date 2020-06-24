package utn.controller;

import org.springframework.stereotype.Controller;
import utn.dto.UserLineDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.model.UserLine;
import utn.service.UserLineService;

import java.util.List;

@Controller
public class UserLineController {

    UserLineService userLineService;

    public UserLineController(UserLineService userLineService) {
        this.userLineService = userLineService;
    }

    public UserLine add(UserLine userLine) throws AlreadyExistsException {
        return userLineService.add(userLine);
    }

    public void remove(Integer id) throws NoExistsException {
        userLineService.remove(id);
    }

    public void update(UserLine userLine) throws NoExistsException {
        userLineService.update(userLine);
    }

    public UserLineDto getById(Integer id) throws NoExistsException {
        return userLineService.getById(id);
    }

    public List<UserLineDto> getAll() {
        return userLineService.getAll();
    }
}

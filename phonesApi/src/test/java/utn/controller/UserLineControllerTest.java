package utn.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dto.UserLineDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.model.User;
import utn.model.UserLine;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;
import utn.service.UserLineService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserLineControllerTest {

    UserLineController userLineController;
    @Mock
    UserLineService userLineService;

    @Before
    public void setUp() {
        initMocks(this);
        userLineController = new UserLineController(userLineService);
    }

    @Test
    public void testAddOk() throws AlreadyExistsException {
        UserLine userLine = new UserLine(1, "111", TypeLine.MOBILE, LineStatus.ACTIVE, new User());
        UserLine userL = new UserLine(2, "222", TypeLine.MOBILE, LineStatus.ACTIVE, new User());
        when(userLineService.add(userL)).thenReturn(userLine);
        UserLine userLineReturned = userLineController.add(userL);
        assertEquals(userLineReturned.getId(), userLine.getId());
        assertEquals(userLine.getLineNumber(), userLineReturned.getLineNumber());
        assertEquals(userLine.getLineStatus(), userLineReturned.getLineStatus());
        assertEquals(userLine.getTypeLine(), userLine.getTypeLine());
        assertEquals(userLine.getUser().getId(), userLineReturned.getUser().getId());
        verify(userLineService, times(1)).add(userL);
    }

    @Test(expected = AlreadyExistsException.class)
    public void testAddAlreadyExistsException() throws AlreadyExistsException {
        UserLine userLine = new UserLine(1, "111", TypeLine.MOBILE, LineStatus.ACTIVE, new User());
        when(userLineService.add(userLine)).thenThrow(new AlreadyExistsException());
        userLineController.add(userLine);
    }

    @Test
    public void testRemoveOk() throws NoExistsException {
        doNothing().when(userLineService).remove(1);
        userLineController.remove(1);
        verify(userLineService, times(1)).remove(1);
    }

    @Test(expected = NoExistsException.class)
    public void testRemoveNoExistsException() throws NoExistsException {
        doThrow(new NoExistsException()).when(userLineService).remove(1);
        userLineController.remove(1);
    }

    @Test
    public void testUpdateOk() throws NoExistsException {
        UserLine userLine = new UserLine(1, "111", TypeLine.MOBILE, LineStatus.ACTIVE, new User());
        doNothing().when(userLineService).update(userLine);
        userLineController.update(userLine);
        verify(userLineService, times(1)).update(userLine);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdateNoExistsException() throws NoExistsException {
        UserLine userLine = new UserLine(1, "111", TypeLine.MOBILE, LineStatus.ACTIVE, new User());
        doThrow(new NoExistsException()).when(userLineService).update(userLine);
        userLineController.update(userLine);
    }

    @Test
    public void testGetByIdOk() throws NoExistsException {
        UserLineDto userLineDto = new UserLineDto("1111", TypeLine.MOBILE, LineStatus.ACTIVE, "ivan", "graciarena", 111111);
        when(userLineService.getById(1)).thenReturn(userLineDto);
        UserLineDto byId = userLineController.getById(1);
        assertEquals(byId.getDni(), userLineDto.getDni());
        assertEquals(byId.getFirstName(), userLineDto.getFirstName());
        assertEquals(byId.getLineNumber(), userLineDto.getLineNumber());
        assertEquals(byId.getLineStatus(), userLineDto.getLineStatus());
        assertEquals(byId.getSurname(), userLineDto.getSurname());
        assertEquals(byId.getTypeLine(), userLineDto.getTypeLine());
        verify(userLineService, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdNoExistsException() throws NoExistsException {
        when(userLineService.getById(1)).thenThrow(new NoExistsException());
        userLineController.getById(1);
    }

    @Test
    public void testGetAllOk() {
        List<UserLineDto> userLineDtos = new ArrayList<>();
        UserLineDto userLineDto = new UserLineDto("1111", TypeLine.MOBILE, LineStatus.ACTIVE, "ivan", "graciarena", 111111);
        userLineDtos.add(userLineDto);
        when(userLineService.getAll()).thenReturn(userLineDtos);
        List<UserLineDto> all = userLineController.getAll();
        assertEquals(all.size(),userLineDtos.size());
        verify(userLineService,times(1)).getAll();
    }
}

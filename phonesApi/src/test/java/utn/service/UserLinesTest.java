package utn.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dao.UserLineDao;
import utn.dto.UserLineDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.model.User;
import utn.model.UserLine;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserLinesTest {

    UserLineService userLineService;
    @Mock
    UserLineDao userLineDao;


    @Before
    public void setUp() {
        initMocks(this);
        userLineService = new UserLineService(userLineDao);
    }

    @Test
    public void testAddOk() throws AlreadyExistsException {
        UserLine userLine = new UserLine(1, "2235764133", TypeLine.MOBILE, LineStatus.ACTIVE, new User());
        when(userLineDao.getUserLineByNumber("2235764133")).thenReturn(false);
        UserLine newUserLine = new UserLine(2, "2234770464", TypeLine.RESIDENCE, LineStatus.ACTIVE, new User());
        when(userLineDao.add(userLine)).thenReturn(newUserLine);
        UserLine add = userLineService.add(userLine);
        assertEquals(add.getUser().getId(), newUserLine.getUser().getId());
        assertEquals(add.getTypeLine(), newUserLine.getTypeLine());
        assertEquals(add.getLineNumber(), newUserLine.getLineNumber());
        assertEquals(add.getLineStatus(), newUserLine.getLineStatus());
        assertEquals(add.getId(), newUserLine.getId());
        verify(userLineDao, times(1)).add(userLine);
    }

    @Test(expected = AlreadyExistsException.class)
    public void testAddAlreadyExistsException() throws AlreadyExistsException {
        UserLine newUserLine = new UserLine(2, "2234770464", TypeLine.RESIDENCE, LineStatus.ACTIVE, new User());
        UserLine userLine = new UserLine(1, "2235764133", TypeLine.MOBILE, LineStatus.ACTIVE, new User());
        when(userLineDao.getUserLineByNumber(anyString())).thenReturn(true);
        when(userLineDao.add(userLine)).thenReturn(newUserLine);
        userLineService.add(userLine);
    }

    @Test
    public void testRemoveOk() throws NoExistsException {
        UserLineDto userLineDto = new UserLineDto("223576349", TypeLine.MOBILE, LineStatus.ACTIVE, "ivan", "graciarena", 387000400);
        when(userLineDao.getById(1)).thenReturn(userLineDto);
        doNothing().when(userLineDao).delete(1);
        userLineService.remove(1);
        verify(userLineDao, times(1)).delete(1);
    }

    @Test(expected = NoExistsException.class)
    public void testRemoveNoExistsException() throws NoExistsException {
        when(userLineDao.getById(anyInt())).thenReturn(null);
        userLineService.remove(anyInt());
    }

    @Test
    public void testUpdateOk() throws NoExistsException {
        UserLine newUserLine = new UserLine(1, "2234770464", TypeLine.RESIDENCE, LineStatus.ACTIVE, new User());
        UserLineDto userLineDto = new UserLineDto("223576349", TypeLine.MOBILE, LineStatus.ACTIVE, "ivan", "graciarena", 387000400);
        when(userLineDao.getById(1)).thenReturn(userLineDto);
        doNothing().when(userLineDao).update(newUserLine);
        userLineService.update(newUserLine);
        verify(userLineDao, times(1)).update(newUserLine);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdateNoExistsException() throws NoExistsException {
        UserLine newUserLine = new UserLine(1, "2234770464", TypeLine.RESIDENCE, LineStatus.ACTIVE, new User());
        when(userLineDao.getById(anyInt())).thenReturn(null);
        userLineService.update(newUserLine);
    }

    @Test
    public void testGetByIdOk() throws NoExistsException {
        UserLineDto userLineDto = new UserLineDto("223576349", TypeLine.MOBILE, LineStatus.ACTIVE, "ivan", "graciarena", 387000400);
        when(userLineDao.getById(1)).thenReturn(userLineDto);
        UserLineDto byId = userLineService.getById(1);
        assertEquals(byId.getTypeLine(), userLineDto.getTypeLine());
        assertEquals(byId.getSurname(), userLineDto.getSurname());
        assertEquals(byId.getLineStatus(), userLineDto.getLineStatus());
        assertEquals(byId.getFirstName(), userLineDto.getFirstName());
        assertEquals(byId.getDni(), userLineDto.getDni());
        verify(userLineDao, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdNoExistsException() throws NoExistsException {
        when(userLineDao.getById(anyInt())).thenReturn(null);
        userLineService.getById(anyInt());
    }

    @Test
    public void testGetAllOk(){
        List<UserLineDto> list = new ArrayList<>();
        UserLineDto userLineDto = new UserLineDto("223576349", TypeLine.MOBILE, LineStatus.ACTIVE, "ivan", "graciarena", 387000400);
        UserLineDto userLine = new UserLineDto("2234770937", TypeLine.MOBILE, LineStatus.ACTIVE, "javier", "lombardo", 39700390);
        list.add(userLine);
        list.add(userLineDto);
        when(userLineDao.getAll()).thenReturn(list);
        List<UserLineDto> all = userLineService.getAll();
        assertEquals(all.size(),list.size());
        verify(userLineDao,times(1)).getAll();
    }

}

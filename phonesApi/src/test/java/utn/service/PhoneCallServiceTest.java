package utn.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dao.PhoneCallDao;
import utn.dao.UserDao;
import utn.dao.UserLineDao;
import utn.dto.PhoneCallDto;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.dto.UserDto;
import utn.exceptions.NoExistsException;
import utn.model.City;
import utn.model.Invoice;
import utn.model.PhoneCall;
import utn.model.UserLine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhoneCallServiceTest {

    PhoneCallService phoneCallService;
    @Mock
    PhoneCallDao phoneCallDao;
    @Mock
    UserDao userDao;
    @Mock
    UserLineDao userLineDao;

    @Before
    public void setUp() {
        initMocks(this);
        phoneCallService = new PhoneCallService(phoneCallDao, userDao, userLineDao);
    }

    @Test
    public void testGetAllPhoneCallsFromUserIdOk() throws NoExistsException {
        UserDto userDto = new UserDto("ivan", "graciarena", 38705059, "ivanmdq22", "ivan@ivan.com", "alabama");

        when(userDao.getById(1)).thenReturn(userDto);
        phoneCallService.getAllPhoneCallsFromUserId(1);

        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, null, 10);
        ReturnedPhoneCallDto r2 = new ReturnedPhoneCallDto("456", "789", "3", "4", 5, null, 20);
        phoneCallDtoList.add(r1);
        phoneCallDtoList.add(r2);


        when(phoneCallDao.getAllPhoneCallsFromUserId(1)).thenReturn(phoneCallDtoList);
        assertEquals(2, phoneCallDtoList.size());
        verify(userDao, times(1)).getById(1);
        verify(phoneCallDao, times(1)).getAllPhoneCallsFromUserId(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetAllPhoneCallsFromUserIdNoExistsException() throws NoExistsException {
        when(userDao.getById(1)).thenReturn(null);
        phoneCallService.getAllPhoneCallsFromUserId(1);
    }

    @Test
    public void testGetByIdOk() throws NoExistsException {
        ReturnedPhoneCallDto returned = new ReturnedPhoneCallDto("223554322", "23421114", "mdq", "mdz", 20, new Date(), 100);
        when(phoneCallDao.getById(1)).thenReturn(returned);
        ReturnedPhoneCallDto byId = phoneCallService.getById(1);
        assertEquals(byId.getTotalPrice(), returned.getTotalPrice());
        assertEquals(byId.getLineNumberTo(), returned.getLineNumberTo());
        assertEquals(byId.getLineNumberFrom(), returned.getLineNumberFrom());
        assertEquals(byId.getDuration(), returned.getDuration());
        assertEquals(byId.getCityTo(), returned.getCityTo());
        assertEquals(byId.getCityFrom(), returned.getCityFrom());
        assertEquals(byId.getCallDate(), returned.getCallDate());
        verify(phoneCallDao, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdNoExistsException() throws NoExistsException {
        when(phoneCallDao.getById(1)).thenReturn(null);
        phoneCallService.getById(anyInt());
    }

    @Test
    public void testAddPhoneCallOk() throws NoExistsException {
        PhoneCallDto phoneCallDto = new PhoneCallDto("1", "2", 3, new Date());
        when(userLineDao.getUserLineByNumber(phoneCallDto.getLineNumberTo())).thenReturn(true);
        when(userLineDao.getUserLineByNumber(phoneCallDto.getLineNumberFrom())).thenReturn(true);
        when(phoneCallDao.addPhoneCall(phoneCallDto)).thenReturn(1);
        Integer integer = phoneCallService.addPhoneCall(phoneCallDto);
        assertEquals(Integer.valueOf(1), integer);
        verify(phoneCallDao, times(1)).addPhoneCall(phoneCallDto);
    }

    @Test(expected = NoExistsException.class)
    public void testAddPhoneCallNoExistsException() throws NoExistsException {
        PhoneCallDto phoneCallDto = new PhoneCallDto("1", "2", 3, new Date());
        when(userLineDao.getUserLineByNumber(phoneCallDto.getLineNumberTo())).thenReturn(false);
        when(userLineDao.getUserLineByNumber(phoneCallDto.getLineNumberFrom())).thenReturn(false);
        when(phoneCallDao.addPhoneCall(phoneCallDto)).thenReturn(1);
        phoneCallService.addPhoneCall(phoneCallDto);
    }

    @Test
    public void testDeleteOk() throws NoExistsException {
        ReturnedPhoneCallDto returned = new ReturnedPhoneCallDto("223554322", "23421114", "mdq", "mdz", 20, new Date(), 100);
        when(phoneCallDao.getById(1)).thenReturn(returned);
        doNothing().when(phoneCallDao).delete(1);
        phoneCallService.delete(1);
        verify(phoneCallDao, times(1)).delete(1);
    }

    @Test(expected = NoExistsException.class)
    public void testDeleteNoExistsException() throws NoExistsException {
        when(phoneCallDao.getById(1)).thenReturn(null);
        phoneCallService.delete(anyInt());
    }

    @Test
    public void testUpdateOk() throws NoExistsException {
        ReturnedPhoneCallDto returned = new ReturnedPhoneCallDto("223554322", "23421114", "mdq", "mdz", 20, new Date(), 100);
        PhoneCall phoneCall = new PhoneCall(1, "1", "2",
                new UserLine(),
                new UserLine(),
                new City(),
                new City(), 20,
                new Date(), 1, 1, 1, 1,
                new Invoice());
        when(phoneCallDao.getById(1)).thenReturn(returned);
        doNothing().when(phoneCallDao).update(phoneCall);
        phoneCallService.update(phoneCall);
        verify(phoneCallDao, times(1)).update(phoneCall);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdateNoExistsException() throws NoExistsException {
        when(phoneCallDao.getById(1)).thenReturn(null);
        phoneCallService.delete(1);
    }

    @Test
    public void testGetAllOk() {
        List<ReturnedPhoneCallDto> list = new ArrayList<>();
        ReturnedPhoneCallDto returned = new ReturnedPhoneCallDto("223554322", "23421114", "mdq", "mdz", 20, new Date(), 100);
        ReturnedPhoneCallDto phoneCall = new ReturnedPhoneCallDto("2222334", "2112133", "cba", "mdq", 20, new Date(), 120);
        list.add(returned);
        list.add(phoneCall);
        when(phoneCallDao.getAll()).thenReturn(list);
        List<ReturnedPhoneCallDto> all = phoneCallService.getAll();
        assertEquals(all.size(), list.size());
        verify(phoneCallDao, times(1)).getAll();
    }

    @Test
    public void testGetPhoneCallsFromUserIdBetweenDatesOk() throws NoExistsException {
        UserDto userDto = new UserDto("ivan", "graciarena", 38705059, "ivanmdq22", "ivan@ivan.com", "alabama");
        PhoneCallsBetweenDatesDto phoneBtDates = new PhoneCallsBetweenDatesDto("11-11-1991", "12-12-1992");
        List<ReturnedPhoneCallDto> list = new ArrayList<>();
        ReturnedPhoneCallDto returned = new ReturnedPhoneCallDto("223554322", "23421114", "mdq", "mdz", 20, new Date(), 100);
        ReturnedPhoneCallDto phoneCall = new ReturnedPhoneCallDto("2222334", "2112133", "cba", "mdq", 20, new Date(), 120);
        list.add(returned);
        list.add(phoneCall);
        when(userDao.getById(1)).thenReturn(userDto);
        when(phoneCallDao.getPhoneCallsFromUserIdBetweenDates(phoneBtDates, 1)).thenReturn(list);
        List<ReturnedPhoneCallDto> phoneCallsFromUserIdBetweenDates = phoneCallService.getPhoneCallsFromUserIdBetweenDates(phoneBtDates, 1);
        assertEquals(phoneCallsFromUserIdBetweenDates.size(), list.size());
        verify(phoneCallDao, times(1)).getPhoneCallsFromUserIdBetweenDates(phoneBtDates, 1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetPhoneCallsFromUserIdBetweenDatesNoExistsException() throws NoExistsException {
        PhoneCallsBetweenDatesDto phoneBtDates = new PhoneCallsBetweenDatesDto("11-11-1991", "12-12-1992");
        when(userLineDao.getById(anyInt())).thenReturn(null);
        phoneCallService.getPhoneCallsFromUserIdBetweenDates(phoneBtDates, 1);
    }

    @Test
    public void testGetMostCalledDestinsByUserIdOk() throws NoExistsException {
        UserDto userDto = new UserDto("ivan", "graciarena", 38705059, "ivanmdq22", "ivan@ivan.com", "alabama");
        List<String> list = new ArrayList<>();
        list.add("aux");
        list.add("param");
        when(userDao.getById(1)).thenReturn(userDto);
        when(phoneCallDao.getMostCalledDestinsByUserId(1)).thenReturn(list);
        List<String> mostCalledDestinsByUserId = phoneCallService.getMostCalledDestinsByUserId(1);
        assertEquals(mostCalledDestinsByUserId.size(), list.size());
        verify(phoneCallDao, times(1)).getMostCalledDestinsByUserId(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetMostCalledDestinsByUserIdNoExistsException() throws NoExistsException {
        when(userDao.getById(anyInt())).thenReturn(null);
        phoneCallService.getMostCalledDestinsByUserId(1);
    }
}

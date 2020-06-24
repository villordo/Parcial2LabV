package utn.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dto.PhoneCallDto;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.exceptions.NoExistsException;
import utn.exceptions.ValidationException;
import utn.model.City;
import utn.model.Invoice;
import utn.model.PhoneCall;
import utn.model.UserLine;
import utn.service.PhoneCallService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhoneCallControllerTest {


    PhoneCallController phoneCallController;
    @Mock
    PhoneCallService phoneCallService;

    @Before
    public void setUp() {
        initMocks(this);
        phoneCallController = new PhoneCallController(phoneCallService);
    }

    @Test
    public void testAddPhoneCallOk() throws ValidationException, NoExistsException {
        PhoneCallDto phoneCallDto = new PhoneCallDto("1", "2", 3, new Date());
        when(phoneCallService.addPhoneCall(phoneCallDto)).thenReturn(1);
        Integer addPhoneCall = phoneCallController.addPhoneCall(phoneCallDto);
        assertEquals(Integer.valueOf(1), addPhoneCall);
        verify(phoneCallService, times(1)).addPhoneCall(phoneCallDto);
    }

    @Test(expected = ValidationException.class)
    public void testAddPhoneCallValidationException() throws ValidationException, NoExistsException {
        PhoneCallDto phoneCallDto = new PhoneCallDto("1", "2", 3, null);
        phoneCallController.addPhoneCall(phoneCallDto);
    }

    @Test(expected = NoExistsException.class)
    public void testAddPhoneCallNoExistsException() throws ValidationException, NoExistsException {
        PhoneCallDto phoneCallDto = new PhoneCallDto("1", "2", 3, new Date());
        when(phoneCallService.addPhoneCall(phoneCallDto)).thenThrow(new NoExistsException());
        phoneCallController.addPhoneCall(phoneCallDto);
    }


    @Test
    public void testRemoveOk() throws NoExistsException {
        ReturnedPhoneCallDto returnedPhoneCallDto = new ReturnedPhoneCallDto("1", "2", "3", "4", 20, null, 20);
        doNothing().when(phoneCallService).delete(1);
        phoneCallController.delete(1);
        verify(phoneCallService, times(1)).delete(1);
    }

    @Test(expected = NoExistsException.class)
    public void testRemoveNoExistsException() throws NoExistsException {
        doThrow(new NoExistsException()).when(phoneCallService).delete(1);
        phoneCallController.delete(1);
    }

    @Test
    public void testUpdateOk() throws NoExistsException {
        PhoneCall phoneCall = new PhoneCall(1, "1", "2",
                new UserLine(),
                new UserLine(),
                new City(),
                new City(), 20,
                new Date(), 1, 1, 1, 1,
                new Invoice());
        doNothing().when(phoneCallService).update(phoneCall);
        phoneCallController.update(phoneCall);
        verify(phoneCallService, times(1)).update(phoneCall);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdateNoExistsException() throws NoExistsException {
        doThrow(new NoExistsException()).when(phoneCallService).update(null);
        phoneCallController.update(null);
    }

    @Test
    public void testGetByIdOk() throws NoExistsException {
        ReturnedPhoneCallDto returnedPhoneCallDto = new ReturnedPhoneCallDto("1", "2", "3", "4", 20, null, 20);
        when(phoneCallService.getById(1)).thenReturn(returnedPhoneCallDto);
        ReturnedPhoneCallDto phoneCallDto = phoneCallController.getById(1);
        assertEquals(phoneCallDto.getCallDate(),returnedPhoneCallDto.getCallDate());
        assertEquals(phoneCallDto.getCityFrom(),returnedPhoneCallDto.getCityFrom());
        assertEquals(phoneCallDto.getCityTo(),returnedPhoneCallDto.getCityTo());
        assertEquals(phoneCallDto.getDuration(),returnedPhoneCallDto.getDuration());
        assertEquals(phoneCallDto.getLineNumberFrom(),returnedPhoneCallDto.getLineNumberFrom());
        assertEquals(phoneCallDto.getLineNumberTo(),returnedPhoneCallDto.getLineNumberTo());
        assertEquals(phoneCallDto.getTotalPrice(),returnedPhoneCallDto.getTotalPrice());
        verify(phoneCallService, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdNoExistsException() throws NoExistsException {
        when(phoneCallService.getById(1)).thenThrow(new NoExistsException());
        phoneCallController.getById(1);
    }

    @Test
    public void testGetAllOk() {
        ReturnedPhoneCallDto phonecallDto = new ReturnedPhoneCallDto("123", "321", "mdp", "bsas", 12, new Date(), 10);
        List<ReturnedPhoneCallDto> returnedPhoneCallDtoList = new ArrayList<>();
        returnedPhoneCallDtoList.add(phonecallDto);
        when(phoneCallService.getAll()).thenReturn(returnedPhoneCallDtoList);
        List<ReturnedPhoneCallDto> all = phoneCallController.getAll();
        assertEquals(all.size(), returnedPhoneCallDtoList.size());
    }

    @Test
    public void testGetAllPhoneCallsFromUserIdOk() throws NoExistsException {
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, null, 10);
        ReturnedPhoneCallDto r2 = new ReturnedPhoneCallDto("456", "789", "3", "4", 5, null, 20);
        phoneCallDtoList.add(r1);
        phoneCallDtoList.add(r2);
        when(phoneCallService.getAllPhoneCallsFromUserId(1)).thenReturn(phoneCallDtoList);
        List<ReturnedPhoneCallDto> returnedPhoneCallDtoList = phoneCallController.getAllPhoneCallsFromUserId(1);
        verify(phoneCallService, times(1)).getAllPhoneCallsFromUserId(1);
        assertEquals(returnedPhoneCallDtoList.size(), phoneCallDtoList.size());
    }

    @Test(expected = NoExistsException.class)
    public void testGetAllPhoneCallsFromUserIdNoExistsException() throws NoExistsException {
        when(phoneCallService.getAllPhoneCallsFromUserId(1)).thenThrow(new NoExistsException());
        phoneCallController.getAllPhoneCallsFromUserId(1);
    }

    @Test
    public void testGetPhoneCallsFromUserIdBetweenDatesOk() throws NoExistsException {
        PhoneCallsBetweenDatesDto phoneCallsBetweenDatesDto = new PhoneCallsBetweenDatesDto("111", "222");
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, null, 10);
        ReturnedPhoneCallDto r2 = new ReturnedPhoneCallDto("456", "789", "3", "4", 5, null, 20);
        phoneCallDtoList.add(r1);
        phoneCallDtoList.add(r2);
        when(phoneCallService.getPhoneCallsFromUserIdBetweenDates(phoneCallsBetweenDatesDto, 1)).thenReturn(phoneCallDtoList);
        List<ReturnedPhoneCallDto> returnedPhoneCallDtoList = phoneCallController.getPhoneCallsFromUserIdBetweenDates(phoneCallsBetweenDatesDto, 1);
        verify(phoneCallService, times(1)).getPhoneCallsFromUserIdBetweenDates(phoneCallsBetweenDatesDto, 1);
        assertEquals(returnedPhoneCallDtoList.size(), phoneCallDtoList.size());
    }

    @Test(expected = NoExistsException.class)
    public void testGetPhoneCallsFromUserIdBetweenDatesNoExistsException() throws NoExistsException {
        PhoneCallsBetweenDatesDto phoneCallsBetweenDatesDto = new PhoneCallsBetweenDatesDto("111", "222");
        when(phoneCallService.getPhoneCallsFromUserIdBetweenDates(phoneCallsBetweenDatesDto, 1)).thenThrow(new NoExistsException());
        phoneCallController.getPhoneCallsFromUserIdBetweenDates(phoneCallsBetweenDatesDto, 1);
    }

    @Test
    public void testGetMostCalledDestinsByUserIdOk() throws NoExistsException {
        List<String> list = new ArrayList<>();
        String s = "alooo";
        list.add(s);
        when(phoneCallService.getMostCalledDestinsByUserId(1)).thenReturn(list);
        List<String> stringList = phoneCallController.getMostCalledDestinsByUserId(1);
        verify(phoneCallService, times(1)).getMostCalledDestinsByUserId(1);
        assertEquals(stringList.size(), list.size());
    }

    @Test(expected = NoExistsException.class)
    public void testGetMostCalledDestinsByUserIdNoExistsException() throws NoExistsException {
        when(phoneCallService.getMostCalledDestinsByUserId(1)).thenThrow(new NoExistsException());
        phoneCallController.getMostCalledDestinsByUserId(1);
    }
}

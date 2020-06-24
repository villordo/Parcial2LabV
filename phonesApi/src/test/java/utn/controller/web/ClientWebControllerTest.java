package utn.controller.web;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utn.controller.InvoiceController;
import utn.controller.PhoneCallController;
import utn.controller.UserController;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.exceptions.NoExistsException;
import utn.model.City;
import utn.model.Province;
import utn.model.User;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;
//import utn.session.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/*public class ClientWebControllerTest {

    ClientWebController clientWebController;

    @Mock
    SessionManager sessionManager;
    @Mock
    UserController userController;
    @Mock
    PhoneCallController phoneCallController;
    @Mock
    InvoiceController invoiceController;

    @Before
    public void setUp() {
        initMocks(this);
        //PowerMockito.mockStatic(RestUtils.class);
        clientWebController = new ClientWebController(sessionManager, userController, phoneCallController, invoiceController);
    }

    //******************************************************getAllPhonecallFromUserId******************************************************************************************
    @Test
    public void testGetAllPhoneCallsFromUserIdOk() throws NoExistsException {
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, null, 10);
        ReturnedPhoneCallDto r2 = new ReturnedPhoneCallDto("456", "789", "3", "4", 5, null, 20);
        phoneCallDtoList.add(r1);
        phoneCallDtoList.add(r2);
        when(phoneCallController.getAllPhoneCallsFromUserId(1)).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.ok(phoneCallDtoList);
        ResponseEntity<List<ReturnedPhoneCallDto>> response = clientWebController.getAllPhoneCallsFromUserId("123", 1);

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getAllPhoneCallsFromUserId(1);
    }

    @Test
    public void testGetAllPhoneCallsFromUserIdNoContent() throws NoExistsException {
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        when(phoneCallController.getAllPhoneCallsFromUserId(1)).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        ResponseEntity<List<ReturnedPhoneCallDto>> response = clientWebController.getAllPhoneCallsFromUserId("123", 1);

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getAllPhoneCallsFromUserId(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetAllPhoneCallsFromUserIdSQLException() throws NoExistsException {
        when(phoneCallController.getAllPhoneCallsFromUserId(1)).thenThrow(new NoExistsException());
        clientWebController.getAllPhoneCallsFromUserId("123", 1);
    }

    //***********************************************************GetTop5DestinationsByUserId******************************************************************************
    @Test
    public void testGetTop5DestinationsByUserIdOk() throws NoExistsException {
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005, 05, 05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2, "mdp", 223, new Province(3, "bsas")));
        when(sessionManager.getCurrentUser("123")).thenReturn(u);
        List<String> phoneCallList = new ArrayList<>();
        phoneCallList.add("asd");
        phoneCallList.add("das");
        when(phoneCallController.getMostCalledDestinsByUserId(1)).thenReturn(phoneCallList);
        ResponseEntity responseRta = ResponseEntity.ok(phoneCallList);

        ResponseEntity<List<String>> response = clientWebController.getTop5DestinationsByUserId("123");

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getMostCalledDestinsByUserId(1);
    }

    @Test
    public void testGetTop5DestinationsByUserIdNoContent() throws NoExistsException {
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005, 05, 05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2, "mdp", 223, new Province(3, "bsas")));
        when(sessionManager.getCurrentUser("123")).thenReturn(u);
        List<String> phoneCallList = new ArrayList<>();
        when(phoneCallController.getMostCalledDestinsByUserId(1)).thenReturn(phoneCallList);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        ResponseEntity<List<String>> response = clientWebController.getTop5DestinationsByUserId("123");

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getMostCalledDestinsByUserId(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetTop5DestinationsByUserIdSQLException() throws NoExistsException {
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005, 05, 05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2, "mdp", 223, new Province(3, "bsas")));
        when(sessionManager.getCurrentUser("123")).thenReturn(u);
        when(phoneCallController.getMostCalledDestinsByUserId(1)).thenThrow(new NoExistsException());
        clientWebController.getTop5DestinationsByUserId("123");
    }

    //*****************************************************getPhoneCallsFromUserIdBetweenDates*************************************************************************************
    @Test
    public void testGetPhoneCallsFromUserIdBetweenDatesOk() throws NoExistsException {
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005, 05, 05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2, "mdp", 223, new Province(3, "bsas")));
        when(sessionManager.getCurrentUser("123")).thenReturn(u);
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, null, 10);
        ReturnedPhoneCallDto r2 = new ReturnedPhoneCallDto("456", "789", "3", "4", 5, null, 20);
        phoneCallDtoList.add(r1);
        phoneCallDtoList.add(r2);
        PhoneCallsBetweenDatesDto phoneCallDto = new PhoneCallsBetweenDatesDto("1233", "1234");
        when(phoneCallController.getPhoneCallsFromUserIdBetweenDates(phoneCallDto, 1)).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.ok(phoneCallDtoList);
        ResponseEntity<List<ReturnedPhoneCallDto>> response = clientWebController.getPhoneCallsFromUserIdBetweenDates("123", phoneCallDto);

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getPhoneCallsFromUserIdBetweenDates(phoneCallDto, 1);
    }

    @Test
    public void testGetPhoneCallsFromUserIdBetweenDatesNoContent() throws NoExistsException {
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005, 05, 05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2, "mdp", 223, new Province(3, "bsas")));
        when(sessionManager.getCurrentUser("123")).thenReturn(u);
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        PhoneCallsBetweenDatesDto phoneCallDto = new PhoneCallsBetweenDatesDto("1233", "1234");
        when(phoneCallController.getPhoneCallsFromUserIdBetweenDates(phoneCallDto, 1)).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        ResponseEntity<List<ReturnedPhoneCallDto>> response = clientWebController.getPhoneCallsFromUserIdBetweenDates("123", phoneCallDto);

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getPhoneCallsFromUserIdBetweenDates(phoneCallDto, 1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetPhoneCallsFromUserIdBetweenDatesSQLException() throws NoExistsException {
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005, 05, 05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2, "mdp", 223, new Province(3, "bsas")));
        PhoneCallsBetweenDatesDto phoneCallDto = new PhoneCallsBetweenDatesDto("1233", "1234");
        when(sessionManager.getCurrentUser("123")).thenReturn(u);
        when(phoneCallController.getPhoneCallsFromUserIdBetweenDates(phoneCallDto, 1)).thenThrow(new NoExistsException());
        clientWebController.getPhoneCallsFromUserIdBetweenDates("123", phoneCallDto);
    }
}
*/
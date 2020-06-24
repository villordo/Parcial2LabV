package utn.controller.web;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utn.controller.*;
import utn.dto.ReturnedPhoneCallDto;
import utn.exceptions.NoExistsException;
import utn.model.City;
import utn.model.Invoice;
import utn.model.PhoneCall;
import utn.model.UserLine;
import utn.session.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/*public class BackofficeWebControllerTest {

    BackofficeWebController backofficeWebController;
    @Mock
    SessionManager sessionManager;
    @Mock
    UserController userController;
    @Mock
    PhoneCallController phoneCallController;
    @Mock
    InvoiceController invoiceController;
    @Mock
    RateController rateController;
    @Mock
    UserLineController userLineController;


    @Before
    public void setUp() {
        initMocks(this);
        backofficeWebController = new BackofficeWebController(sessionManager, userController, phoneCallController, invoiceController, rateController, userLineController);
    }

    //******************************************************getByIdEmployee************************************************************************************
    @Test
    public void testGetByIdPhonecallOk() throws NoExistsException {
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, new Date(), 10);

        when(phoneCallController.getById(1)).thenReturn(r1);
        ResponseEntity responseRta = ResponseEntity.ok(r1);
        ResponseEntity response = backofficeWebController.getByIdPhonecall(1, "123");

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdPhonecallNoExistsException() throws NoExistsException {
        when(phoneCallController.getById(1)).thenThrow(new NoExistsException());
        ResponseEntity responseRta = ResponseEntity.badRequest().build();
        ResponseEntity response = backofficeWebController.getByIdPhonecall(1, "123");
        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getById(1);
    }

    //********************************************************getAll*************************************************************************************
    @Test
    public void testGetAllPhoneCallsOk() throws NoExistsException {
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, new Date(), 10);
        ReturnedPhoneCallDto r2 = new ReturnedPhoneCallDto("456", "789", "3", "4", 5, new Date(), 20);
        phoneCallDtoList.add(r1);
        phoneCallDtoList.add(r2);
        when(phoneCallController.getAll()).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.ok(phoneCallDtoList);
        ResponseEntity<List<ReturnedPhoneCallDto>> response = backofficeWebController.getAllPhoneCalls("234");

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getAll();
    }

    @Test
    public void testGetAllPhoneCallsNoContent() throws NoExistsException {
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();

        when(phoneCallController.getAll()).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        ResponseEntity<List<ReturnedPhoneCallDto>> response = backofficeWebController.getAllPhoneCalls("234");

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getAll();
    }

//******************************************************delete*************************************************************************************

    @Test
    public void testDeleteOk() throws NoExistsException {
        doNothing().when(phoneCallController).delete(1);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.OK).build();
        ResponseEntity response = backofficeWebController.deletePhoneCall("asd", 1);
        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).delete(1);
    }

    @Test(expected = NoExistsException.class)
    public void testDeleteNoExistsException() throws NoExistsException {
        doThrow(new NoExistsException()).when(phoneCallController).delete(1);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        ResponseEntity response = backofficeWebController.deletePhoneCall("asd", 1);
        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).delete(1);

    }

    //*******************************************************************************************************************************************
    @Test
    public void testUpdatePhoneCallOk() throws NoExistsException {
        PhoneCall phoneCall = new PhoneCall(1, "1", "2",
                new UserLine(),
                new UserLine(),
                new City(),
                new City(), 20,
                new java.util.Date(), 1, 1, 1, 1,
                new Invoice());
        doNothing().when(phoneCallController).update(phoneCall);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.OK).build();
        ResponseEntity response = backofficeWebController.updatePhoneCall(phoneCall, "asd");
        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).update(phoneCall);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdatePhoneCallNotExists() throws NoExistsException {
        PhoneCall phoneCall = new PhoneCall(1, "1", "2",
                new UserLine(),
                new UserLine(),
                new City(),
                new City(), 20,
                new java.util.Date(), 1, 1, 1, 1,
                new Invoice());
        doThrow(new NoExistsException()).when(phoneCallController).update(phoneCall);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        ResponseEntity response = backofficeWebController.updatePhoneCall(phoneCall, "asd");
        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).update(phoneCall);
    }

    //******************************************************getAllPhonecallFromUserId******************************************************************************************
    @Test
    public void testGetAllPhoneCallsFromUserIdOk() throws NoExistsException {
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        ReturnedPhoneCallDto r1 = new ReturnedPhoneCallDto("123", "456", "1", "2", 1, new Date(), 10);
        ReturnedPhoneCallDto r2 = new ReturnedPhoneCallDto("456", "789", "3", "4", 5, new Date(), 20);
        phoneCallDtoList.add(r1);
        phoneCallDtoList.add(r2);
        when(phoneCallController.getAllPhoneCallsFromUserId(1)).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.ok(phoneCallDtoList);
        ResponseEntity<List<ReturnedPhoneCallDto>> response = backofficeWebController.getAllPhoneCallsFromUserId("123", 1);

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getAllPhoneCallsFromUserId(1);
    }

    @Test
    public void testGetAllPhoneCallsFromUserIdNoContent() throws NoExistsException {
        List<ReturnedPhoneCallDto> phoneCallDtoList = new ArrayList<>();
        when(phoneCallController.getAllPhoneCallsFromUserId(1)).thenReturn(phoneCallDtoList);
        ResponseEntity responseRta = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        ResponseEntity<List<ReturnedPhoneCallDto>> response = backofficeWebController.getAllPhoneCallsFromUserId("123", 1);

        assertEquals(responseRta, response);
        verify(phoneCallController, times(1)).getAllPhoneCallsFromUserId(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetAllPhoneCallsFromUserIdSQLException() throws NoExistsException {
        when(phoneCallController.getAllPhoneCallsFromUserId(1)).thenThrow(new NoExistsException());
        backofficeWebController.getAllPhoneCallsFromUserId("123", 1);
    }


}*/

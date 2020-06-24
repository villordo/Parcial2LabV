package utn.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dto.DateDto;
import utn.dto.InvoiceDto;
import utn.dto.InvoicesBetweenDateDto;
import utn.exceptions.NoExistsException;
import utn.exceptions.ValidationException;
import utn.service.InvoiceService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceControllerTest {

    InvoiceController invoiceController;
    @Mock
    InvoiceService invoiceService;

    @Before
    public void setUp() {
        initMocks(this);
        invoiceController = new InvoiceController(invoiceService);
    }

    @Test
    public void testGetByIdOk() throws NoExistsException {
        InvoiceDto invoiceDto = new InvoiceDto(1, "123", new Date(), new Date(), 1);
        when(invoiceService.getById(1)).thenReturn(invoiceDto);
        InvoiceDto invoice = invoiceController.getById(1);
        assertEquals(invoice.getCallCount(),invoiceDto.getCallCount());
        assertEquals(invoice.getDateEmission(),invoiceDto.getDateEmission());
        assertEquals(invoice.getDateExpiration(),invoiceDto.getDateExpiration());
        assertEquals(invoice.getLineNumber(),invoiceDto.getLineNumber());
        verify(invoiceService, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdNoExistsException() throws NoExistsException {
        when(invoiceService.getById(1)).thenThrow(new NoExistsException());
        invoiceController.getById(1);
    }

    @Test
    public void testGetAllOk() {
        InvoiceDto invoice = new InvoiceDto(1, "123", new Date(), new Date(), 1);
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        invoiceDtos.add(invoice);
        when(invoiceService.getAll()).thenReturn(invoiceDtos);
        List<InvoiceDto> invoiceDtoList = invoiceController.getAll();
        assertEquals(invoiceDtoList.size(), invoiceDtos.size());
        verify(invoiceService, times(1)).getAll();
    }

    @Test
    public void testGetInvoicesBetweenDatesFromUserIdOk() throws NoExistsException {
        InvoicesBetweenDateDto invoicesBetweenDateDto = new InvoicesBetweenDateDto( "111", "222");
        InvoiceDto invoice = new InvoiceDto(1, "123", new Date(), new Date(), 1);
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        invoiceDtos.add(invoice);
        when(invoiceService.getInvoicesBetweenDatesFromUserId(invoicesBetweenDateDto,1)).thenReturn(invoiceDtos);
        List<InvoiceDto> invoiceDtoList = invoiceController.getInvoicesBetweenDatesFromUserId(invoicesBetweenDateDto,1);
        assertEquals(invoiceDtoList.size(), invoiceDtos.size());
        verify(invoiceService, times(1)).getInvoicesBetweenDatesFromUserId(invoicesBetweenDateDto,1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetInvoicesBetweenDatesFromUserIdNoExistsException() throws NoExistsException {
        InvoicesBetweenDateDto invoicesBetweenDateDto = new InvoicesBetweenDateDto( "111", "222");
        when(invoiceService.getInvoicesBetweenDatesFromUserId(invoicesBetweenDateDto,1)).thenThrow(new NoExistsException());
        invoiceController.getInvoicesBetweenDatesFromUserId(invoicesBetweenDateDto,1);
    }


    @Test
    public void testGetInvoicesByDateOk() throws ValidationException {

        DateDto dateDto = new DateDto(new Date());
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        InvoiceDto r1 = new InvoiceDto(3, "2234567843", new Date(),new Date(), 10);
        InvoiceDto r2 = new InvoiceDto(3, "2234567843", new Date(), new Date(), 10);
        invoiceDtoList.add(r1);
        invoiceDtoList.add(r2);
        when(invoiceService.getInvoicesByDate(dateDto)).thenReturn(invoiceDtoList);
        List<InvoiceDto> invoiceDtoList2 = invoiceController.getInvoicesByDate(dateDto);
        verify(invoiceService, times(1)).getInvoicesByDate(dateDto);
        assertEquals(invoiceDtoList.size(), invoiceDtoList2.size());
    }

    @Test(expected = ValidationException.class)
    public void testGetInvoicesByDateIvalidadData() throws ValidationException {
        invoiceController.getInvoicesByDate(null);
    }
}

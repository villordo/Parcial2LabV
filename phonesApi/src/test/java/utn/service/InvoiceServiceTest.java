package utn.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dao.InvoiceDao;
import utn.dao.mysql.UserMySQLDao;
import utn.dto.InvoiceDto;
import utn.dto.InvoicesBetweenDateDto;
import utn.dto.UserDto;
import utn.exceptions.NoExistsException;
import utn.model.Invoice;
import utn.model.UserLine;
import utn.model.enumerated.InvoiceStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceServiceTest {
    InvoiceService invoiceService;
    @Mock
    InvoiceDao invoiceDao;
    @Mock
    UserMySQLDao userDao;

    @Before
    public void setUp() {
        initMocks(this);
        invoiceService = new InvoiceService(invoiceDao, userDao);
    }

    @Test
    public void testGetInvoicesByDateOk() {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        InvoiceDto r1 = new InvoiceDto(3, "2234567843", new Date(), new Date(), 10);
        InvoiceDto r2 = new InvoiceDto(3, "2234567843", new Date(), new Date(), 10);
        invoiceDtoList.add(r1);
        invoiceDtoList.add(r2);
        when(invoiceDao.getInvoicesByDate(any())).thenReturn(invoiceDtoList);
        List<InvoiceDto> invoicesByDate = invoiceService.getInvoicesByDate(any());
        assertEquals(invoicesByDate.size(), invoiceDtoList.size());
        verify(invoiceDao, times(1)).getInvoicesByDate(any());
    }

    @Test
    public void testGetByIdOk() throws NoExistsException {
        InvoiceDto invoiceDto = new InvoiceDto(2, "123", new Date(), new Date(), 10f);
        when(invoiceDao.getById(1)).thenReturn(invoiceDto);
        InvoiceDto byId = invoiceService.getById(1);
        assertEquals(byId.getLineNumber(), invoiceDto.getLineNumber());
        assertEquals(byId.getCallCount(), invoiceDto.getCallCount());
        verify(invoiceDao, times(1)).getById(1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetByIdNoExistsException() throws NoExistsException {
        InvoiceDto invoiceDto = new InvoiceDto(2, "123", new Date(), new Date(), 10f);
        when(invoiceDao.getById(1)).thenReturn(null);
        invoiceService.getById(1);
    }


    @Test
    public void testUpdateOk() throws NoExistsException {

        InvoiceDto invoiceaux = new InvoiceDto(2, "123", new Date(), new Date(), 10f);
        when(invoiceDao.getById(1)).thenReturn(invoiceaux);
        Invoice invoice = new Invoice(1, 20, 20f, 30f, new Date(), new Date(), InvoiceStatus.NOT_PAY, new UserLine());
        doNothing().when(invoiceDao).update(invoice);
        invoiceService.update(invoice);
        verify(invoiceDao, times(1)).update(invoice);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdateNoExistsException() throws NoExistsException {
        Invoice invoice = new Invoice(1, 20, 20f, 30f, new Date(), new Date(), InvoiceStatus.NOT_PAY, new UserLine());
        when(invoiceDao.getById(anyInt())).thenReturn(null);
        invoiceService.update(invoice);
    }

    @Test
    public void testRemoveOk() throws NoExistsException {
        InvoiceDto invoiceDto = new InvoiceDto(2, "123", new Date(), new Date(), 10f);
        when(invoiceDao.getById(1)).thenReturn(invoiceDto);
        doNothing().when(invoiceDao).delete(1);
        invoiceService.delete(1);
        assertEquals("123", invoiceDto.getLineNumber());
        assertEquals(Integer.valueOf(2), invoiceDto.getCallCount());
        verify(invoiceDao, times(1)).delete(1);
    }

    @Test(expected = NoExistsException.class)
    public void testRemoveNoExistsException() throws NoExistsException {
        when(invoiceDao.getById(1)).thenReturn(null);
        invoiceService.delete(1);
    }

    @Test
    public void testGetAllOk() {
        List<InvoiceDto> dtoList = new ArrayList<>();
        InvoiceDto invoiceDto = new InvoiceDto(2, "123", new Date(), new Date(), 10f);
        InvoiceDto r2 = new InvoiceDto(3, "2234567843", new Date(), new Date(), 10);
        dtoList.add(invoiceDto);
        dtoList.add(r2);
        when(invoiceDao.getAll()).thenReturn(dtoList);
        List<InvoiceDto> all = invoiceService.getAll();
        assertEquals(all.size(), dtoList.size());
        verify(invoiceDao, times(1)).getAll();
    }

    @Test
    public void testGetInvoicesBetweenDatesFromUserIdOk() throws NoExistsException {
        List<InvoiceDto> list = new ArrayList<>();
        InvoiceDto invoice = new InvoiceDto(2, "123", new Date(), new Date(), 10f);
        InvoiceDto r2 = new InvoiceDto(3, "2234567843", new Date(), new Date(), 10);
        list.add(invoice);
        list.add(r2);
        InvoicesBetweenDateDto invoicedto = new InvoicesBetweenDateDto( "11-11-1911", "11-12-1992");
        UserDto user = new UserDto("ivan", "graciarena", 38877444, "ivanmdq22", "ivan@ivan.com", "mdq");
        when(userDao.getById(1)).thenReturn(user);
        when(invoiceDao.getInvoicesBetweenDatesFromUserId(invoicedto,1)).thenReturn(list);
        List<InvoiceDto> invoicesBetweenDatesFromUserId = invoiceService.getInvoicesBetweenDatesFromUserId(invoicedto,1);
        assertEquals(invoicesBetweenDatesFromUserId.size(), list.size());
        verify(invoiceDao, times(1)).getInvoicesBetweenDatesFromUserId(invoicedto,1);
    }

    @Test(expected = NoExistsException.class)
    public void testGetInvoicesBetweenDatesFromUserIdNoExistsException() throws NoExistsException {
        InvoicesBetweenDateDto invoicedto = new InvoicesBetweenDateDto( "11-11-1911", "11-12-1992");
        when(invoiceDao.getById(1)).thenReturn(null);
        invoiceService.getInvoicesBetweenDatesFromUserId(invoicedto,1);
    }

}

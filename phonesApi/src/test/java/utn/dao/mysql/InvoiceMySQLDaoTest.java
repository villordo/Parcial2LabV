package utn.dao.mysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dao.InvoiceDao;
import utn.dto.*;
import utn.model.Invoice;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static utn.dao.mysql.MySQLUtils.*;

public class InvoiceMySQLDaoTest {
    InvoiceMySQLDao invoiceMySQLDao;
    @Mock
    Connection connection;
    @Mock
    UserLineMySQLDao userLineMySQLDao;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;
    @Mock
    CallableStatement cs;

    @Before
    public void setUp() {
        initMocks(this);
        invoiceMySQLDao = new InvoiceMySQLDao(connection, userLineMySQLDao);
    }

//****************************************************************getInvoicesByDate*************************************************************

    @Test
    public void testGetInvoicesByDateOk() throws SQLException {
        when(connection.prepareCall("call sp_invoices_by_date(?)")).thenReturn(cs);
        doNothing().when(cs).setDate(1, new Date(2005,06,06));

        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("call_count")).thenReturn(10);
        when(resultSet.getInt("id_line_fk")).thenReturn(1);
        when(userLineMySQLDao.getLineNumber(1)).thenReturn("223");
        when(resultSet.getDate("date_emission")).thenReturn( new Date(2005,06,06));
        when(resultSet.getDate("date_expiration")).thenReturn( new Date(2005,06,07));
        when(resultSet.getFloat("total_price")).thenReturn(10f);


        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        DateDto dateDto = new DateDto(new Date(2005,06,06));
        List<InvoiceDto> allInvoices = invoiceMySQLDao.getInvoicesByDate(dateDto);

        assertEquals(Integer.valueOf(10), allInvoices.get(0).getCallCount());
        assertEquals("223", allInvoices.get(0).getLineNumber());
        assertEquals(new Date(2005,06,06), allInvoices.get(0).getDateEmission());
        assertEquals(new Date(2005,06,07), allInvoices.get(0).getDateExpiration());

        verify(connection,times(1)).prepareCall("call sp_invoices_by_date(?)");
        verify(cs,times(1)).setDate(1, new Date(2005,06,06));
        verify(cs,times(1)).executeQuery();
    }
    @Test
    public void testGetInvoicesByDateNoContent() throws SQLException{
        when(connection.prepareCall("call sp_invoices_by_date(?)")).thenReturn(cs);
        doNothing().when(cs).setDate(1, new Date(2005,05,05));

        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        DateDto dateDto = new DateDto(new Date(2005,05,05));
        List<InvoiceDto> allInvoices = invoiceMySQLDao.getInvoicesByDate(dateDto);

        assertEquals(0, allInvoices.size());

        verify(connection,times(1)).prepareCall("call sp_invoices_by_date(?)");
        verify(cs,times(1)).setDate(1, new Date(2005,05,05));
        verify(cs,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testInvoicesByDateSQLException() throws SQLException {
        when(connection.prepareCall("call sp_invoices_by_date(?)")).thenThrow(new SQLException());
        DateDto dateDto = new DateDto(new Date(2005,05,05));
        invoiceMySQLDao.getInvoicesByDate(dateDto);
    }
//**********************************************************getInvoicesBetweenDatesFromUserId*******************************************************************
    @Test
    public void testGetInvoicesBetweenDatesFromUserIdOk() throws SQLException {
        when(connection.prepareCall("call sp_invoices_betweendates(?,?,?)")).thenReturn(cs);
        doNothing().when(cs).setInt(1, 1);
        doNothing().when(cs).setString(2, "2005-06-06");
        doNothing().when(cs).setString(3, "2005-06-07");

        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("call_count")).thenReturn(10);
        when(resultSet.getInt("id_line_fk")).thenReturn(1);
        when(userLineMySQLDao.getLineNumber(1)).thenReturn("223");
        when(resultSet.getDate("date_emission")).thenReturn( new Date(2005,06,06));
        when(resultSet.getDate("date_expiration")).thenReturn( new Date(2005,06,07));
        when(resultSet.getFloat("total_price")).thenReturn(10f);


        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        InvoicesBetweenDateDto invoicesDto = new InvoicesBetweenDateDto("2005-06-06","2005-06-07");
        List<InvoiceDto> allInvoices = invoiceMySQLDao.getInvoicesBetweenDatesFromUserId(invoicesDto,1);

        assertEquals(Integer.valueOf(10), allInvoices.get(0).getCallCount());
        assertEquals("223", allInvoices.get(0).getLineNumber());
        assertEquals(new Date(2005,06,06), allInvoices.get(0).getDateEmission());
        assertEquals(new Date(2005,06,07), allInvoices.get(0).getDateExpiration());

        verify(connection,times(1)).prepareCall("call sp_invoices_betweendates(?,?,?)");
        verify(cs,times(1)).setString(2, "2005-06-06");
        verify(cs,times(1)).setString(3, "2005-06-07");
        verify(cs,times(1)).executeQuery();
    }
    @Test
    public void testGetInvoicesBetweenDatesFromUserIdNoContent() throws SQLException{
        when(connection.prepareCall("call sp_invoices_betweendates(?,?,?)")).thenReturn(cs);
        doNothing().when(cs).setInt(1, 1);
        doNothing().when(cs).setString(2, "2005-06-06");
        doNothing().when(cs).setString(3, "2005-06-07");

        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        InvoicesBetweenDateDto invoicesDto = new InvoicesBetweenDateDto("2005-06-06","2005-06-07");
        List<InvoiceDto> allInvoices = invoiceMySQLDao.getInvoicesBetweenDatesFromUserId(invoicesDto,1);

        assertEquals(0, allInvoices.size());


        verify(connection,times(1)).prepareCall("call sp_invoices_betweendates(?,?,?)");
        verify(cs,times(1)).setString(2, "2005-06-06");
        verify(cs,times(1)).setString(3, "2005-06-07");
        verify(cs,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetInvoicesBetweenDatesFromUserIdSQLException() throws SQLException {
        when(connection.prepareCall("call sp_invoices_betweendates(?,?,?)")).thenThrow(new SQLException());
        InvoicesBetweenDateDto invoicesDto = new InvoicesBetweenDateDto("2005-06-06","2005-06-07");
        invoiceMySQLDao.getInvoicesBetweenDatesFromUserId(invoicesDto,1);
    }
//**************************************************************getAll***************************************************************
    @Test
    public void testGetAllOk() throws SQLException {
        when(connection.prepareStatement(BASE_INVOICES_QUERY)).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("call_count")).thenReturn(10);
        when(resultSet.getInt("id_line_fk")).thenReturn(1);
        when(userLineMySQLDao.getLineNumber(1)).thenReturn("223");
        when(resultSet.getDate("date_emission")).thenReturn( new Date(2005,06,06));
        when(resultSet.getDate("date_expiration")).thenReturn( new Date(2005,06,07));
        when(resultSet.getFloat("total_price")).thenReturn(10f);


        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<InvoiceDto> allInvoices = invoiceMySQLDao.getAll();

        assertEquals(Integer.valueOf(10), allInvoices.get(0).getCallCount());
        assertEquals("223", allInvoices.get(0).getLineNumber());
        assertEquals(new Date(2005,06,06), allInvoices.get(0).getDateEmission());
        assertEquals(new Date(2005,06,07), allInvoices.get(0).getDateExpiration());

        verify(connection,times(1)).prepareStatement(BASE_INVOICES_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test
    public void testGetAllNoContent() throws SQLException{
        when(connection.prepareStatement(BASE_INVOICES_QUERY)).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<InvoiceDto> allInvoices = invoiceMySQLDao.getAll();

        assertEquals(0, allInvoices.size());

        verify(connection,times(1)).prepareStatement(BASE_INVOICES_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetAllSQLException() throws SQLException {
        when(connection.prepareStatement(BASE_INVOICES_QUERY)).thenThrow(new SQLException());
        invoiceMySQLDao.getAll();
    }
//******************************************************getAllFromUserId***********************************************************************
    @Test
    public void testGetAllFromUserIdOk() throws SQLException {
        when(connection.prepareStatement(GETALLBYID_INVOICES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("call_count")).thenReturn(10);
        when(resultSet.getInt("id_line_fk")).thenReturn(1);
        when(userLineMySQLDao.getLineNumber(1)).thenReturn("223");
        when(resultSet.getDate("date_emission")).thenReturn( new Date(2005,06,06));
        when(resultSet.getDate("date_expiration")).thenReturn( new Date(2005,06,07));
        when(resultSet.getFloat("total_price")).thenReturn(10f);


        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<InvoiceDto> allInvoices = invoiceMySQLDao.getAllFromUserId(1);

        assertEquals(Integer.valueOf(10), allInvoices.get(0).getCallCount());
        assertEquals("223", allInvoices.get(0).getLineNumber());
        assertEquals(new Date(2005,06,06), allInvoices.get(0).getDateEmission());
        assertEquals(new Date(2005,06,07), allInvoices.get(0).getDateExpiration());

        verify(connection,times(1)).prepareStatement(GETALLBYID_INVOICES_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test
    public void testGetAllFromUserIdNoContent() throws SQLException{
        when(connection.prepareStatement(GETALLBYID_INVOICES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<InvoiceDto> allInvoices = invoiceMySQLDao.getAllFromUserId(1);

        assertEquals(0, allInvoices.size());

        verify(connection,times(1)).prepareStatement(GETALLBYID_INVOICES_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetAllFromUserIdSQLException() throws SQLException {
        when(connection.prepareStatement(GETALLBYID_INVOICES_QUERY)).thenThrow(new SQLException());
        invoiceMySQLDao.getAllFromUserId(1);
    }

//******************************************************getById***********************************************************************

    @Test
    public void testGetByIdOk() throws SQLException {
        when(connection.prepareStatement(GETBYID_INVOICES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("call_count")).thenReturn(10);
        when(resultSet.getInt("id_line_fk")).thenReturn(1);
        when(userLineMySQLDao.getLineNumber(1)).thenReturn("223");
        when(resultSet.getDate("date_emission")).thenReturn( new Date(2005,06,06));
        when(resultSet.getDate("date_expiration")).thenReturn( new Date(2005,06,07));
        when(resultSet.getFloat("total_price")).thenReturn(10f);


        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        //InvoiceDto invoiceDto = new InvoiceDto(10,"223",new Date(),new Date(),10);
        InvoiceDto invoiceDto1 = invoiceMySQLDao.getById(1);

        assertEquals(Integer.valueOf(10), invoiceDto1.getCallCount());
        assertEquals("223", invoiceDto1.getLineNumber());
        assertEquals(new Date(2005,06,06), invoiceDto1.getDateEmission());
        assertEquals(new Date(2005,06,07), invoiceDto1.getDateExpiration());

        verify(connection,times(1)).prepareStatement(GETBYID_INVOICES_QUERY);
        verify(preparedStatement,times(1)).setInt(1, 1);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test
    public void testGetByIdNoContent() throws SQLException{
        when(connection.prepareStatement(GETBYID_INVOICES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        InvoiceDto invoiceDto1 = invoiceMySQLDao.getById(1);

        assertEquals(null, invoiceDto1);

        verify(connection,times(1)).prepareStatement(GETBYID_INVOICES_QUERY);
        verify(preparedStatement,times(1)).setInt(1, 1);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetByIdSQLException() throws SQLException {
        when(connection.prepareStatement(GETBYID_INVOICES_QUERY)).thenThrow(new SQLException());
        invoiceMySQLDao.getById(1);
    }
//*****************************************************************************************************************************


}

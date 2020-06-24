package utn.dao.mysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dto.PhoneCallDto;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.model.*;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;

import javax.sound.sampled.Line;
import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static utn.dao.mysql.MySQLUtils.*;

public class PhoneCallMySQLDaoTest {

    PhoneCallMySQLDao phoneCallMySQLDao;
    @Mock
    Connection connection;
    @Mock
    CityMySQLDao cityMySQLDao;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;
    @Mock
    CallableStatement cs;

    @Before
    public void setUp() {
        initMocks(this);
        phoneCallMySQLDao = new PhoneCallMySQLDao(connection, cityMySQLDao);
    }
//****************************************************************addPhoneCall************************************************************************

    @Test
    public void testAddPhoneCallOk() throws SQLException {
        when(connection.prepareStatement(INSERT_PHONECALLS_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1, "123");
        doNothing().when(preparedStatement).setString(2, "321");
        doNothing().when(preparedStatement).setInt(3, 1);
        doNothing().when(preparedStatement).setDate(4, new Date(2001,05,05));
        when(preparedStatement.execute()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();
        PhoneCallDto phoneCallDto = new PhoneCallDto("123","321",1,new Date(2001,05,05));
        phoneCallMySQLDao.addPhoneCall(phoneCallDto);
    }
    @Test(expected = RuntimeException.class)
    public void testAddPhonecallSQLException() throws SQLException {
        when(connection.prepareStatement(INSERT_PHONECALLS_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
        PhoneCallDto phoneCallDto = new PhoneCallDto("123","321",1,new Date(2001,05,05));
        phoneCallMySQLDao.addPhoneCall(phoneCallDto);
    }
//***************************************************************update*************************************************************************
    @Test
    public void testUpdateOk() throws SQLException {
        when(connection.prepareStatement(UPDATE_PHONECALLS_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1, "223");
        doNothing().when(preparedStatement).setString(2, "321");
        doNothing().when(preparedStatement).setInt(3, 1);
        doNothing().when(preparedStatement).setInt(4, 2);
        doNothing().when(preparedStatement).setInt(5, 1);
        doNothing().when(preparedStatement).setInt(6, 2);
        doNothing().when(preparedStatement).setDate(8, new Date(2001,05,05));
        doNothing().when(preparedStatement).setFloat(9, 10);
        doNothing().when(preparedStatement).setFloat(10, 10);
        doNothing().when(preparedStatement).setFloat(11, 10);
        doNothing().when(preparedStatement).setFloat(12, 10);

        when(cs.executeQuery()).thenReturn(resultSet);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();
        TypeLine typeLine = TypeLine.MOBILE;
        LineStatus lineStatus = LineStatus.ACTIVE;
        PhoneCall phoneCall = new PhoneCall(1,"223","321",new UserLine(1,"223",typeLine,lineStatus,new User()),new UserLine(2,"321",typeLine,lineStatus,new User()),new City(1,"mdp",223,new Province()),new City(2,"bsas",011,new Province()),1,new Date(2001,05,05),10,10,10,10,new Invoice());
        phoneCallMySQLDao.update(phoneCall);
    }
    @Test(expected = RuntimeException.class)
    public void testUpdateSQLException() throws SQLException {
        when(connection.prepareStatement(UPDATE_PHONECALLS_QUERY)).thenThrow(new SQLException());
        TypeLine typeLine = TypeLine.MOBILE;
        LineStatus lineStatus = LineStatus.ACTIVE;
        PhoneCall phoneCall = new PhoneCall(1,"223","321",new UserLine(1,"223",typeLine,lineStatus,new User()),new UserLine(2,"321",typeLine,lineStatus,new User()),new City(1,"mdp",223,new Province()),new City(2,"bsas",011,new Province()),1,new Date(2001,05,05),10,10,10,10,new Invoice());
        phoneCallMySQLDao.update(phoneCall);
    }
//**************************************************************delete**************************************************************************
    @Test
    public void testDeteleOk() throws SQLException {
        when(connection.prepareStatement(REMOVE_PHONECALLS_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();
        phoneCallMySQLDao.delete(1);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteSQLException() throws SQLException {
        when(connection.prepareStatement(REMOVE_PHONECALLS_QUERY)).thenThrow(new SQLException());
        phoneCallMySQLDao.delete(1);
    }
//****************************************************************getPhoneCallsFromUserIdBetweenDates************************************************************************************
    @Test
    public void testGetPhonecallsFromUserIdBetweenDatesOk() throws SQLException {
        when(connection.prepareCall("call sp_phonecalls_betweendates(?,?,?)")).thenReturn(cs);
        doNothing().when(cs).setInt(1, 1);
        doNothing().when(cs).setString(2, "2001-05-05");
        doNothing().when(cs).setString(3, "2001-05-05");
        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("line_number_from")).thenReturn("1");
        when(resultSet.getString("line_number_to")).thenReturn("2");
        when(resultSet.getInt("id_city_from_fk")).thenReturn(1);
        when(cityMySQLDao.getCityName(1)).thenReturn("mdp");
        when(resultSet.getInt("id_city_to_fk")).thenReturn(2);
        when(cityMySQLDao.getCityName(2)).thenReturn("bsas");
        when(resultSet.getInt("duration")).thenReturn(3);
        when(resultSet.getInt("total_price")).thenReturn(10);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        PhoneCallsBetweenDatesDto phonecallDto = new PhoneCallsBetweenDatesDto("2001-05-05","2001-05-05");
        List<ReturnedPhoneCallDto> allPhoneCalls = phoneCallMySQLDao.getPhoneCallsFromUserIdBetweenDates(phonecallDto,1);

        assertEquals("1", allPhoneCalls.get(0).getLineNumberFrom());
        assertEquals("2", allPhoneCalls.get(0).getLineNumberTo());
        assertEquals("mdp", allPhoneCalls.get(0).getCityFrom());
        assertEquals("bsas", allPhoneCalls.get(0).getCityTo());
        assertEquals(Integer.valueOf(3), allPhoneCalls.get(0).getDuration());
        assertEquals(Integer.valueOf(10), allPhoneCalls.get(0).getTotalPrice());

        verify(connection,times(1)).prepareCall("call sp_phonecalls_betweendates(?,?,?)");
        verify(cs,times(1)).setInt(1,1);
        verify(cs,times(1)).setString(2,"2001-05-05");
        verify(cs,times(1)).setString(3,"2001-05-05");
        verify(cs,times(1)).executeQuery();

    }

    @Test
    public void testGetPhonecallsFromUserIdBetweenDatesNoContent() throws SQLException {
        when(connection.prepareCall("call sp_phonecalls_betweendates(?,?,?)")).thenReturn(cs);
        doNothing().when(cs).setInt(1, 1);
        doNothing().when(cs).setString(2, "2001-05-05");
        doNothing().when(cs).setString(3, "2001-05-05");
        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        PhoneCallsBetweenDatesDto phonecallDto = new PhoneCallsBetweenDatesDto("2001-05-05","2001-05-05");
        List<ReturnedPhoneCallDto> allPhoneCalls = phoneCallMySQLDao.getPhoneCallsFromUserIdBetweenDates(phonecallDto,1);

        assertEquals(0, allPhoneCalls.size());

        verify(connection,times(1)).prepareCall("call sp_phonecalls_betweendates(?,?,?)");
        verify(cs,times(1)).setInt(1,1);
        verify(cs,times(1)).setString(2,"2001-05-05");
        verify(cs,times(1)).setString(3,"2001-05-05");
        verify(cs,times(1)).executeQuery();

    }
    @Test(expected = RuntimeException.class)
    public void testGetPhonecallsFromUserIdBetweenDatesSQLException() throws SQLException {
        when(connection.prepareCall("call sp_phonecalls_betweendates(?,?,?)")).thenThrow(new SQLException());
        PhoneCallsBetweenDatesDto phonecallDto = new PhoneCallsBetweenDatesDto("2001-05-05","2001-05-05");
        phoneCallMySQLDao.getPhoneCallsFromUserIdBetweenDates(phonecallDto,1);
    }
//***************************************************************getAll*************************************************************************************
    @Test
    public void testGetAllOk() throws SQLException {
        when(connection.prepareStatement(BASE_PHONECALLS_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("line_number_from")).thenReturn("1");
        when(resultSet.getString("line_number_to")).thenReturn("2");
        when(resultSet.getInt("id_city_from_fk")).thenReturn(1);
        when(cityMySQLDao.getCityName(1)).thenReturn("mdp");
        when(resultSet.getInt("id_city_to_fk")).thenReturn(2);
        when(cityMySQLDao.getCityName(2)).thenReturn("bsas");
        when(resultSet.getInt("duration")).thenReturn(3);
        when(resultSet.getInt("total_price")).thenReturn(10);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<ReturnedPhoneCallDto> allPhoneCalls = phoneCallMySQLDao.getAll();

        assertEquals("1", allPhoneCalls.get(0).getLineNumberFrom());
        assertEquals("2", allPhoneCalls.get(0).getLineNumberTo());
        assertEquals("mdp", allPhoneCalls.get(0).getCityFrom());
        assertEquals("bsas", allPhoneCalls.get(0).getCityTo());
        assertEquals(Integer.valueOf(3), allPhoneCalls.get(0).getDuration());
        assertEquals(Integer.valueOf(10), allPhoneCalls.get(0).getTotalPrice());

        verify(resultSet,times(1)).getString("line_number_from");
        verify(resultSet,times(1)).getString("line_number_to");
        verify(connection,times(1)).prepareStatement(BASE_PHONECALLS_QUERY);
        verify(preparedStatement,times(1)).executeQuery();

    }

    @Test
    public void testGetAllNoContent() throws SQLException {
        when(connection.prepareStatement(BASE_PHONECALLS_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<ReturnedPhoneCallDto> allPhoneCalls = phoneCallMySQLDao.getAll();

        assertEquals(0, allPhoneCalls.size());

        verify(connection,times(1)).prepareStatement(BASE_PHONECALLS_QUERY);
        verify(preparedStatement,times(1)).executeQuery();

    }
    @Test(expected = RuntimeException.class)
    public void testGetAllSQLException() throws SQLException {
        when(connection.prepareStatement(BASE_PHONECALLS_QUERY)).thenThrow(new SQLException());
        phoneCallMySQLDao.getAll();
    }
//*************************************************************getMostCalledDestinsByUserId***************************************************************************************
    @Test
    public void testGetMostCalledDestinsByUserIdOk() throws SQLException {
        when(connection.prepareCall("call sp_user_top10(?)")).thenReturn(cs);
        doNothing().when(cs).setInt(1, 1);
        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("line_number_to")).thenReturn("223");

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<String> destins = phoneCallMySQLDao.getMostCalledDestinsByUserId(1);

        assertEquals("223", destins.get(0));
        verify(connection,times(1)).prepareCall("call sp_user_top10(?)");
        verify(cs,times(1)).setInt(1,1);
        verify(cs,times(1)).executeQuery();

    }
    @Test
    public void testGetMostCalledDestinsByUserIdNoContent() throws SQLException {
        when(connection.prepareCall("call sp_user_top10(?)")).thenReturn(cs);
        doNothing().when(cs).setInt(1, 1);
        when(cs.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<String> destins = phoneCallMySQLDao.getMostCalledDestinsByUserId(1);

        assertEquals(0, destins.size());
        verify(connection,times(1)).prepareCall("call sp_user_top10(?)");
        verify(cs,times(1)).setInt(1,1);
        verify(cs,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetMostCalledDestinsByUserIdOSQLException() throws SQLException {
        when(connection.prepareCall("call sp_user_top10(?)")).thenThrow(new SQLException());
        phoneCallMySQLDao.getMostCalledDestinsByUserId(1);
    }
//***********************************************************GetAllPhoneCallsFromUserId*****************************************************************************************

    @Test
    public void testGetAllPhoneCallsFromUserIdOk() throws SQLException {
        when(connection.prepareStatement(GETBYID_USERPHONECALLS_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("line_number_from")).thenReturn("1");
        when(resultSet.getString("line_number_to")).thenReturn("2");
        when(resultSet.getInt("id_city_from_fk")).thenReturn(1);
        when(cityMySQLDao.getCityName(1)).thenReturn("mdp");
        when(resultSet.getInt("id_city_to_fk")).thenReturn(2);
        when(cityMySQLDao.getCityName(2)).thenReturn("bsas");
        when(resultSet.getInt("duration")).thenReturn(3);
        when(resultSet.getInt("total_price")).thenReturn(10);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<ReturnedPhoneCallDto> allPhoneCallsFromUserId = phoneCallMySQLDao.getAllPhoneCallsFromUserId(1);

        assertEquals("1", allPhoneCallsFromUserId.get(0).getLineNumberFrom());
        assertEquals("2", allPhoneCallsFromUserId.get(0).getLineNumberTo());
        assertEquals("mdp", allPhoneCallsFromUserId.get(0).getCityFrom());
        assertEquals("bsas", allPhoneCallsFromUserId.get(0).getCityTo());
        assertEquals(Integer.valueOf(3), allPhoneCallsFromUserId.get(0).getDuration());
        assertEquals(Integer.valueOf(10), allPhoneCallsFromUserId.get(0).getTotalPrice());

        verify(resultSet,times(1)).getString("line_number_from");
        verify(resultSet,times(1)).getString("line_number_to");
        verify(connection,times(1)).prepareStatement(GETBYID_USERPHONECALLS_QUERY);
        verify(preparedStatement,times(1)).setInt(1,1);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test
    public void testGetAllPhoneCallsFromUserIdNoContent() throws SQLException {
        when(connection.prepareStatement(GETBYID_USERPHONECALLS_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<ReturnedPhoneCallDto> allPhoneCallsFromUserId = phoneCallMySQLDao.getAllPhoneCallsFromUserId(1);

        assertEquals(0, allPhoneCallsFromUserId.size());
        verify(connection,times(1)).prepareStatement(GETBYID_USERPHONECALLS_QUERY);
        verify(preparedStatement,times(1)).setInt(1,1);
        verify(preparedStatement,times(1)).executeQuery();
    }

    @Test(expected = RuntimeException.class)
    public void testGetAllPhoneCallsFromUserIdOSQLException() throws SQLException {
        when(connection.prepareStatement(GETBYID_USERPHONECALLS_QUERY)).thenThrow(new SQLException());
        phoneCallMySQLDao.getAllPhoneCallsFromUserId(1);
    }
//**************************************************************GetById**************************************************************************************
    @Test
    public void testGetByIdOk() throws SQLException {


    }

    @Test
    public void testGetByIdNoContent() throws SQLException {
        when(connection.prepareStatement(GETBYID_PHONECALLS_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        ReturnedPhoneCallDto byId = phoneCallMySQLDao.getById(1);

        assertEquals(null, byId);
        verify(connection,times(1)).prepareStatement(GETBYID_PHONECALLS_QUERY);
        verify(preparedStatement,times(1)).setInt(1,1);
        verify(preparedStatement,times(1)).executeQuery();

    }

    @Test(expected = RuntimeException.class)
    public void getByIdSQLException() throws SQLException {
        when(connection.prepareStatement(GETBYID_PHONECALLS_QUERY)).thenThrow(new SQLException());
        phoneCallMySQLDao.getById(1);
    }
//****************************************************************************************************************************************************
}

package utn.dao.mysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.model.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static utn.dao.mysql.MySQLUtils.GETBYID_CITY_QUERY;

public class CityMySQLDaoTest {

    CityMySQLDao cityMySQLDao;
    @Mock
    Connection con;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;

    @Before
    public void setUp() {
        initMocks(this);
        cityMySQLDao = new CityMySQLDao(con);
    }
//*********************************************************************************************************************

    @Test
    public void testGetByIdOk() throws SQLException {
        when(con.prepareStatement(GETBYID_CITY_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("id_city")).thenReturn(1);
        when(resultSet.getString("city_name")).thenReturn("mdp");
        when(resultSet.getInt("prefix")).thenReturn(223);
        when(resultSet.getInt("id_province")).thenReturn(2);
        when(resultSet.getString("province_name")).thenReturn("bsas");

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        City byId = cityMySQLDao.getById(1);

        assertEquals("mdp", byId.getCityName());
        assertEquals("bsas", byId.getProvince().getProvinceName());

        verify(resultSet, times(2)).getString(anyString());
        verify(resultSet, times(3)).getInt(anyString());
        verify(con, times(1)).prepareStatement(GETBYID_CITY_QUERY);
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetByIdNoContent() throws SQLException {
        when(con.prepareStatement(GETBYID_CITY_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        City byId = cityMySQLDao.getById(1);

        assertEquals(null, byId);

        verify(con, times(1)).prepareStatement(GETBYID_CITY_QUERY);
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test(expected = RuntimeException.class)
    public void testGetByIdSQLException() throws SQLException {
        when(con.prepareStatement(GETBYID_CITY_QUERY)).thenThrow(new SQLException());
        cityMySQLDao.getById(1);
    }

    //*********************************************getCityName************************************************************************
    @Test
    public void testGetCityNameOk() throws SQLException {
        when(con.prepareStatement("select city_name from cities where id_city=?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getString("city_name")).thenReturn("mdp");

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        String byId = cityMySQLDao.getCityName(1);

        assertEquals("mdp", byId);


        verify(resultSet, times(1)).getString(anyString());
        verify(con, times(1)).prepareStatement("select city_name from cities where id_city=?");
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetCityNameNoContent() throws SQLException {
        when(con.prepareStatement("select city_name from cities where id_city=?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        String byId = cityMySQLDao.getCityName(1);

        assertEquals("", byId);

        verify(con, times(1)).prepareStatement("select city_name from cities where id_city=?");
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test(expected = RuntimeException.class)
    public void testGetCityNameSQLException() throws SQLException {
        when(con.prepareStatement("select city_name from cities where id_city=?")).thenThrow(new SQLException());
        cityMySQLDao.getCityName(1);
    }
//****************************************************getIdByName*****************************************************************
    @Test
    public void testGetIdByNameOk() throws SQLException {
        when(con.prepareStatement("select id_city from cities where city_name=?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1, "asd");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("id_city")).thenReturn(1);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        Integer byId = cityMySQLDao.getIdByName("asd");

        assertEquals(Integer.valueOf(1), byId);


        verify(resultSet, times(1)).getInt("id_city");
        verify(con, times(1)).prepareStatement("select id_city from cities where city_name=?");
        verify(preparedStatement, times(1)).setString(1, "asd");
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetIdByNameNoContent() throws SQLException {
        when(con.prepareStatement("select id_city from cities where city_name=?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1, "asd");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);


        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        Integer byId = cityMySQLDao.getIdByName("asd");

        assertEquals(null, byId);


        verify(con, times(1)).prepareStatement("select id_city from cities where city_name=?");
        verify(preparedStatement, times(1)).setString(1, "asd");
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test(expected = RuntimeException.class)
    public void testGetIdByNameSQLException() throws SQLException {
        when(con.prepareStatement("select id_city from cities where city_name=?")).thenThrow(new SQLException());
        cityMySQLDao.getIdByName("asd");
    }

}

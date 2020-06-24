package utn.dao.mysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dto.GetRateCityDto;
import utn.dto.RateDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.model.City;
import utn.model.Province;
import utn.model.Rate;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static utn.dao.mysql.MySQLUtils.*;

public class RateMySQLDaoTest {

    RateMySQLDao rateMySQLDao;
    @Mock
    Connection con;
    @Mock
    CityMySQLDao cityMySQLDao;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;

    @Before
    public void setUp() {
        initMocks(this);
        rateMySQLDao = new RateMySQLDao(con, cityMySQLDao);
    }

//**********************************************************Add***************************************************************
    @Test
    public void testAddOk() throws SQLException {
        when(con.prepareStatement(INSERT_RATES_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setFloat(1, 10);
        doNothing().when(preparedStatement).setFloat(2, 8);
        doNothing().when(preparedStatement).setInt(3, 1);
        doNothing().when(preparedStatement).setInt(4, 2);

        when(preparedStatement.execute()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        Rate rate = new Rate(1,10,10,new City(1,"mdp",223,new Province()),new City(2,"bsas",011,new Province()));
        Rate newRate = rateMySQLDao.add(rate);

        assertEquals(Integer.valueOf(1), newRate.getCityFrom().getId());
        assertEquals(Integer.valueOf(2), newRate.getCityTo().getId());

        verify(con,times(1)).prepareStatement(INSERT_RATES_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
        verify(preparedStatement,times(2)).setInt(anyInt(),anyInt());
        verify(preparedStatement,times(2)).setFloat(anyInt(),anyFloat());
        verify(preparedStatement,times(1)).execute();
        verify(preparedStatement,times(1)).getGeneratedKeys();

    }
    @Test(expected = RuntimeException.class)
    public void testAddSQLException() throws SQLException {
        when(con.prepareStatement(INSERT_RATES_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
        Rate rate = new Rate(1,10,10,new City(1,"mdp",223,new Province()),new City(2,"bsas",011,new Province()));
        Rate newRate = rateMySQLDao.add(rate);
    }
//**************************************************************Update***********************************************************
    @Test
    public void testUpdateOk() throws SQLException {
        when(con.prepareStatement(UPDATE_RATES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setFloat(1, 10);
        doNothing().when(preparedStatement).setFloat(2, 8);
        doNothing().when(preparedStatement).setInt(3, 1);
        doNothing().when(preparedStatement).setInt(4, 2);
        doNothing().when(preparedStatement).setInt(5, 3);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        doNothing().when(preparedStatement).close();

        Rate rate = new Rate(3,10,10,new City(1,"mdp",223,new Province()),new City(2,"bsas",011,new Province()));
        rateMySQLDao.update(rate);

        verify(con,times(1)).prepareStatement(UPDATE_RATES_QUERY);
        verify(preparedStatement,times(3)).setInt(anyInt(),anyInt());
        verify(preparedStatement,times(2)).setFloat(anyInt(),anyFloat());
        verify(preparedStatement,times(1)).executeUpdate();
    }
    @Test(expected = RuntimeException.class)
    public void testUpdateSQLException() throws SQLException {
        when(con.prepareStatement(UPDATE_RATES_QUERY)).thenThrow(new SQLException());
        Rate rate = new Rate(3,10,10,new City(1,"mdp",223,new Province()),new City(2,"bsas",011,new Province()));
        rateMySQLDao.update(rate);
    }
//*************************************************************Delete************************************************************
    @Test
    public void testDeleteOk() throws SQLException {
        when(con.prepareStatement(REMOVE_RATES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        doNothing().when(preparedStatement).close();
        rateMySQLDao.delete(1);
    }
    @Test(expected = RuntimeException.class)
    public void testDeleteSQLException() throws SQLException {
        when(con.prepareStatement(REMOVE_RATES_QUERY)).thenThrow(new SQLException());
        rateMySQLDao.delete(1);
    }
//************************************************************getById*************************************************************
    @Test
    public void testGetByIdOk() throws SQLException {
        when(con.prepareStatement(GETBYID_RATES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getFloat("price_per_min")).thenReturn(10f);
        when(resultSet.getFloat("cost_per_min")).thenReturn(8f);
        when(resultSet.getInt("id_city_from_fk")).thenReturn(1);
        when(cityMySQLDao.getCityName(1)).thenReturn("mdp");
        when(resultSet.getInt("id_city_to_fk")).thenReturn(2);
        when(cityMySQLDao.getCityName(2)).thenReturn("bsas");

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        RateDto byId = rateMySQLDao.getById(1);

        assertEquals("mdp", byId.getCityFrom());
        assertEquals("bsas", byId.getCityTo());

        verify(resultSet,times(2)).getFloat(anyString());
        verify(resultSet,times(2)).getInt(anyString());
        verify(con,times(1)).prepareStatement(GETBYID_RATES_QUERY);
        verify(preparedStatement,times(1)).setInt(1,1);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test
    public void testGetByIdNoContent() throws SQLException {
        when(con.prepareStatement(GETBYID_RATES_QUERY)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, 1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        RateDto byId = rateMySQLDao.getById(1);

        assertEquals(null, byId);

        verify(con,times(1)).prepareStatement(GETBYID_RATES_QUERY);
        verify(preparedStatement,times(1)).setInt(1,1);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetByIdSQLException() throws SQLException {
        when(con.prepareStatement(GETBYID_RATES_QUERY)).thenThrow(new SQLException());
        rateMySQLDao.getById(1);
    }
//*************************************************************getAll************************************************************
    @Test
    public void testGetAllOk() throws SQLException {
        when(con.prepareStatement(BASE_RATES_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getFloat("price_per_min")).thenReturn(10f);
        when(resultSet.getFloat("cost_per_min")).thenReturn(8f);
        when(resultSet.getInt("id_city_from_fk")).thenReturn(1);
        when(cityMySQLDao.getCityName(1)).thenReturn("mdp");
        when(resultSet.getInt("id_city_to_fk")).thenReturn(2);
        when(cityMySQLDao.getCityName(2)).thenReturn("bsas");


        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<RateDto> list = rateMySQLDao.getAll();

        assertEquals("mdp", list.get(0).getCityFrom());
        assertEquals("bsas", list.get(0).getCityTo());

        verify(resultSet,times(2)).getFloat(anyString());
        verify(resultSet,times(2)).getInt(anyString());
        verify(con,times(1)).prepareStatement(BASE_RATES_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test
    public void testGetAllNoContent() throws SQLException {
        when(con.prepareStatement(BASE_RATES_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        List<RateDto> list = rateMySQLDao.getAll();

        assertEquals(0, list.size());

        verify(con,times(1)).prepareStatement(BASE_RATES_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetAllSQLException() throws SQLException {
        when(con.prepareStatement(BASE_RATES_QUERY)).thenThrow(new SQLException());
        rateMySQLDao.getAll();
    }
//******************************************************GetRateByCity*******************************************************************
    @Test
    public void testGetRateByCityOk() throws SQLException {
        when(con.prepareStatement(GET_RATE_BY_CITY_QUERY)).thenReturn(preparedStatement);
        when(cityMySQLDao.getIdByName("mdp")).thenReturn(1);
        when(cityMySQLDao.getIdByName("bs as")).thenReturn(2);
        doNothing().when(preparedStatement).setInt(1,1);
        doNothing().when(preparedStatement).setInt(2,2);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getFloat("price_per_min")).thenReturn(10f);
        when(resultSet.getFloat("cost_per_min")).thenReturn(8f);
        when(resultSet.getInt("id_city_from_fk")).thenReturn(1);
        when(cityMySQLDao.getCityName(1)).thenReturn("mdp");
        when(resultSet.getInt("id_city_to_fk")).thenReturn(2);
        when(cityMySQLDao.getCityName(2)).thenReturn("bsas");

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        GetRateCityDto dto = new GetRateCityDto("mdp","bs as");
        List<RateDto> list = rateMySQLDao.getRateByCity(dto);

        assertEquals("mdp", list.get(0).getCityFrom());
        assertEquals("bsas", list.get(0).getCityTo());

        verify(resultSet,times(2)).getFloat(anyString());
        verify(resultSet,times(2)).getInt(anyString());
        verify(con,times(1)).prepareStatement(GET_RATE_BY_CITY_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test
    public void testGetRateByCityNoContent() throws SQLException {
        when(con.prepareStatement(GET_RATE_BY_CITY_QUERY)).thenReturn(preparedStatement);
        when(cityMySQLDao.getIdByName("mdp")).thenReturn(1);
        when(cityMySQLDao.getIdByName("bs as")).thenReturn(2);
        doNothing().when(preparedStatement).setInt(1,1);
        doNothing().when(preparedStatement).setInt(2,2);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        doNothing().when(resultSet).close();
        doNothing().when(preparedStatement).close();

        GetRateCityDto dto = new GetRateCityDto("mdp","bs as");
        List<RateDto> list = rateMySQLDao.getRateByCity(dto);

        assertEquals(0, list.size());

        verify(con,times(1)).prepareStatement(GET_RATE_BY_CITY_QUERY);
        verify(preparedStatement,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetRateByCitySQLException() throws SQLException {
        when(con.prepareStatement(GET_RATE_BY_CITY_QUERY)).thenThrow(new SQLException());
        GetRateCityDto dto = new GetRateCityDto("mdp","bs as");
        rateMySQLDao.getRateByCity(dto);
    }




}

package utn.dao.mysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dao.mysql.CityMySQLDao;
import utn.dao.mysql.UserMySQLDao;
import utn.dto.UserDto;
import utn.dto.UserMostCalledNumberDto;
import utn.model.*;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static utn.dao.mysql.MySQLUtils.*;

public class UserMySQLDaoTest {

    UserMySQLDao dao;
    @Mock
    Connection con;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;
    @Mock
    CityMySQLDao cmd;
    @Mock
    UserLineMySQLDao userLineMySQLDao;

    @Before
    public void setUp() {
        initMocks(this);
        dao = new UserMySQLDao(con, cmd,userLineMySQLDao);
    }
//*********************************************************getByUsername*********************************************************************
    @Test
    public void testGetByUserNameOk() throws SQLException {
        when(con.prepareStatement(GET_BY_USERNAME_USER_QUERY)).thenReturn(ps);
        doNothing().when(ps).setString(1, "user");
        doNothing().when(ps).setString(1, "pwd");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id_user")).thenReturn(1);
        when(rs.getInt("id_city")).thenReturn(2);
        when(rs.getInt("id_province")).thenReturn(3);

        when(rs.getString("user_type")).thenReturn("CLIENT");
        when(rs.getString("user_status")).thenReturn("ACTIVE");
        when(rs.getString("username")).thenReturn("username");
        when(rs.getString("pwd")).thenReturn("pwd");
        UserType userType = UserType.CLIENT;
        UserStatus userStatus = UserStatus.ACTIVE;

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        User u = dao.getByUserName("user", "pwd");

        assertEquals("username", u.getUsername());
        assertEquals("pwd", u.getPwd());
        assertEquals(Integer.valueOf(1), u.getId());
        assertEquals(Integer.valueOf(2), u.getCity().getId());
        assertEquals(Integer.valueOf(3), u.getCity().getProvince().getId());
        assertEquals(userType, u.getUserType());
        assertEquals(userStatus, u.getUserStatus());

        verify(ps,times(1)).setString(1,"user");
        verify(ps,times(1)).setString(2,"pwd");
    }

    @Test
    public void testGetByUserNameNoContent() throws SQLException {
        when(con.prepareStatement(GET_BY_USERNAME_USER_QUERY)).thenReturn(ps);
        doNothing().when(ps).setString(1, "user");
        doNothing().when(ps).setString(1, "pwd");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        User u = dao.getByUserName("user", "pwd");

        assertEquals(null, u);
        verify(ps,times(1)).setString(1,"user");
        verify(ps,times(1)).setString(2,"pwd");
    }
    @Test(expected = RuntimeException.class)
    public void testGetByUserNameSQLException() throws SQLException {
        when(con.prepareStatement(GET_BY_USERNAME_USER_QUERY)).thenThrow(new SQLException());
        dao.getByUserName("user", "pwd");
    }
//*****************************************************ADD*************************************************************************
    @Test
    public void testAddOk() throws SQLException {
        when(con.prepareStatement(INSERT_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(ps);
        doNothing().when(ps).setString(1, "jor");
        doNothing().when(ps).setString(2, "vill");
        doNothing().when(ps).setInt(3, 123);
        doNothing().when(ps).setDate(4, new Date(2005,05,05));
        doNothing().when(ps).setString(5, "user");
        doNothing().when(ps).setString(6, "pwd");
        doNothing().when(ps).setString(7, "email");
        doNothing().when(ps).setString(8, "CLIENT");
        doNothing().when(ps).setString(9, "ACTIVE");
        doNothing().when(ps).setInt(10, 1);

        when(ps.execute()).thenReturn(true);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);
        UserType userType = UserType.CLIENT;
        UserStatus userStatus = UserStatus.ACTIVE;

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005,05,05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2,"mdp",223,
                        new Province(3,"bsas")));
        User user = dao.add(u);

        assertEquals("user", user.getUsername());
        assertEquals("pwd", user.getPwd());
        assertEquals(Integer.valueOf(1), user.getId());
        assertEquals(Integer.valueOf(2), user.getCity().getId());
        assertEquals(Integer.valueOf(3), user.getCity().getProvince().getId());
        assertEquals(userType, user.getUserType());
        assertEquals(userStatus, user.getUserStatus());

        verify(ps,times(7)).setString(anyInt(),anyString());

    }

    @Test(expected = RuntimeException.class)
    public void testAddSQLException() throws SQLException {
        when(con.prepareStatement(INSERT_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005,05,05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2,"mdp",223,
                        new Province(3,"bsas")));
        dao.add(u);
    }
//**********************************************************getMostCalledNumber********************************************************************
    @Test
    public void testGetMostCalledNumberOk() throws SQLException {
        when(con.prepareStatement(GET_MOST_CALLED_NUMBER)).thenReturn(ps);
        doNothing().when(ps).setString(1, "223");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);


        when(rs.getString("line_number_to")).thenReturn("321");
        when(rs.getString("first_name")).thenReturn("jor");
        when(rs.getString("surname")).thenReturn("vill");

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        UserMostCalledNumberDto u = dao.getMostCalledNumber( "223");

        assertEquals("321", u.getLineNumber());
        assertEquals("jor", u.getName());
        assertEquals("vill", u.getSurname());

        verify(ps,times(1)).setString(1,"223");
        verify(con,times(1)).prepareStatement(GET_MOST_CALLED_NUMBER);
        verify(ps,times(1)).executeQuery();
    }

    @Test
    public void testGetMostCalledNumberNoContent() throws SQLException {
        when(con.prepareStatement(GET_MOST_CALLED_NUMBER)).thenReturn(ps);
        doNothing().when(ps).setString(1, "223");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        UserMostCalledNumberDto u = dao.getMostCalledNumber( "223");

        assertEquals( null, u);

        verify(ps,times(1)).setString(1,"223");
        verify(con,times(1)).prepareStatement(GET_MOST_CALLED_NUMBER);
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetMostCalledNumberSQLException() throws SQLException {
        when(con.prepareStatement(GET_MOST_CALLED_NUMBER)).thenThrow(new SQLException());
        dao.getMostCalledNumber( "223");
    }
//*******************************************************update***********************************************************************
    @Test
    public void testUpdateOk() throws SQLException {
        when(con.prepareStatement(UPDATE_USER_QUERY)).thenReturn(ps);
        doNothing().when(ps).setString(1, "jor");
        doNothing().when(ps).setString(2, "vill");
        doNothing().when(ps).setInt(3, 123);
        doNothing().when(ps).setDate(4, new Date(2005,05,05));
        doNothing().when(ps).setString(5, "user");
        doNothing().when(ps).setString(6, "pwd");
        doNothing().when(ps).setString(7, "email");
        doNothing().when(ps).setString(8, "CLIENT");
        doNothing().when(ps).setString(9, "ACTIVE");
        doNothing().when(ps).setInt(10, 2);
        doNothing().when(ps).setInt(11, 1);

        when(ps.executeUpdate()).thenReturn(1);

        doNothing().when(ps).close();
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005,05,05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2,"mdp",223,
                        new Province(3,"bsas")));
        dao.update(u);
        verify(ps,times(7)).setString(anyInt(),anyString());
        verify(ps,times(3)).setInt(anyInt(),anyInt());

    }

    @Test(expected = RuntimeException.class)
    public void testUpdateSQLException() throws SQLException {
        when(con.prepareStatement(UPDATE_USER_QUERY)).thenThrow(new SQLException());
        User u = new User(1,
                "jor",
                "vill",
                123,
                new Date(2005,05,05),
                "user",
                "pwd",
                "email",
                UserType.valueOf("CLIENT"),
                UserStatus.valueOf("ACTIVE"),
                new City(2,"mdp",223,
                        new Province(3,"bsas")));
        dao.update(u);

    }
//******************************************************Delete************************************************************************
    @Test
    public void testDeleteOk() throws SQLException {
        when(con.prepareStatement(UPDATE_USER_STATUS_QUERY)).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);
        when(ps.executeUpdate()).thenReturn(1);
        doNothing().when(ps).close();
        dao.delete(1);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteSQLException() throws SQLException {
        when(con.prepareStatement(UPDATE_USER_STATUS_QUERY)).thenThrow(new SQLException());
        dao.delete(1);

    }
//**************************************************************getById****************************************************************
    @Test
    public void testGetByIdOk() throws SQLException {
        when(con.prepareStatement(GET_BY_ID_USER_QUERY)).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        when(rs.getString("first_name")).thenReturn("jor");
        when(rs.getString("surname")).thenReturn("vill");
        when(rs.getInt("dni")).thenReturn(321);
        when(rs.getString("username")).thenReturn("villo");
        when(rs.getString("email")).thenReturn("vill@");
        when(rs.getInt("id_city_fk")).thenReturn(1);
        when(cmd.getCityName(1)).thenReturn("mdp");

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        UserDto u = dao.getById( 1);

        assertEquals("jor", u.getFirstName());
        assertEquals(Integer.valueOf(321), u.getDni());
        assertEquals("vill", u.getSurname());
        assertEquals("villo", u.getUsername());
        assertEquals("vill@", u.getEmail());
        assertEquals("mdp", u.getCityName());

        verify(ps,times(1)).setInt(1,1);
        verify(con,times(1)).prepareStatement(GET_BY_ID_USER_QUERY);
        verify(ps,times(1)).executeQuery();
    }

    @Test
    public void testGetByIdNoContent() throws SQLException {
        when(con.prepareStatement(GET_BY_ID_USER_QUERY)).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        UserDto u = dao.getById( 1);

        assertEquals(null, u);

        verify(ps,times(1)).setInt(1,1);
        verify(con,times(1)).prepareStatement(GET_BY_ID_USER_QUERY);
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetByIdSQLException() throws SQLException {
        when(con.prepareStatement(GET_BY_ID_USER_QUERY)).thenThrow(new SQLException());
        dao.getById( 1);
    }
    //*******************************************************getByUsername***********************************************************************
    @Test
    public void testGetByUsernameOk() throws SQLException {
        when(con.prepareStatement(GET_BY_USERNAME)).thenReturn(ps);
        doNothing().when(ps).setString(1, "username");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        boolean bool = dao.getByUsername( "username");

        assertEquals(true, bool);

        verify(ps,times(1)).setString(1,"username");
        verify(con,times(1)).prepareStatement(GET_BY_USERNAME);
        verify(ps,times(1)).executeQuery();
    }

    @Test
    public void testGetByUsernameNoContent() throws SQLException {
        when(con.prepareStatement(GET_BY_USERNAME)).thenReturn(ps);
        doNothing().when(ps).setString(1, "username");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        boolean bool = dao.getByUsername( "username");

        assertEquals(false, bool);

        verify(ps,times(1)).setString(1,"username");
        verify(con,times(1)).prepareStatement(GET_BY_USERNAME);
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetByUsernameSQLException() throws SQLException {
        when(con.prepareStatement(GET_BY_USERNAME)).thenThrow(new SQLException());
        dao.getByUsername( "username");
    }
    //**********************************************************getAll********************************************************************
    @Test
    public void testGetAllUsersOk() throws SQLException {
        when(con.prepareStatement(BASE_USER_QUERY)).thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        when(rs.getString("first_name")).thenReturn("jor");
        when(rs.getString("surname")).thenReturn("vill");
        when(rs.getInt("dni")).thenReturn(321);
        when(rs.getString("username")).thenReturn("villo");
        when(rs.getString("email")).thenReturn("vill@");
        when(rs.getInt("id_city_fk")).thenReturn(1);
        when(cmd.getCityName(1)).thenReturn("mdp");

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        List<UserDto> list = dao.getAll();

        assertEquals("jor", list.get(0).getFirstName());
        assertEquals(Integer.valueOf(321),list.get(0).getDni());
        assertEquals("vill", list.get(0).getSurname());
        assertEquals("villo", list.get(0).getUsername());
        assertEquals("vill@", list.get(0).getEmail());
        assertEquals("mdp", list.get(0).getCityName());

        verify(con,times(1)).prepareStatement(BASE_USER_QUERY);
        verify(ps,times(1)).executeQuery();
    }

    @Test
    public void testGetAllNoContent() throws SQLException {
        when(con.prepareStatement(BASE_USER_QUERY)).thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        List<UserDto> list = dao.getAll();

        assertEquals(0, list.size());

        verify(con,times(1)).prepareStatement(BASE_USER_QUERY);
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetAllSQLException() throws SQLException {
        when(con.prepareStatement(BASE_USER_QUERY)).thenThrow(new SQLException());
        dao.getAll();
    }
//*****************************************************************getAll*************************************************************
}

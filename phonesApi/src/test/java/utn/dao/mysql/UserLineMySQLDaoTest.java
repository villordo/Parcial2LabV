package utn.dao.mysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dto.UserDto;
import utn.dto.UserLineDto;
import utn.exceptions.AlreadyExistsException;
import utn.model.City;
import utn.model.Province;
import utn.model.User;
import utn.model.UserLine;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static utn.dao.mysql.MySQLUtils.*;
import static utn.dao.mysql.MySQLUtils.BASE_USER_QUERY;

public class UserLineMySQLDaoTest {

    UserLineMySQLDao userLineMySQLDao;
    @Mock
    Connection con;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;

    @Before
    public void setUp() {
        initMocks(this);
        userLineMySQLDao = new UserLineMySQLDao(con);
    }
//***********************************************************getUserLineByNumber**************************************************************

    @Test
    public void testGetUserLineByNumberOk() throws SQLException {
        when(con.prepareStatement(GET_USERLINE_BYNUMBER_QUERY)).thenReturn(ps);
        doNothing().when(ps).setString(1, "223");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        boolean bool = userLineMySQLDao.getUserLineByNumber( "223");

        assertEquals(true, bool);

        verify(ps,times(1)).setString(1,"223");
        verify(con,times(1)).prepareStatement(GET_USERLINE_BYNUMBER_QUERY);
        verify(ps,times(1)).executeQuery();
    }
    @Test
    public void testGetUserLineByNumberNoContent() throws SQLException {
        when(con.prepareStatement(GET_USERLINE_BYNUMBER_QUERY)).thenReturn(ps);
        doNothing().when(ps).setString(1, "223");

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        boolean bool = userLineMySQLDao.getUserLineByNumber( "223");

        assertEquals(false, bool);

        verify(ps,times(1)).setString(1,"223");
        verify(con,times(1)).prepareStatement(GET_USERLINE_BYNUMBER_QUERY);
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetUserLineByNumberSQLException() throws SQLException {
        when(con.prepareStatement(GET_USERLINE_BYNUMBER_QUERY)).thenThrow(new SQLException());
        userLineMySQLDao.getUserLineByNumber( "223");
    }
//*********************************************************GetAll****************************************************************
    @Test
    public void testGetAllOk() throws SQLException {
        when(con.prepareStatement(BASE_USERLINE_QUERY)).thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        when(rs.getString("line_number")).thenReturn("223");
        when(rs.getString("type_line")).thenReturn("MOBILE");
        when(rs.getString("line_status")).thenReturn("ACTIVE");
        when(rs.getString("first_name")).thenReturn("jor");
        when(rs.getString("surname")).thenReturn("vill");
        when(rs.getInt("dni")).thenReturn(321);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        List<UserLineDto> list = userLineMySQLDao.getAll();

        TypeLine typeLine = TypeLine.MOBILE;
        LineStatus lineStatus = LineStatus.ACTIVE;
        assertEquals("jor", list.get(0).getFirstName());
        assertEquals(Integer.valueOf(321),list.get(0).getDni());
        assertEquals("vill", list.get(0).getSurname());
        assertEquals(lineStatus, list.get(0).getLineStatus());
        assertEquals(typeLine, list.get(0).getTypeLine());
        assertEquals("223", list.get(0).getLineNumber());

        verify(con,times(1)).prepareStatement(BASE_USERLINE_QUERY);
        verify(ps,times(1)).executeQuery();
    }
    @Test
    public void testGetAllNoContent() throws SQLException {
        when(con.prepareStatement(BASE_USERLINE_QUERY)).thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        List<UserLineDto> list = userLineMySQLDao.getAll();

        assertEquals(0, list.size());

        verify(con,times(1)).prepareStatement(BASE_USERLINE_QUERY);
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetAllSQLException() throws SQLException {
        when(con.prepareStatement(BASE_USERLINE_QUERY)).thenThrow(new SQLException());
        userLineMySQLDao.getAll();
    }
//*********************************************************getLineNumber****************************************************************
    @Test
    public void testGetLineNumberOk() throws SQLException {
        when(con.prepareStatement("select line_number from user_lines where id_user_line=?")).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        when(rs.getString("line_number")).thenReturn("223");

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        String number = userLineMySQLDao.getLineNumber(1);

        assertEquals("223", number);

        verify(con,times(1)).prepareStatement("select line_number from user_lines where id_user_line=?");
        verify(ps,times(1)).executeQuery();
    }
    @Test
    public void testGetLineNumberNoContent() throws SQLException {
        when(con.prepareStatement("select line_number from user_lines where id_user_line=?")).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        String number = userLineMySQLDao.getLineNumber(1);

        assertEquals("", number);

        verify(con,times(1)).prepareStatement("select line_number from user_lines where id_user_line=?");
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetLineNumberSQLException() throws SQLException {
        when(con.prepareStatement("select line_number from user_lines where id_user_line=?")).thenThrow(new SQLException());
        userLineMySQLDao.getLineNumber(1);
    }
//********************************************************getById*****************************************************************
    @Test
    public void testGetByIdOk() throws SQLException {
        when(con.prepareStatement(GETBYID_USERLINE_QUERY)).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        when(rs.getString("line_number")).thenReturn("223");
        when(rs.getString("type_line")).thenReturn("MOBILE");
        when(rs.getString("line_status")).thenReturn("ACTIVE");
        when(rs.getString("first_name")).thenReturn("jor");
        when(rs.getString("surname")).thenReturn("vill");
        when(rs.getInt("dni")).thenReturn(321);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        UserLineDto userLineDto = userLineMySQLDao.getById(1);

        TypeLine typeLine = TypeLine.MOBILE;
        LineStatus lineStatus = LineStatus.ACTIVE;
        assertEquals("jor", userLineDto.getFirstName());
        assertEquals(Integer.valueOf(321),userLineDto.getDni());
        assertEquals("vill", userLineDto.getSurname());
        assertEquals(lineStatus, userLineDto.getLineStatus());
        assertEquals(typeLine, userLineDto.getTypeLine());
        assertEquals("223", userLineDto.getLineNumber());

        verify(con,times(1)).prepareStatement(GETBYID_USERLINE_QUERY);
        verify(ps,times(1)).setInt(1,1);
        verify(ps,times(1)).executeQuery();
    }
    @Test
    public void testGetByIdNoContent() throws SQLException {
        when(con.prepareStatement(GETBYID_USERLINE_QUERY)).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);

        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(ps).close();
        doNothing().when(rs).close();

        UserLineDto userLineDto = userLineMySQLDao.getById(1);

        assertEquals(null, userLineDto);

        verify(con,times(1)).prepareStatement(GETBYID_USERLINE_QUERY);
        verify(ps,times(1)).setInt(1,1);
        verify(ps,times(1)).executeQuery();
    }
    @Test(expected = RuntimeException.class)
    public void testGetByIdSQLException() throws SQLException {
        when(con.prepareStatement(GETBYID_USERLINE_QUERY)).thenThrow(new SQLException());
        userLineMySQLDao.getById(1);
    }
//*********************************************************Delete****************************************************************
    @Test
    public void testDeleteOk() throws SQLException {
        when(con.prepareStatement(UPDATE_USERLINE_STATUS_QUERY)).thenReturn(ps);
        doNothing().when(ps).setInt(1, 1);
        when(ps.executeUpdate()).thenReturn(1);
        doNothing().when(ps).close();
        userLineMySQLDao.delete(1);
    }
    @Test(expected = RuntimeException.class)
    public void testDeleteSQLException() throws SQLException {
        when(con.prepareStatement(UPDATE_USERLINE_STATUS_QUERY)).thenThrow(new SQLException());
        userLineMySQLDao.delete(1);
    }
//*******************************************************Update******************************************************************
    @Test
    public void testUpdateOk() throws SQLException {
        when(con.prepareStatement(UPDATE_USERLINE_QUERY)).thenReturn(ps);
        doNothing().when(ps).setString(1, "223");
        doNothing().when(ps).setString(2, "MOBILE");
        doNothing().when(ps).setString(3, "ACTIVE");
        doNothing().when(ps).setInt(4, 1);
        doNothing().when(ps).setInt(5, 1);

        when(ps.executeUpdate()).thenReturn(1);
        doNothing().when(ps).close();

        TypeLine typeLine = TypeLine.MOBILE;
        LineStatus lineStatus = LineStatus.ACTIVE;
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
                new City(2,"mdp",223, new Province(3,"bsas")));
        UserLine userLine = new UserLine(1,"223",typeLine,lineStatus,u);
        userLineMySQLDao.update(userLine);

        verify(con,times(1)).prepareStatement(UPDATE_USERLINE_QUERY);
        verify(ps,times(2)).setInt(anyInt(),anyInt());
        verify(ps,times(3)).setString(anyInt(),anyString());
        verify(ps,times(1)).executeUpdate();
    }
    @Test(expected = RuntimeException.class)
    public void testUpdateSQLException() throws SQLException {
        when(con.prepareStatement(UPDATE_USERLINE_QUERY)).thenThrow(new SQLException());
        UserLine userLine = new UserLine(1,"223",null,null,null);
        userLineMySQLDao.update(userLine);
    }
//*************************************************************************************************************************
    @Test
    public void testAddOk() throws SQLException, AlreadyExistsException {
        when(con.prepareStatement(INSERT_USERLINE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(ps);
        doNothing().when(ps).setString(1, "223");
        doNothing().when(ps).setString(2, "MOBILE");
        doNothing().when(ps).setString(3, "ACTIVE");
        doNothing().when(ps).setInt(4, 1);

        when(ps.execute()).thenReturn(true);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        when(rs.getInt(1)).thenReturn(1);

        doNothing().when(rs).close();
        doNothing().when(ps).close();

        TypeLine typeLine = TypeLine.MOBILE;
        LineStatus lineStatus = LineStatus.ACTIVE;
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
                new City(2,"mdp",223, new Province(3,"bsas")));
        UserLine userLine = new UserLine(1,"223",typeLine,lineStatus,u);
        UserLine retorned = userLineMySQLDao.add(userLine);

        assertEquals("jor", retorned.getUser().getFirstname());
        assertEquals(lineStatus, retorned.getLineStatus());
        assertEquals(typeLine, retorned.getTypeLine());
        assertEquals("223", retorned.getLineNumber());

        verify(con,times(1)).prepareStatement(INSERT_USERLINE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
        verify(ps,times(1)).setInt(anyInt(),anyInt());
        verify(ps,times(3)).setString(anyInt(),anyString());
        verify(ps,times(1)).execute();
        verify(ps,times(1)).getGeneratedKeys();
    }

    @Test(expected = RuntimeException.class)
    public void testAddSQLException() throws SQLException, AlreadyExistsException {
        when(con.prepareStatement(INSERT_USERLINE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
        UserLine userLine = new UserLine(1,"223",null,null,null);
        userLineMySQLDao.add(userLine);
    }
//*************************************************************************************************************************

}

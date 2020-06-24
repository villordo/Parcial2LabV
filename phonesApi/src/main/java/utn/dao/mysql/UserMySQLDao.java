package utn.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import utn.dao.UserDao;
import utn.dto.UserDto;
import utn.dto.UserMostCalledNumberDto;
import utn.model.City;
import utn.model.Province;
import utn.model.User;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utn.dao.mysql.MySQLUtils.*;

@Repository
public class UserMySQLDao implements UserDao {

    Connection connection;
    CityMySQLDao cityMySQLDao;
    UserLineMySQLDao userLineMySQLDao;

    @Autowired
    public UserMySQLDao(Connection connection, CityMySQLDao cityMySQLDao, UserLineMySQLDao userLineMySQLDao) {
        this.connection = connection;
        this.cityMySQLDao = cityMySQLDao;
        this.userLineMySQLDao = userLineMySQLDao;
    }

    @Override
    public User getByUserName(String username, String password) {
        try {
            PreparedStatement ps = connection.prepareStatement(GET_BY_USERNAME_USER_QUERY);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = createUser(rs);
            }
            rs.close();
            ps.close();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de usuario", e);
        }
    }
    private User createUser(ResultSet rs) throws SQLException {
        User u = new User(rs.getInt("id_user"),
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getInt("dni"),
                rs.getDate("birthdate"),
                rs.getString("username"),
                rs.getString("pwd"),
                rs.getString("email"),
                UserType.valueOf(rs.getString("user_type")),
                UserStatus.valueOf(rs.getString("user_status")),
                new City(rs.getInt("id_city"), rs.getString("city_name"), rs.getInt("prefix"),
                        new Province(rs.getInt("id_province"), rs.getString("province_name"))));
        return u;
    }

    private UserDto createUserDto(ResultSet rs) throws SQLException {
        UserDto u = new UserDto(rs.getString("first_name"),
                rs.getString("surname"),
                rs.getInt("dni"),
                rs.getString("username"),
                rs.getString("email"),
                cityMySQLDao.getCityName(rs.getInt("id_city_fk"))
        );
        return u;
    }

    @Override
    public List<UserDto> getByCity(City city) {
        return null;
    }

    @Override
    public User add(User value) {
        try {
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, value.getFirstname());
            ps.setString(2, value.getSurname());
            ps.setInt(3, value.getDni());
            ps.setDate(4, new Date(value.getBirthdate().getTime()));
            ps.setString(5, value.getUsername());
            ps.setString(6, value.getPwd());
            ps.setString(7, value.getEmail());
            ps.setString(8, String.valueOf(value.getUserType()));
            ps.setString(9, String.valueOf(value.getUserStatus()));
            ps.setInt(10, value.getCity().getId());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                value.setId(rs.getInt(1));
            }
            ps.close();
            rs.close();
            return value;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario",e);
        }
    }

    @Override
    public UserMostCalledNumberDto getMostCalledNumber(String lineNumber) {
        UserMostCalledNumberDto aux = null;
        try {
            PreparedStatement ps = connection.prepareStatement(GET_MOST_CALLED_NUMBER);
            ps.setString(1, lineNumber);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                aux = new UserMostCalledNumberDto(rs.getString("line_number_to"), rs.getString("first_name"), rs.getString("surname"));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuario",e);
        }
        return aux;
    }

    @Override
    public void update(User value) {
        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_USER_QUERY);
            ps.setString(1, value.getFirstname());
            ps.setString(2, value.getSurname());
            ps.setInt(3, value.getDni());
            ps.setDate(4, new Date(value.getBirthdate().getTime()));
            ps.setString(5, value.getUsername());
            ps.setString(6, value.getPwd());
            ps.setString(7, value.getEmail());
            ps.setString(8, String.valueOf(value.getUserType()));
            ps.setString(9, String.valueOf(value.getUserStatus()));
            ps.setInt(10, value.getCity().getId());
            ps.setInt(11, value.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException sqlException) {
            throw new RuntimeException("Error al modificar usuario", sqlException);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_USER_STATUS_QUERY);
            ps.setInt(1, id);
            userLineMySQLDao.suspendLines(id);//Al eliminar un usuario tambien debo suspender las lineas que le pertenecia.
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }


    @Override
    public UserDto getById(Integer id) {
        UserDto user = null;

        try {
            PreparedStatement ps = connection.prepareStatement(GET_BY_ID_USER_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = createUserDto(rs);
            }
            rs.close();
            ps.close();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de usuario", e);
        }

    }

    public boolean getByUsername(String username) {
        boolean aswr = false;
        try {
            PreparedStatement ps = connection.prepareStatement(GET_BY_USERNAME);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                aswr = true;
            }
            rs.close();
            ps.close();
            return aswr;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de usuario", e);
        }
    }

    @Override
    public List<UserDto> getAll() {
        try {
            PreparedStatement st = connection.prepareStatement(BASE_USER_QUERY);
            ResultSet rs = st.executeQuery();
            List<UserDto> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(createUserDto(rs));
            }
            st.close();
            rs.close();
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de usuarios", e);
        }
    }
}

package utn.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import utn.dao.UserLineDao;
import utn.dto.UserLineDto;
import utn.exceptions.AlreadyExistsException;
import utn.model.User;
import utn.model.UserLine;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utn.dao.mysql.MySQLUtils.*;

@Repository
public class UserLineMySQLDao implements UserLineDao {
    Connection con;

    @Autowired
    public UserLineMySQLDao(Connection con) {
        this.con = con;
    }


    @Override
    public UserLine add(UserLine userLine) throws AlreadyExistsException {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_USERLINE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userLine.getLineNumber());
            preparedStatement.setString(2, String.valueOf(userLine.getTypeLine()));
            preparedStatement.setString(3, String.valueOf(userLine.getLineStatus()));
            preparedStatement.setInt(4, userLine.getUser().getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                userLine.setId(resultSet.getInt(1));
            }
            preparedStatement.close();
            resultSet.close();
            return userLine;

        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar una linea de usuario",e);
        }
    }

    @Override
    public void update(UserLine userLine) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USERLINE_QUERY);
            preparedStatement.setString(1, userLine.getLineNumber());
            preparedStatement.setString(2, String.valueOf(userLine.getTypeLine()));
            preparedStatement.setString(3, String.valueOf(userLine.getLineStatus()));
            preparedStatement.setInt(4, (userLine.getUser().getId()));
            preparedStatement.setInt(5,userLine.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar usuario", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            PreparedStatement ps = con.prepareStatement(UPDATE_USERLINE_STATUS_QUERY);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la linea de usuario", e);
        }
    }

    @Override
    public UserLineDto getById(Integer id) {
        UserLineDto ul = null;
        try {
            PreparedStatement ps = con.prepareStatement(GETBYID_USERLINE_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ul = createUserLine(rs);
            }
            rs.close();
            ps.close();
            return ul;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de la linea del usuario", e);
        }
    }

    public String getLineNumber(Integer id) {
        String aux = "";
        try {
            PreparedStatement ps = con.prepareStatement("select line_number from user_lines where id_user_line=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                aux = rs.getString("line_number");
            }
            ps.close();
            rs.close();
            return aux;
        } catch (SQLException e) {
            throw new RuntimeException("Error al traer el numbero de la linea", e);
        }
    }

    private UserLineDto createUserLine(ResultSet rs) throws SQLException {
        UserLineDto ul = new UserLineDto(rs.getString("line_number"),
                TypeLine.valueOf(rs.getString("type_line")),
                LineStatus.valueOf(rs.getString("line_status")),
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getInt("dni"));
        return ul;
    }

    @Override
    public List<UserLineDto> getAll() {
        try {
            PreparedStatement st = con.prepareStatement(BASE_USERLINE_QUERY);
            ResultSet rs = st.executeQuery();
            List<UserLineDto> userLineList = new ArrayList<>();
            while (rs.next()) {
                userLineList.add(createUserLine(rs));
            }
            st.close();
            rs.close();
            return userLineList;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de lineas de usuario", e);
        }
    }


    @Override
    public boolean getUserLineByNumber(String lineNumber) {
        PreparedStatement ps = null;
        boolean answer = false;
        try {
            ps = con.prepareStatement(GET_USERLINE_BYNUMBER_QUERY);
            ps.setString(1, lineNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                answer = true;
            }
            ps.close();
            rs.close();
            return answer;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener linea de usuario", e);
        }
    }

    public void suspendLines(Integer id) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(SUSPEND_USERLINE_QUERY);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener ID de cliente", e);
        }
    }
}

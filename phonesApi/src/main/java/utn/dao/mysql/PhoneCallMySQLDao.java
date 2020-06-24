package utn.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import utn.dao.PhoneCallDao;
import utn.dto.PhoneCallDto;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.model.PhoneCall;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utn.dao.mysql.MySQLUtils.*;

@Repository
public class PhoneCallMySQLDao implements PhoneCallDao {
    Connection con;
    CityMySQLDao cityMySQLDao;

    @Autowired
    public PhoneCallMySQLDao(Connection con, CityMySQLDao cityMySQLDao) {
        this.cityMySQLDao = cityMySQLDao;
        this.con = con;
    }

    @Override
    public Integer addPhoneCall(PhoneCallDto value) {
        try {
            Integer idPhonecall=0;
            PreparedStatement ps = con.prepareStatement(INSERT_PHONECALLS_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,value.getLineNumberFrom());
            ps.setString(2,value.getLineNumberTo());
            ps.setInt(3,value.getDuration());
            ps.setDate(4,new Date(value.getDate().getTime()));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                idPhonecall = rs.getInt(1);
            }
            ps.close();
            rs.close();
            return idPhonecall;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar la llamada", e);
        }
    }

    @Override
    public Object add(PhoneCall value) throws AlreadyExistsException {
        return null;
    }

    @Override
    public void update(PhoneCall value) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(UPDATE_PHONECALLS_QUERY);
            ps.setString(1, value.getLineNumberFrom());
            ps.setString(2, value.getLineNumberTo());
            ps.setInt(3, value.getIdLineNumberFrom().getId());
            ps.setInt(4, value.getIdLineNumberTo().getId());
            ps.setInt(5, value.getIdCityFrom().getId());
            ps.setInt(6, value.getIdCityTo().getId());
            ps.setInt(7, value.getDuration());
            ps.setDate(8, new Date(value.getCallDate().getTime()));
            ps.setFloat(9, value.getCostPerMin());
            ps.setFloat(10, value.getPricePerMin());
            ps.setFloat(11, value.getTotalPrice());
            ps.setFloat(12, value.getTotalCost());
            ps.executeQuery();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updatiando datos de la llamada",e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            PreparedStatement ps = con.prepareStatement(REMOVE_PHONECALLS_QUERY);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la llamada", e);
        }
    }

    @Override
    public ReturnedPhoneCallDto getById(Integer id) {
        ReturnedPhoneCallDto returnedPhoneCallDto = null;
        try {
            PreparedStatement ps = con.prepareStatement(GETBYID_PHONECALLS_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                returnedPhoneCallDto = createPhoneCall(rs);
            }
            rs.close();
            ps.close();
            return returnedPhoneCallDto;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de la llamada", e);
        }
    }


    public ReturnedPhoneCallDto createPhoneCall(ResultSet rs) throws SQLException {
        ReturnedPhoneCallDto returnedPhoneCallDto = new ReturnedPhoneCallDto(
                rs.getString("line_number_from"),
                rs.getString("line_number_to"),
                cityMySQLDao.getCityName(rs.getInt("id_city_from_fk")),
                cityMySQLDao.getCityName(rs.getInt("id_city_to_fk")),
                rs.getInt("duration"),
                rs.getDate("call_date"),
                rs.getInt("total_price"));
        return returnedPhoneCallDto;
    }

    public List<ReturnedPhoneCallDto> getPhoneCallsFromUserIdBetweenDates(PhoneCallsBetweenDatesDto phonecallDto,Integer userId){
        try {
            CallableStatement cs = con.prepareCall("call sp_phonecalls_betweendates(?,?,?)");
            cs.setInt(1,userId);
            cs.setString(2,phonecallDto.getDateFrom());
            cs.setString(3,phonecallDto.getDateTo());
            ResultSet rs = cs.executeQuery();
            List<ReturnedPhoneCallDto> phoneCallDtos = new ArrayList<>();
            while (rs.next()) {
                phoneCallDtos.add(createPhoneCall(rs));
            }
            rs.close();
            cs.close();
            return phoneCallDtos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las llamadas por fechas", e);
        }

    }


    @Override
    public List<ReturnedPhoneCallDto> getAll() {
        try {
            PreparedStatement st = con.prepareStatement(BASE_PHONECALLS_QUERY);
            ResultSet rs = st.executeQuery();
            List<ReturnedPhoneCallDto> phoneCallDtos = new ArrayList<>();
            while (rs.next()) {
                phoneCallDtos.add(createPhoneCall(rs));
            }
            rs.close();
            st.close();
            return phoneCallDtos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de llamadas", e);
        }
    }

    @Override
    public List<ReturnedPhoneCallDto> getAllPhoneCallsFromUserId(Integer userId) {
        try {
            PreparedStatement ps = con.prepareStatement(GETBYID_USERPHONECALLS_QUERY);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<ReturnedPhoneCallDto> returnedPhoneCallDtoList = new ArrayList<>();
            while (rs.next()) {
                returnedPhoneCallDtoList.add(createPhoneCall(rs));
            }
            rs.close();
            ps.close();
            return returnedPhoneCallDtoList;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de llamadas por usuario", e);
        }
    }

    @Override
    public List<String> getMostCalledDestinsByUserId(Integer idUser){
        try {
            CallableStatement cs = con.prepareCall("call sp_user_top10(?)");
            cs.setInt(1,idUser);
            ResultSet rs = cs.executeQuery();
            List<String> destinsMostCalled = new ArrayList<>();
            while (rs.next()) {
                destinsMostCalled.add(rs.getString("line_number_to"));
            }
            rs.close();
            cs.close();
            return destinsMostCalled;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los destinos mas llamados", e);
        }
    }
}

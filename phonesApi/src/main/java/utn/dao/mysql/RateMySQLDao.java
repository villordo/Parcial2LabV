package utn.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import utn.dao.RateDao;
import utn.dto.GetRateCityDto;
import utn.dto.RateDto;
import utn.exceptions.AlreadyExistsException;
import utn.model.Rate;
import utn.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utn.dao.mysql.MySQLUtils.*;

@Repository
public class RateMySQLDao implements RateDao {

    Connection con;
    CityMySQLDao cityMySQLDao;

    @Autowired
    public RateMySQLDao(Connection con, CityMySQLDao cityMySQLDao) {
        this.cityMySQLDao = cityMySQLDao;
        this.con = con;
    }

    @Override
    public Rate add(Rate value) {
        try {
            PreparedStatement ps = con.prepareStatement(INSERT_RATES_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setFloat(1, value.getPricePerMin());
            ps.setFloat(2, value.getCostPerMin());
            ps.setInt(3, value.getCityFrom().getId());
            ps.setInt(4, value.getCityTo().getId());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                value.setId(rs.getInt(1));
            }
            ps.close();
            rs.close();
            return value;
        } catch (SQLException e) {
           throw new RuntimeException("Error al agregar una tarifa",e);
        }
    }

    @Override
    public void update(Rate value) {
        try {
            PreparedStatement ps = con.prepareStatement(UPDATE_RATES_QUERY);
            ps.setFloat(1, value.getPricePerMin());
            ps.setFloat(2, value.getCostPerMin());
            ps.setInt(3, value.getCityFrom().getId());
            ps.setInt(4, value.getCityTo().getId());
            ps.setInt(5,value.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar la tarifa", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            PreparedStatement ps = con.prepareStatement(REMOVE_RATES_QUERY);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar tarifa", e);
        }
    }

    @Override
    public RateDto getById(Integer id) {
        RateDto rateDto = null;
        try {
            PreparedStatement ps = con.prepareStatement(GETBYID_RATES_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rateDto = createRate(rs);
            }
            rs.close();
            ps.close();
            return rateDto;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de la tarifa", e);
        }

    }


    private RateDto createRate(ResultSet rs) throws SQLException {
        RateDto rateDto = new RateDto(
                rs.getFloat("price_per_min"),
                rs.getFloat("cost_per_min"),
                cityMySQLDao.getCityName(rs.getInt("id_city_from_fk")),
                cityMySQLDao.getCityName(rs.getInt("id_city_to_fk")));
        return rateDto;
    }

    @Override
    public List<RateDto> getAll() {
        try {
            PreparedStatement st = con.prepareStatement(BASE_RATES_QUERY);
            ResultSet rs = st.executeQuery();
            List<RateDto> rateDtos = new ArrayList<>();
            while (rs.next()) {
                rateDtos.add(createRate(rs));
            }
            st.close();
            rs.close();
            return rateDtos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de tarifas", e);
        }
    }

    @Override
    public List<RateDto> getRateByCity(GetRateCityDto city) {
        try {
            PreparedStatement st = con.prepareStatement(GET_RATE_BY_CITY_QUERY);
            Integer cityFromint = cityMySQLDao.getIdByName(city.getCityFrom());
            Integer cityToint = cityMySQLDao.getIdByName(city.getCityTo());
            st.setInt(1,cityFromint);
            st.setInt(2,cityToint);
            ResultSet rs = st.executeQuery();
            List<RateDto> rateDtos = new ArrayList<>();
            while (rs.next()) {
                rateDtos.add(createRate(rs));
            }
            st.close();
            rs.close();
            return rateDtos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de tarifas", e);
        }
    }

}

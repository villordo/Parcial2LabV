package utn.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import utn.dao.InvoiceDao;
import utn.dto.DateDto;
import utn.dto.InvoiceDto;
import utn.dto.InvoicesBetweenDateDto;
import utn.exceptions.AlreadyExistsException;
import utn.model.Invoice;
import utn.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utn.dao.mysql.MySQLUtils.*;

@Repository
public class InvoiceMySQLDao implements InvoiceDao {
    Connection con;
    UserLineMySQLDao userLineMySQLDao;

    @Autowired
    public InvoiceMySQLDao(Connection con, UserLineMySQLDao userLineMySQLDao) {
        this.userLineMySQLDao = userLineMySQLDao;
        this.con = con;
    }

    @Override
    public User add(Invoice value) throws AlreadyExistsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Invoice value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InvoiceDto getById(Integer id) {
        InvoiceDto invoiceDto = null;
        try {
            PreparedStatement ps = con.prepareStatement(GETBYID_INVOICES_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                invoiceDto = createInvoice(rs);
            }
            ps.close();
            rs.close();
            return invoiceDto;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de la factura", e);
        }
    }

    private InvoiceDto createInvoice(ResultSet rs) throws SQLException {
        InvoiceDto invoiceDto = new InvoiceDto(
                rs.getInt("call_count"),
                userLineMySQLDao.getLineNumber(rs.getInt("id_line_fk")),
                rs.getDate("date_emission"),
                rs.getDate("date_expiration"),
                rs.getFloat("total_price")
        );
        return invoiceDto;
    }


    @Override
    public List<InvoiceDto> getAll() {
        try {
            PreparedStatement st = con.prepareStatement(BASE_INVOICES_QUERY);
            ResultSet rs = st.executeQuery();
            List<InvoiceDto> invoiceDtos = new ArrayList<>();
            while (rs.next()) {
                invoiceDtos.add(createInvoice(rs));
            }
            rs.close();
            st.close();
            return invoiceDtos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de facturas", e);
        }
    }

    @Override
    public List<InvoiceDto> getAllFromUserId(Integer id) {
        try {
            PreparedStatement st = con.prepareStatement(GETALLBYID_INVOICES_QUERY);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            List<InvoiceDto> invoiceDtos = new ArrayList<>();
            while (rs.next()) {
                invoiceDtos.add(createInvoice(rs));
            }
            rs.close();
            st.close();
            return invoiceDtos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de facturas por id de cliente", e);
        }
    }

    @Override
    public List<InvoiceDto> getInvoicesBetweenDatesFromUserId(InvoicesBetweenDateDto invoiceDto, Integer id) {
        try {
            CallableStatement cs = con.prepareCall("call sp_invoices_betweendates(?,?,?)");
            cs.setInt(1, id);
            cs.setString(2, invoiceDto.getDateFrom());
            cs.setString(3, invoiceDto.getDateTo());
            ResultSet rs = cs.executeQuery();
            List<InvoiceDto> invoicesDtos = new ArrayList<>();
            while (rs.next()) {
                invoicesDtos.add(createInvoice(rs));
            }
            rs.close();
            cs.close();
            return invoicesDtos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener facturas por rango de fechas", e);
        }
    }

    /*
     * PARCIAL
     * */
    @Override
    public List<InvoiceDto> getInvoicesByDate(DateDto dateDto) {
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("call sp_invoices_by_date(?)");
            cs.setDate(1, new Date(dateDto.getDate().getTime()));
            ResultSet rs = cs.executeQuery();
            List<InvoiceDto> invoicesDtos = new ArrayList<>();
            while (rs.next()) {
                invoicesDtos.add(createInvoice(rs));
            }
            rs.close();
            cs.close();
            return invoicesDtos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener facturas por fecha", e);
        }

    }
}

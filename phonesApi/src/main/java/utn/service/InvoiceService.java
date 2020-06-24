package utn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.dao.InvoiceDao;
import utn.dao.UserDao;
import utn.dto.DateDto;
import utn.dto.InvoiceDto;
import utn.dto.InvoicesBetweenDateDto;
import utn.dto.UserDto;
import utn.exceptions.NoExistsException;
import utn.model.Invoice;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    InvoiceDao dao;
    UserDao daoUser;

    @Autowired
    public InvoiceService(InvoiceDao invoiceDao, UserDao daoUser) {
        this.dao = invoiceDao;
        this.daoUser = daoUser;
    }

    public InvoiceDto getById(Integer id) throws NoExistsException {
        InvoiceDto invoice = dao.getById(id);
        Optional.ofNullable(invoice).orElseThrow(NoExistsException::new);
        return invoice;
    }

    public void delete(Integer id) throws NoExistsException {
        InvoiceDto invoice = dao.getById(id);
        Optional.ofNullable(invoice).orElseThrow(NoExistsException::new);
        dao.delete(id);
    }

    public void update(Invoice value) throws NoExistsException {
        InvoiceDto invoice = dao.getById(value.getId());
        Optional.ofNullable(invoice).orElseThrow(NoExistsException::new);
        dao.update(value);
    }

    public List<InvoiceDto> getAllFromUserId(Integer id) {
        return dao.getAllFromUserId(id);
    }
    public List<InvoiceDto> getAll() {
        return dao.getAll();
    }

    public List<InvoiceDto> getInvoicesBetweenDatesFromUserId(InvoicesBetweenDateDto invoiceDto,Integer id) throws NoExistsException {
        UserDto user = daoUser.getById(id);
        Optional.ofNullable(user).orElseThrow(NoExistsException::new);
        return dao.getInvoicesBetweenDatesFromUserId(invoiceDto,id);
    }

    public List<InvoiceDto> getInvoicesByDate(DateDto dateDto) {
        return dao.getInvoicesByDate(dateDto);
    }
}

package utn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import utn.dto.DateDto;
import utn.dto.InvoiceDto;
import utn.dto.InvoicesBetweenDateDto;
import utn.exceptions.NoExistsException;
import utn.exceptions.ValidationException;
import utn.model.Invoice;
import utn.service.InvoiceService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class InvoiceController {
    InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    //todo baja logica
    public void delete(Integer id) throws NoExistsException {
        invoiceService.delete(id);
    }

    public void update(Invoice invoice) throws NoExistsException {
        invoiceService.update(invoice);
    }

    public InvoiceDto getById(Integer id) throws NoExistsException {
        return invoiceService.getById(id);
    }

    public List<InvoiceDto> getAll() {
        return invoiceService.getAll();
    }

    public List<InvoiceDto> getInvoicesBetweenDatesFromUserId(InvoicesBetweenDateDto invoiceDto, Integer id) throws NoExistsException {
        return invoiceService.getInvoicesBetweenDatesFromUserId(invoiceDto,id);
    }

    public List<InvoiceDto> getInvoicesByDate(DateDto dateDto) throws ValidationException {
        if (dateDto != null) {
            return invoiceService.getInvoicesByDate(dateDto);
        } else {
            throw new ValidationException();
        }
    }

    public List<InvoiceDto> getInvoices(Integer invoiceId) throws NoExistsException, ValidationException {
        if(invoiceId != null){
             return invoiceService.getAllFromUserId(invoiceId);
        }else{
            throw new ValidationException();
        }
    }
}

package utn.dao;

import utn.dto.DateDto;
import utn.dto.InvoiceDto;
import utn.dto.InvoicesBetweenDateDto;
import utn.model.Invoice;

import java.util.List;

public interface InvoiceDao extends AbstractDao<Invoice> {
    InvoiceDto getById(Integer id);

    List<InvoiceDto> getAll();

    List<InvoiceDto> getInvoicesBetweenDatesFromUserId(InvoicesBetweenDateDto invoiceDto, Integer id);

    List<InvoiceDto> getInvoicesByDate(DateDto dateDto);

    List<InvoiceDto> getAllFromUserId(Integer id);
}

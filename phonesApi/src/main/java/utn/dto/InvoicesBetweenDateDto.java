package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoicesBetweenDateDto {
    String dateFrom;
    String dateTo;
}

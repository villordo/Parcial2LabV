package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class InvoiceDto {
    private Integer callCount;
    private String lineNumber;
    private Date dateEmission;
    private Date dateExpiration;
    private float priceTotal;
}

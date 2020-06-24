package utn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.model.enumerated.InvoiceStatus;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invoice {

    private Integer id;
    private Integer callCount;
    private float priceCost;
    private float priceTotal;
    private Date dateEmission;
    private Date dateExpiration;
    private InvoiceStatus invoiceStatus;
    private UserLine userLine;

}

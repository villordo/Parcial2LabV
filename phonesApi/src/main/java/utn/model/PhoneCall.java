package utn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhoneCall {

    private Integer id;
    private String lineNumberFrom;
    private String lineNumberTo;
    private UserLine idLineNumberFrom;
    private UserLine idLineNumberTo;
    private City idCityFrom;
    private City idCityTo;
    private Integer duration;
    private Date callDate;
    private float costPerMin;
    private float pricePerMin;
    private float totalPrice;
    private float totalCost;
    private Invoice invoice;

}

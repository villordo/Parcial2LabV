package utn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rate {

    private Integer id;
    private float pricePerMin;
    private float costPerMin;
    private City cityFrom;
    private City cityTo;

}

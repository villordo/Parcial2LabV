package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RateDto {
    Float pricePerMin;
    Float costPerMin;
    String cityFrom;
    String cityTo;
}

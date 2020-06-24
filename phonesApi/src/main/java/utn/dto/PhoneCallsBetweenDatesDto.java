package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneCallsBetweenDatesDto {
    String dateFrom;
    String dateTo;
}

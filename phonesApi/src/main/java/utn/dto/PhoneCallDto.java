package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class PhoneCallDto {
    private String lineNumberFrom;
    private String lineNumberTo;
    private Integer duration;
    private Date date;
}

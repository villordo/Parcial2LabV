package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;

@AllArgsConstructor
@Data
public class UserLineDto {
    String lineNumber;
    TypeLine typeLine;
    LineStatus lineStatus;
    String firstName;
    String surname;
    Integer dni;
}

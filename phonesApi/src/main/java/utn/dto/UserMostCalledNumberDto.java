package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserMostCalledNumberDto {
    String lineNumber;
    String name;
    String surname;
}

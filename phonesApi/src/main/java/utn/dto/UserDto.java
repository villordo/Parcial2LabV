package utn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    String firstName;
    String surname;
    Integer dni;
    String username;
    String email;
    String cityName;
}

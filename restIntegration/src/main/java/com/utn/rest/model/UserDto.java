package com.utn.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    String firstName;
    String surname;
    Integer dni;
    String username;
    String email;
    String cityName;
}

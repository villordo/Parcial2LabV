package utn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.model.enumerated.UserStatus;
import utn.model.enumerated.UserType;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private Integer id;
    private String firstname;
    private String surname;
    private Integer dni;
    private Date birthdate;
    private String username;
    private String pwd;
    private String email;
    private UserType userType;
    private UserStatus userStatus;
    private City city;

}
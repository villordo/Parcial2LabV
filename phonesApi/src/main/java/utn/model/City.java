package utn.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class City {

    private Integer id;
    private String cityName;
    private Integer prefix;
    private Province province;

}
package utn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.model.enumerated.LineStatus;
import utn.model.enumerated.TypeLine;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLine {

    private Integer id;
    private String lineNumber;
    private TypeLine typeLine;
    private LineStatus lineStatus;
    private User user;

}

package utn.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.controller.PhoneCallController;
import utn.dto.PhoneCallDto;
import utn.exceptions.NoExistsException;
import utn.exceptions.UserNotExistsException;
import utn.exceptions.ValidationException;
//import utn.session.SessionManager;

@RestController
@RequestMapping("/antenna")
public class AntennaWebController {

    PhoneCallController phoneCallController;
    /*SessionManager sessionManager;*/

    @Autowired
    AntennaWebController(PhoneCallController phoneCallController/*, SessionManager sessionManager*/) {
        this.phoneCallController = phoneCallController;
       /* this.sessionManager = sessionManager;*/
    }

    @PostMapping("/phonecall/")
    public ResponseEntity addPhoneCall(@RequestBody PhoneCallDto phoneCallDto, @RequestHeader("Authorization") String token) throws UserNotExistsException, ValidationException, NoExistsException {
        return ResponseEntity.created(RestUtils.getLocationPhoneCall(phoneCallController.addPhoneCall(phoneCallDto))).build();
    }

}

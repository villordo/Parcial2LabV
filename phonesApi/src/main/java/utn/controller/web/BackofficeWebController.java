package utn.controller.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.controller.*;
import utn.dto.*;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.exceptions.UserNotExistsException;
import utn.exceptions.ValidationException;
import utn.model.PhoneCall;
import utn.model.Rate;
import utn.model.User;
import utn.model.UserLine;
//import utn.session.SessionManager;

import java.util.List;

@RestController
@RequestMapping("/backoffice")
public class BackofficeWebController {

    //SessionManager sessionManager;
    UserController userController;
    PhoneCallController phoneCallController;
    InvoiceController invoiceController;
    RateController rateController;
    UserLineController userLineController;

    @Autowired
    BackofficeWebController(/*SessionManager sessionManager,*/ UserController userController, PhoneCallController phoneCallController, InvoiceController invoiceController, RateController rateController, UserLineController userLineController) {
        //this.sessionManager = sessionManager;
        this.userController = userController;
        this.phoneCallController = phoneCallController;
        this.invoiceController = invoiceController;
        this.rateController = rateController;
        this.userLineController = userLineController;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //ENDPOINT DE PRUEBA - REST TEMPLATE
    @GetMapping("/users/{userId}")
    public ResponseEntity getUserById(@PathVariable Integer userId) throws UserNotExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(userController.getById(userId));
    }

    @GetMapping("/string")
    public ResponseEntity getString() {
        String st = "hola";
        return ResponseEntity.status(HttpStatus.OK).body(st);
    }
//****************************************************USERS***********************************************************

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user, @RequestHeader("Authorization") String token) throws AlreadyExistsException {
        return ResponseEntity.created(RestUtils.getLocationUser(userController.add(user))).build();
    }

    @DeleteMapping("/users/{idUser}")
    public ResponseEntity removeUserById(@PathVariable Integer idUser, @RequestHeader("Authorization") String token) throws UserNotExistsException {
        userController.removeUser(idUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/users")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @RequestHeader("Authorization") String token) throws UserNotExistsException {
        userController.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestHeader("Authorization") String token) {
        List<UserDto> userList = userController.getAll();
        return (userList.size() > 0) ?
                ResponseEntity.ok(userList) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*@GetMapping("/users/{userId}")
    public ResponseEntity getUserById(@RequestHeader("Authorization") String token, @PathVariable Integer userId) throws UserNotExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(userController.getById(userId));
    }*/

    //****************************************************RATES***********************************************************
    /*
        4) Consulta de tarifas.
    */
    @PostMapping("/rates/")
    public ResponseEntity addRate(@RequestBody Rate rate, @RequestHeader("Authorization") String token) throws UserNotExistsException, AlreadyExistsException {
        return ResponseEntity.created(RestUtils.getLocationRate(rateController.add(rate))).build();
    }

    @PutMapping("/rates/")
    public ResponseEntity updateRate(@RequestBody Rate rate, @RequestHeader("Authorization") String token) throws NoExistsException {
        rateController.update(rate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/rates/{rateId}")
    public ResponseEntity deleteRateById(@RequestHeader("Authorization") String token, @PathVariable Integer rateId) throws NoExistsException {
        rateController.delete(rateId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/rates/all")
    public ResponseEntity<List<RateDto>> getAllRates(@RequestHeader("Authorization") String token) {
        List<RateDto> rateDtos = rateController.getAll();
        return (rateDtos.size() > 0) ?
                ResponseEntity.ok(rateDtos) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/rates/")
    public ResponseEntity<List<RateDto>> getRateByCity(@RequestHeader("Authorization") String token, @RequestBody GetRateCityDto city) throws NoExistsException {
        List<RateDto> rateList = rateController.getRateByCity(city);
        return (rateList.size() > 0) ?
                ResponseEntity.ok(rateList) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //****************************************************USERLINES***********************************************************
    /*
        3) Alta , baja y suspensión de líneas.
     */
    @PostMapping("/userlines")
    public ResponseEntity addUserLine(@RequestBody UserLine userLine, @RequestHeader("Authorization") String token) throws AlreadyExistsException, UserNotExistsException {
        return ResponseEntity.created(RestUtils.getLocationUserLine(userLineController.add(userLine))).build();
    }

    @PutMapping("/userlines")
    public ResponseEntity updateUserLine(@RequestBody UserLine userLine, @RequestHeader("Authorization") String token) throws NoExistsException {
        userLineController.update(userLine);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/userlines/{userLineId}")
    public ResponseEntity removeUserLineById(@RequestHeader("Authorization") String token, @PathVariable Integer userLineId) throws NoExistsException {
        userLineController.remove(userLineId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/userlines/{userLineId}")
    public ResponseEntity getUserLineById(@RequestHeader("Authorization") String token, @PathVariable Integer userLineId) throws NoExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(userLineController.getById(userLineId));
    }

    @GetMapping("/userlines")
    public ResponseEntity<List<UserLineDto>> getAllUserLines(@RequestHeader("Authorization") String token) throws UserNotExistsException {
        List<UserLineDto> userLineDtos = userLineController.getAll();
        return (userLineDtos.size() > 0) ?
                ResponseEntity.ok(userLineDtos) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//****************************************************PHONECALLS***********************************************************

    /*
        5) Consulta todas las llamadas a partir de un ID de usuario
    */
    @GetMapping("/phonecalls/users/{userId}")
    public ResponseEntity<List<ReturnedPhoneCallDto>> getAllPhoneCallsFromUserId(@RequestHeader("Authorization") String token, @PathVariable Integer userId) throws NoExistsException {
        List<ReturnedPhoneCallDto> returnedPhoneCallDtoList = phoneCallController.getAllPhoneCallsFromUserId(userId);
        return (returnedPhoneCallDtoList.size() > 0) ?
                ResponseEntity.ok(returnedPhoneCallDtoList) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/phonecalls/")
    public ResponseEntity updatePhoneCall(@RequestBody PhoneCall phoneCall, @RequestHeader("Authorization") String token) throws NoExistsException {
        phoneCallController.update(phoneCall);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/phonecalls/{idPhoneCall}")
    public ResponseEntity deletePhoneCall(@RequestHeader("Authorization") String token, @PathVariable Integer idPhoneCall) throws NoExistsException {
        phoneCallController.delete(idPhoneCall);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/phonecalls/")
    public ResponseEntity<List<ReturnedPhoneCallDto>> getAllPhoneCalls(@RequestHeader("Authorization") String token) {
        List<ReturnedPhoneCallDto> phoneCalls = phoneCallController.getAll();
        return (phoneCalls.size() > 0) ? ResponseEntity.ok(phoneCalls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/phonecalls/{idPhoneCall}")
    public ResponseEntity getByIdPhonecall(@PathVariable Integer idPhoneCall, @RequestHeader("Authorization") String token) throws NoExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(phoneCallController.getById(idPhoneCall));
    }

//****************************************************INVOICES***********************************************************

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity getInvoiceById(@RequestHeader("Authorization") String token, @PathVariable Integer invoiceId) throws NoExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(invoiceController.getById(invoiceId));
    }

    @GetMapping("/invoices/client/{invoiceId}")
    public ResponseEntity getInvoiceByIdClient(@RequestHeader("Authorization") String token, @PathVariable Integer invoiceId) throws NoExistsException, ValidationException {
        List<InvoiceDto> invoiceDtos = invoiceController.getInvoices(invoiceId);
        return (invoiceDtos.size() > 0) ?
                ResponseEntity.ok(invoiceDtos) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

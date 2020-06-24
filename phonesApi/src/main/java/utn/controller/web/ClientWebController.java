package utn.controller.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.controller.InvoiceController;
import utn.controller.PhoneCallController;
import utn.controller.UserController;
import utn.dto.InvoiceDto;
import utn.dto.InvoicesBetweenDateDto;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.exceptions.NoExistsException;
import utn.exceptions.ValidationException;
//import utn.session.SessionManager;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientWebController {

    /*SessionManager sessionManager;*/
    UserController userController;
    PhoneCallController phoneCallController;
    InvoiceController invoiceController;

    @Autowired
    ClientWebController(/*SessionManager sessionManager,*/ UserController userController, PhoneCallController phoneCallController, InvoiceController invoiceController) {
        /*this.sessionManager = sessionManager;*/
        this.userController = userController;
        this.phoneCallController = phoneCallController;
        this.invoiceController = invoiceController;
    }

    /*
        Consulta de numero mas llamado por usuario (parcial)
     */
    @GetMapping("/users/most-called/{lineNumber}")
    public ResponseEntity getMostCalledNumber(@RequestHeader("Authorization") String token, @PathVariable String lineNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(userController.getMostCalledNumber(lineNumber));
    }

    /*
       1) Consulta de destinos más llamados por el usuario
     */
   /* @GetMapping("/phonecalls/top5/")
    public ResponseEntity<List<String>> getTop5DestinationsByUserId(@RequestHeader("Authorization") String token) throws NoExistsException {
        List<String> returnedPhoneCallDtoList = phoneCallController.getMostCalledDestinsByUserId(sessionManager.getCurrentUser(token).getId());
        return (returnedPhoneCallDtoList.size() > 0) ?
                ResponseEntity.ok(returnedPhoneCallDtoList) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/

    /*
       2) Consulta de llamadas del usuario logueado por rango de fechas.
    */
    /*@GetMapping("/phonecalls/dates/")
    public ResponseEntity<List<ReturnedPhoneCallDto>> getPhoneCallsFromUserIdBetweenDates(@RequestHeader("Authorization") String token, @RequestBody PhoneCallsBetweenDatesDto phonecallDto) throws NoExistsException {
        List<ReturnedPhoneCallDto> returnedPhoneCallDtoList = phoneCallController.getPhoneCallsFromUserIdBetweenDates(phonecallDto, sessionManager.getCurrentUser(token).getId());
        return (returnedPhoneCallDtoList.size() > 0) ?
                ResponseEntity.ok(returnedPhoneCallDtoList) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/

    /*
       3) Consulta de facturas del usuario logueado por rango de fechas.
    */
   /* @GetMapping("/invoices/dates/")
    public ResponseEntity<List<InvoiceDto>> getInvoicesBetweenDatesFromUserId(@RequestHeader("Authorization") String token, @RequestBody InvoicesBetweenDateDto invoiceDto) throws NoExistsException {
        List<InvoiceDto> returnedInvoicesDtoList = invoiceController.getInvoicesBetweenDatesFromUserId(invoiceDto,sessionManager.getCurrentUser(token).getId());
        return (returnedInvoicesDtoList.size() > 0) ?
                ResponseEntity.ok(returnedInvoicesDtoList) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/
    /*
        Trae todas las facturas del CLIENTE LOGUEADO
     */
   /* @GetMapping("/invoices")
    public ResponseEntity getInvoiceByIdClient(@RequestHeader("Authorization") String token) throws NoExistsException, ValidationException {
        List<InvoiceDto> invoiceDtos = invoiceController.getInvoices(sessionManager.getCurrentUser(token).getId());
        return (invoiceDtos.size() > 0) ?
                ResponseEntity.ok(invoiceDtos) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/

    /*
       5) Consulta todas las llamadas a partir de un ID de usuario
   */
    @GetMapping("/phonecalls/users/{userId}")
    public ResponseEntity<List<ReturnedPhoneCallDto>> getAllPhoneCallsFromUserId(@RequestHeader("Authorization") String token, @PathVariable Integer userId) throws NoExistsException {
        List<ReturnedPhoneCallDto> returnedPhoneCallDtoList = phoneCallController.getAllPhoneCallsFromUserId(userId);
        return (returnedPhoneCallDtoList.size() > 0) ?
                ResponseEntity.ok(returnedPhoneCallDtoList) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

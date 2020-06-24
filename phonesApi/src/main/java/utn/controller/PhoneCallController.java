package utn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import utn.dto.PhoneCallDto;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.exceptions.NoExistsException;
import utn.exceptions.ValidationException;
import utn.model.PhoneCall;
import utn.service.PhoneCallService;

import java.util.List;

@Controller
public class PhoneCallController {

    PhoneCallService phoneCallService;

    @Autowired
    public PhoneCallController(PhoneCallService phoneCallService) {
        this.phoneCallService = phoneCallService;
    }

    public Integer addPhoneCall(PhoneCallDto phoneCall) throws ValidationException, NoExistsException {
        if (phoneCall.getDate() != null && phoneCall.getLineNumberFrom() != null && phoneCall.getLineNumberTo() != null && phoneCall.getDuration() != null){
            return phoneCallService.addPhoneCall(phoneCall);
        }
        else {
            throw new ValidationException();
        }
    }

    public void delete(Integer id) throws NoExistsException {
        phoneCallService.delete(id);
    }

    public void update(PhoneCall phoneCall) throws NoExistsException {
        phoneCallService.update(phoneCall);
    }

    public ReturnedPhoneCallDto getById(Integer id) throws NoExistsException {
        return phoneCallService.getById(id);
    }

    public List<ReturnedPhoneCallDto> getAll() {
        return phoneCallService.getAll();
    }

    public List<ReturnedPhoneCallDto> getAllPhoneCallsFromUserId(Integer userId) throws NoExistsException {
        return phoneCallService.getAllPhoneCallsFromUserId(userId);
    }

    public List<ReturnedPhoneCallDto> getPhoneCallsFromUserIdBetweenDates(PhoneCallsBetweenDatesDto phonecallDto, Integer userId) throws NoExistsException {
        return phoneCallService.getPhoneCallsFromUserIdBetweenDates(phonecallDto, userId);
    }

    public List<String> getMostCalledDestinsByUserId(Integer idUser) throws NoExistsException {
        return phoneCallService.getMostCalledDestinsByUserId(idUser);
    }
}

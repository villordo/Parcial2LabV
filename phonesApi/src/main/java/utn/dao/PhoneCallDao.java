package utn.dao;

import utn.dto.PhoneCallDto;
import utn.dto.PhoneCallsBetweenDatesDto;
import utn.dto.ReturnedPhoneCallDto;
import utn.model.PhoneCall;

import java.util.List;

public interface PhoneCallDao extends AbstractDao<PhoneCall> {
    ReturnedPhoneCallDto getById(Integer id);

    List<ReturnedPhoneCallDto> getPhoneCallsFromUserIdBetweenDates(PhoneCallsBetweenDatesDto phonecallDto,Integer userId);

    Integer addPhoneCall(PhoneCallDto value);

    List<ReturnedPhoneCallDto> getAll();

    List<ReturnedPhoneCallDto> getAllPhoneCallsFromUserId(Integer userId);

    List<String> getMostCalledDestinsByUserId(Integer idUser);
}

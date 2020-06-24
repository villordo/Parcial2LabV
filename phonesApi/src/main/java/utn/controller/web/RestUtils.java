package utn.controller.web;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.model.Rate;
import utn.model.User;
import utn.model.UserLine;

import java.net.URI;

public class RestUtils {

    public static URI getLocationPhoneCall(Integer idPhoneCall) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idPhoneCall)
                .toUri();
    }

    public static URI getLocationUser(User user) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
    }

    public static URI getLocationRate(Rate rate) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(rate.getId())
                .toUri();
    }

    public static URI getLocationUserLine(UserLine userLine) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userLine.getId())
                .toUri();
    }


}

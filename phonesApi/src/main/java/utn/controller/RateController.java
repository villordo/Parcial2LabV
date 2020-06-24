package utn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import utn.dto.GetRateCityDto;
import utn.dto.RateDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.model.Rate;
import utn.service.RateService;

import java.util.List;

@Controller
public class RateController {
    RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    public Rate add(Rate rate) throws AlreadyExistsException {
        return rateService.add(rate);
    }

    public void delete(Integer id) throws NoExistsException {
        rateService.delete(id);
    }

    public void update(Rate rate) throws NoExistsException {
        rateService.update(rate);
    }

    public List<RateDto> getAll() {
        return rateService.getAll();
    }

    public List<RateDto> getRateByCity(GetRateCityDto city) {
        return rateService.getRateByCity(city);
    }
}

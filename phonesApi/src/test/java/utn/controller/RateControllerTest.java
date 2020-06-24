package utn.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dto.RateDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.model.City;
import utn.model.Rate;
import utn.service.RateService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RateControllerTest {

    RateController rateController;
    @Mock
    RateService rateService;

    @Before
    public void setUp() {
        initMocks(this);
        rateController = new RateController(rateService);
    }

    @Test
    public void testAddOk() throws AlreadyExistsException {
        Rate rate = new Rate(1, 10, 7, new City(), new City());
        Rate rateaux = new Rate(2, 11, 12, new City(), new City());
        when(rateService.add(rate)).thenReturn(rateaux);
        Rate adda = rateController.add(rate);
        assertEquals(adda.getId(), rateaux.getId());
        assertEquals(adda.getCityFrom().getId(), rateaux.getCityFrom().getId());
        verify(rateService, times(1)).add(rate);
    }


    @Test(expected = AlreadyExistsException.class)
    public void testAddAlreadyExistsException() throws AlreadyExistsException {
        Rate rate = new Rate(1, 10, 7, new City(), new City());
        when(rateService.add(rate)).thenThrow(new AlreadyExistsException());
        rateController.add(rate);
    }

    @Test
    public void testDeleteOk() throws NoExistsException {
        doNothing().when(rateService).delete(1);
        rateController.delete(1);
        verify(rateService, times(1)).delete(1);
    }

    @Test(expected = NoExistsException.class)
    public void testDeleteNoExistsException() throws NoExistsException {
        doThrow(new NoExistsException()).when(rateService).delete(1);
        rateController.delete(1);
    }

    @Test
    public void testUpdateOk() throws NoExistsException {
        Rate rate = new Rate(1, 10, 7, new City(), new City());
        doNothing().when(rateService).update(rate);
        rateController.update(rate);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdateNoExistsException() throws NoExistsException {
        Rate rate = new Rate(1, 10, 7, new City(), new City());
        doThrow(new NoExistsException()).when(rateService).update(rate);
        rateController.update(rate);
    }

    @Test
    public void testGetAllOk() {
        List<RateDto> rateDtos = new ArrayList<>();
        when(rateService.getAll()).thenReturn(rateDtos);
        List<RateDto> all = rateController.getAll();
        assertEquals(all.size(), rateDtos.size());
        verify(rateService, times(1)).getAll();
    }

}

package utn.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utn.dao.RateDao;
import utn.dto.RateDto;
import utn.exceptions.AlreadyExistsException;
import utn.exceptions.NoExistsException;
import utn.model.City;
import utn.model.Rate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RateServiceTest {


    RateService rateService;
    @Mock
    RateDao rateDao;

    @Before
    public void setUp() {
        initMocks(this);
        rateService = new RateService(rateDao);
    }

    @Test
    public void testAddOk() throws AlreadyExistsException {
        Rate rate = new Rate(1, 20f, 15f, new City(), new City());
        Rate newRate = new Rate(2, 20f, 15f, new City(), new City());
        when(rateDao.add(rate)).thenReturn(newRate);
        Rate add = rateService.add(rate);
        assertEquals(add.getCityFrom().getId(), newRate.getCityFrom().getId());
        assertEquals(add.getCityTo().getId(), newRate.getCityTo().getId());
        assertEquals(add.getId(), newRate.getId());
        verify(rateDao, times(1)).add(rate);
    }


    @Test
    public void testDeleteOk() throws NoExistsException {
        RateDto rateDto = new RateDto(15f, 10f, "mdq", "mdz");
        when(rateDao.getById(1)).thenReturn(rateDto);
        doNothing().when(rateDao).delete(1);
        rateService.delete(1);
        verify(rateDao, times(1)).delete(1);
    }

    @Test(expected = NoExistsException.class)
    public void testDeleteNoExistsException() throws NoExistsException {
        when(rateDao.getById(anyInt())).thenReturn(null);
        rateService.delete(anyInt());
    }

    @Test
    public void testUpdateOk() throws NoExistsException {
        RateDto rateDto = new RateDto(15f, 10f, "mdq", "mdz");
        Rate rate = new Rate(1, 20f, 15f, new City(), new City());
        when(rateDao.getById(1)).thenReturn(rateDto);
        doNothing().when(rateDao).update(rate);
        rateService.update(rate);
        verify(rateDao, times(1)).update(rate);
    }

    @Test(expected = NoExistsException.class)
    public void testUpdateNoExistsException() throws NoExistsException {
        Rate rate = new Rate(1, 20f, 15f, new City(), new City());
        when(rateDao.getById(anyInt())).thenReturn(null);
        rateService.update(rate);
    }

    @Test
    public void testGetAllOk(){
        List<RateDto> list = new ArrayList<>();
        RateDto rateDto = new RateDto(15f, 10f, "mdq", "mdz");
        RateDto rate = new RateDto(20f, 15f, "cba", "mdz");
        list.add(rateDto);
        list.add(rate);
        when(rateDao.getAll()).thenReturn(list);
        List<RateDto> all = rateService.getAll();
        assertEquals(all.size(),list.size());
        verify(rateDao,times(1)).getAll();
    }
}

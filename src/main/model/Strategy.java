package main.model;

import main.vo.Vacancy;

import java.util.List;

public interface Strategy {  //Он будет отвечать за получение данных с сайта.
    // Будет какой-то метод/ы для получения данных с сайта
    public   List<Vacancy> getVacancies(String searchString);
}

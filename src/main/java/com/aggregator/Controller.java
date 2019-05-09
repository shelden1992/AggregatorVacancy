package com.aggregator;

import com.aggregator.model.Model;

public class Controller {
    Model model;


    public Controller(Model model) {
        if (model==null){
            throw new IllegalArgumentException();
        }
        this.model=model;
    }
  public   void onCitySelectAndTypeVacancy(String typeVacancy, String cityName, String nameDatabase, boolean newDatabase){

        model.selectCityAndVacancy(typeVacancy, cityName, nameDatabase, newDatabase);
    }
}


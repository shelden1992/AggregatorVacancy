package com.aggregator.model;

import com.aggregator.vo.Vacancy;

import java.util.List;

public class Provider {  //контекст
    private Strategy strategy;


 public List<Vacancy> getJavaVacancies(String typeVacancy, String city){

     return strategy.getVacancies(typeVacancy, city );
 }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy=strategy;
    }

    public Provider(Strategy strategy) {
        this.strategy=strategy;
    }
}

package com.aggregator.model;


import com.aggregator.view.View;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Model {
    View[] view;
    Provider[] providers;

    public Model(View[] view, Provider... providers) {

        if (providers == null || providers.length == 0 || view == null || view.length == 0) {
            throw new IllegalArgumentException("View or array of providers must be not null");
        }
        this.providers=providers;
        this.view=view;


    }

    public void selectCityAndVacancy(String typeVacancy, String city, String nameDatabase, boolean newDatabase) {

//        for (Provider pr:providers
//             ) {
//            if (pr!=null){
//                com.aggregator.view.update(pr.getJavaVacancies(city).stream().collect(Collectors.toList()));
//            }
//
//        }

        for (View views : view
        ) {

            views.update(
                    Arrays.stream(providers)
                            .map(f -> f.getJavaVacancies(typeVacancy, city))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList()), nameDatabase, newDatabase);
        }
    }
}

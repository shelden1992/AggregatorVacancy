package com.aggregator.model;


import com.aggregator.view.DatabaseView;
import com.aggregator.view.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

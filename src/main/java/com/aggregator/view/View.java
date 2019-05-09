package com.aggregator.view;


import com.aggregator.Controller;
import com.aggregator.vo.Vacancy;

import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies, String nameDatabase, boolean newDatabase);

    void setController(Controller controller);
}

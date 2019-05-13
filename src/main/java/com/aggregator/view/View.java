package com.aggregator.view;


import com.aggregator.Controller;
import com.aggregator.vo.Vacancy;

import javax.sql.DataSource;
import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies, String nameDatabase, boolean newDatabase);

    void setController(Controller controller);


//    void setDataSource (DataSource dataSource);
}

package main.view;

import main.Controller;
import main.vo.Vacancy;

import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies);

    void setController(Controller controller);
}

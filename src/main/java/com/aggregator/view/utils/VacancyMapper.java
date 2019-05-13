package com.aggregator.view.utils;

import com.aggregator.vo.Vacancy;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VacancyMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {


        Vacancy vacancy=new Vacancy();

        resultSet.getString("id");

        vacancy.setUrl(resultSet.getString("url"));

        vacancy.setTitle(resultSet.getString("title"));

        vacancy.setCity(resultSet.getString("city"));

        vacancy.setCompanyName(resultSet.getString("company_name"));

        vacancy.setSalary(resultSet.getString("salary"));


        return vacancy;


    }
}

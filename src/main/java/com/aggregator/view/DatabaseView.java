package com.aggregator.view;

import com.aggregator.Controller;
import com.aggregator.vo.Vacancy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class DatabaseView implements View {
    private Controller controller;

    private void getUpdatedFileContent(List<Vacancy> list) {

        createTable(list);


    }

    public void createTable(List<Vacancy> list) {
        Properties connectionDatasource=new Properties();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (FileInputStream inputStream=new FileInputStream("/Users/macuser/Desktop/projects/aggregatorVacancy/src/main/resources/connect.properties")) {
            connectionDatasource.load(( inputStream ));

        } catch (IOException e) {
            e.printStackTrace();
        }


        try (Connection connection=DriverManager.getConnection(connectionDatasource.getProperty("url"), connectionDatasource.getProperty("user"), connectionDatasource.getProperty("password"))) {

            PreparedStatement preparedStatement=null;
            int i=1;
            for (Vacancy vc : list
            ) {
                String city=vc.getCity();
                String title=vc.getTitle();
                String salary=vc.getSalary();
                String companyName=vc.getCompanyName();
                String url=vc.getUrl();

                preparedStatement=connection.prepareStatement("insert into vacancy_table (id, url, title, city, company_name, salary) values (?,?,?,?,?,?)");


                preparedStatement.setInt(1, i++);
                preparedStatement.setString(2, url);

                preparedStatement.setString(3, title);
                preparedStatement.setString(4, city);
                preparedStatement.setString(5, companyName);

//                preparedStatement.setString(6, salary);
                try {


                    int salaryInt=Integer.parseInt(salary.replaceAll("[а-яА-Я a-zA-Z #@$%^&*()()?\\/:;><.+=&$!,]+", ""));
                    preparedStatement.setInt(6, salaryInt);
                } catch (NumberFormatException e) {
                    preparedStatement.setInt(6, 0);
                }


                preparedStatement.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void update(List<Vacancy> vacancies) {
        getUpdatedFileContent(vacancies);
    }

    @Override
    public void setController(Controller controller) {
        this.controller=controller;

    }

//    public void userCitySelectEmulationMethod() {
//        controller.onCitySelectAndTypeVacancy("Харьков");
//    }
}

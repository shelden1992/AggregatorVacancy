package com.aggregator.view;

import com.aggregator.Controller;
import com.aggregator.vo.Vacancy;
import connectionManager.ConnectionManager;
//import net.servletDatabase.modelDao.impl.VacancyDaoImplement;
//import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class DatabaseView implements View {

    //    private DataSource dataSource;
    private Controller controller;

//    public static final Set<String> databaseNameAndCity=new ConcurrentSkipListSet();

    private void getUpdatedFileContent(List<Vacancy> list, String nameDatabase, boolean newDatabase) {
        if (newDatabase) {
            createTable(list, nameDatabase);

        } else {
            insertIntoDatabase(list, nameDatabase);
        }


    }


    public void createTable(List<Vacancy> list, String nameDatabase) {

        try {
            ConnectionManager connectionManager=connectionToDatabase();

            String query="CREATE TABLE IF NOT EXISTS " + nameDatabase + " (id int(20) not null AUTO_INCREMENT, url varchar(150) null, title varchar(150) null, city varchar(100) null, company_name varchar(350) null, salary int null,  primary key (id))";
            PreparedStatement preparedStatement=connectionManager.getConnection().prepareStatement(query);
            boolean execute=preparedStatement.execute();
            insertIntoDatabase(list, nameDatabase);
            connectionManager.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    void insertIntoDatabase(List<Vacancy> list, String nameDatabase) {

        try {
            ConnectionManager connectionManager=connectionToDatabase();


            String query="insert into " + nameDatabase + " ( url, title, city, company_name, salary) values (?,?,?,?,?)";
            PreparedStatement preparedStatement=connectionManager.getConnection().prepareStatement(query);

            for (Vacancy vc : list
            ) {


                String city=vc.getCity();
                String title=vc.getTitle();
                String salary=vc.getSalary();
                String companyName=vc.getCompanyName();
                String url=vc.getUrl();

                int salaryInt=0;
                try {


                    salaryInt=Integer.parseInt(salary.replaceAll("[а-яА-Я a-zA-Z #@$%^&*()()?\\/:;><.+=&$!,]+", ""));
                } catch (NumberFormatException e) {
                }

                preparedStatement.setString(1, url);
//
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, city);
                preparedStatement.setString(4, companyName);

                preparedStatement.setInt(5, salaryInt);
                preparedStatement.executeUpdate();

            }
            connectionManager.closeConnection();

//            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(List<Vacancy> vacancies, String nameDatabase, boolean newDatabase) {
        getUpdatedFileContent(vacancies, nameDatabase, newDatabase);
    }

    @Override
    public void setController(Controller controller) {
        this.controller=controller;

    }

    private ConnectionManager connectionToDatabase() {


        Properties connectionDatasource=new Properties();
        ConnectionManager connectionManager=null;

        try (FileInputStream inputStream=new FileInputStream("/Users/macuser/Desktop/projects/aggregatorVacancy/src/main/resources/connectToDataBase.properties");) {

            connectionDatasource.load(( inputStream ));
            String url=(String) connectionDatasource.get("url");
            String user=(String) connectionDatasource.get("user");
            String password=(String) connectionDatasource.get("password");
            connectionManager=new ConnectionManager(url, user, password);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectionManager;


    }
}

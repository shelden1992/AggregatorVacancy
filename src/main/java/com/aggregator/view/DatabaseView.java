package com.aggregator.view;

import com.aggregator.Controller;
import com.aggregator.vo.Vacancy;
import com.mysql.jdbc.Driver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
//import net.servletDatabase.modelDao.impl.VacancyDaoImplement;
//import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class DatabaseView implements View {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private Controller controller;

    public static final Set<String> databaseNameAndCity=new ConcurrentSkipListSet();

    private void getUpdatedFileContent(List<Vacancy> list, String nameDatabase, boolean newDatabase) {
        if (newDatabase) {
            createTable(list, nameDatabase);
        }


    }


    public void createTable(List<Vacancy> list, String nameDatabase) {

        Properties connectionDatasource=new Properties();

        try (FileInputStream inputStream=new FileInputStream("/Users/macuser/Desktop/projects/aggregatorVacancy/src/main/resources/connectToDataBase.properties")) {
            connectionDatasource.load(( inputStream ));

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            SimpleDriverDataSource dataSource=new SimpleDriverDataSource();
            dataSource.setDriver(new com.mysql.cj.jdbc.Driver());
            dataSource.setUsername(connectionDatasource.getProperty("user"));
            dataSource.setPassword(connectionDatasource.getProperty("password"));
            dataSource.setUrl(connectionDatasource.getProperty("url"));
            this.jdbcTemplate=new JdbcTemplate(dataSource);



//        try (Connection connection=  DriverManager.getConnection(connectionDatasource.getProperty("url"), connectionDatasource.getProperty("user"), connectionDatasource.getProperty("password"))) {

            // ---->>>>>> to many connection ------->>>>>>>>
////////////            до 100 клиентов/ секунду           ///////////////

//            closing inbound before receiving peer's close_notify
//            я хуй его знает может это баг
// https://github.com/brettwooldridge/HikariCP/issues/1268
//            new  Driver().connect()

//            Statement statement=connection.createStatement();
//            System.out.println(nameDatabase);

            String query="CREATE TABLE IF NOT EXISTS " + nameDatabase + " (id int not null, url varchar(150) null, title varchar(150) null, city varchar(100) null, company_name varchar(350) null, salary int null,  primary key (id))";
            jdbcTemplate.execute(query);


//            statement.executeUpdate("create table " + nameDatabase + " (id int not null, url varchar(150) null, title varchar(150) null, city varchar(100) null, company_name varchar(350) null, salary int null,  primary key (id))");
            System.out.println("Database was created and add to set of database" + nameDatabase);
            databaseNameAndCity.add(nameDatabase);
            insertIntoDatabase(list, nameDatabase);

        } catch (
                SQLException e) {
            e.printStackTrace();

        }

    }

    void insertIntoDatabase(List<Vacancy> list, String nameDatabase) {
//        Properties connectionDatasource=new Properties();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        try (FileInputStream inputStream=new FileInputStream("/Users/macuser/Desktop/projects/aggregatorVacancy/src/main/resources/connectToDataBase.properties")) {
//            connectionDatasource.load(( inputStream ));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        new VacancyDaoImplement();
//        try {
//            PreparedStatement preparedStatement=null;
        int id=1;
        for (Vacancy vc : list
        ) {


            String city=vc.getCity();
            String title=vc.getTitle();
            String salary=vc.getSalary();
            String companyName=vc.getCompanyName();
            String url=vc.getUrl();


            String query="insert into " + nameDatabase + " (id, url, title, city, company_name, salary) values (?,?,?,?,?,?)";
//                preparedStatement=connection.prepareStatement("insert into " + nameDatabase + " (id, url, title, city, company_name, salary) values (?,?,?,?,?,?)");


//                preparedStatement.setInt(1, id++);
//                preparedStatement.setString(2, url);
//
//                preparedStatement.setString(3, title);
//                preparedStatement.setString(4, city);
//                preparedStatement.setString(5, companyName);

//                preparedStatement.setString(6, salary);

            int salaryInt=0;
            try {


                salaryInt=Integer.parseInt(salary.replaceAll("[а-яА-Я a-zA-Z #@$%^&*()()?\\/:;><.+=&$!,]+", ""));
//                    preparedStatement.setInt(6, salaryInt);
            } catch (NumberFormatException e) {
//                    preparedStatement.setInt(6, 0);
            }
            jdbcTemplate.update(query, id++, url, title, city, companyName, salaryInt);
        }

//                preparedStatement.executeUpdate();
//            }


//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void update(List<Vacancy> vacancies, String nameDatabase, boolean newDatabase) {
        getUpdatedFileContent(vacancies, nameDatabase, newDatabase);
    }

    @Override
    public void setController(Controller controller) {
        this.controller=controller;

    }


//    public void userCitySelectEmulationMethod() {
//        controller.onCitySelectAndTypeVacancy("Харьков");
//    }
}

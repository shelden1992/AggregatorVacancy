package com.aggregator.model;

import com.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DouStrategy implements Strategy {


    private static final String URL_FORMAT="https://jobs.dou.ua/vacancies/?city=%s&search=%s";
    int numberPage=0;

    @Override
    public List<Vacancy> getVacancies(String typeVacancy, String city ) {

        typeVacancy= doСorrectString(typeVacancy);
        List<Vacancy> list=new ArrayList<>();

//        while (true) {
        try {

            Document document=getDocument(typeVacancy, city, numberPage);

            Elements elements=document.getElementsByClass("vacancy");

            for (Element element : elements
            ) {
                Vacancy vacancy=new Vacancy();
                vacancy.setTitle(element.getElementsByClass("vt").text());
                vacancy.setUrl(element.getElementsByClass("vt").attr("href"));
                vacancy.setCompanyName(element.getElementsByClass("company").text());
                String locCity=element.getElementsByClass("cities").text();
                vacancy.setCity(locCity);
                vacancy.setSalary(element.getElementsByClass("salary").text());
                vacancy.setSiteName(String.format(URL_FORMAT, locCity,typeVacancy));
                list.add(vacancy);

//сложность в том, что тут джава скрипт! и его нужно запустить, чтобы загрузить вакансии далее
//<a href="javascript:;" class="__pd __loading">Больше вакансий</a>
//<a href="javascript:;" class="" style="display: none;">Больше вакансий</a>


            }
//

            System.out.printf("Количетво вакансий на сайте %s в городе %s - %d\n", this.getClass().getSimpleName(), city, list.size());
        } catch (Exception e) {
            e.getMessage();
        }
//        }
        return list;

    }


    private Document getDocument(String typeVacancy, String city, int page) {
        Document document=null;
        try {
            document=Jsoup.connect(String.format(URL_FORMAT, city, typeVacancy)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private String doСorrectString(String vacancy){

        String s=  vacancy;
        s = s.replaceAll(" ", "+");
        s = s.replaceAll("-", "+");
        s = s.replaceAll("_", "+");

        return  s;
    }
}

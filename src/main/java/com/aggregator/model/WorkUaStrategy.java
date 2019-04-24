package com.aggregator.model;

import com.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkUaStrategy implements Strategy {
    private static final String URL_FORMAT="https://www.work.ua/jobs-%s-%s/?page=%d";
    private int pageNum=0;

    @Override
    public List<Vacancy> getVacancies(String typeVacancy, String city) {
        List<Vacancy> vacancyList=new ArrayList<>();

        Document doc=null;
        while (true) {
            try {
                doc=getDocument(typeVacancy, city, pageNum);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements vacancies=doc.getElementsByClass("card card-hover card-visited wordwrap job-link");
            if (vacancies.size() == 0) break;


            for (Element element : vacancies) {
                if (element != null) {
                    Vacancy vac=new Vacancy();
                    vac.setTitle(element.getElementsByClass("add-bottom-sm").text() );
                    vac.setCompanyName(element.getElementsByTag("b").text());
                    vac.setSiteName(String.format( URL_FORMAT,getCorrectCity(city), typeVacancy, pageNum));
                    vac.setUrl("https://www.work.ua"+element.getElementsByAttribute("href").attr("href")) ;
                    String salary=element.getElementsByAttributeValue("class", "nowrap").text();
                    vac.setSalary(salary);
                    vac.setCity(city.toLowerCase());
                    vacancyList.add(vac);
                }
            }
            pageNum++;
        }
        System.out.printf("Количетво вакансий на сайте %s в городе %s - %d\n", this.getClass().getSimpleName(), city, vacancyList.size());
        return vacancyList;
    }

    private Document getDocument(String typeVacancy, String city, int pageNum) throws IOException {

        String typeVacancyForUrl=typeVacancy.toLowerCase().replaceAll(" ", "+");

        String url=String.format(URL_FORMAT, getCorrectCity(city), typeVacancyForUrl, pageNum);
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .timeout(5000)
                .referrer("http://google.ru")
                .get();
    }

    private String getCorrectCity (String city){
        String cityForURL="";
        switch (city.toLowerCase()) {
            case "киев":
               return cityForURL="kyiv";
            case "харків":
             return    cityForURL="kharkiv";
            case "харьков":
              return   cityForURL="kharkiv";
            case "київ":
               return cityForURL="kyiv";
            case "львів":
                return cityForURL="lviv";
            case "львов":
              return   cityForURL="lviv";

            default: return "";
        }
    }
}





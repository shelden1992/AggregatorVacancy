package com.aggregator.model;

import  com.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {
//    private static final String URL_FORMAT="http://javarush.ru/testdata/big28data2.html";
//    private static final String URL_FORMAT="https://moikrug.ru/vacancies?city_id=%d&page=%d&type=all";
    private static final String URL_FORMAT="https://moikrug.ru/vacancies?q=java&city_id=%d&company_id=&company_name=&currency=rur&division_ids%%5B%%5D=&employment_type=&location=%s&page=%d&q=java&qid=&salary=&skills_finder=&type=all&utf8=%%E2%%9C%%93";
 private     int pageNum=0;
    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancyList =new ArrayList<>();

        Document doc=null;
        while (true) {
            try {
                doc=getDocument(searchString, pageNum);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements vacancies=doc.getElementsByClass("job");
            if (vacancies.size() == 0) break;
            for (Element element : vacancies) {
                if (element != null) {
                    Vacancy vac=new Vacancy();
                    vac.setTitle(element.getElementsByAttributeValue("class", "title").text());
                    vac.setCompanyName(element.getElementsByAttributeValue("class", "company_name").text());
                    vac.setSiteName(URL_FORMAT);
                    vac.setUrl("https://moikrug.ru" + element.select("a[class=job_icon]").attr("href"));
                    String salary=element.getElementsByAttributeValue("class", "salary").text();
                    String city=element.getElementsByAttributeValue("class", "location").text();
                    vac.setSalary(salary.length() == 0 ? "" : salary);
                    vac.setCity(city.length() == 0 ? "" : city);
                    vacancyList.add(vac);
                }
            }
            pageNum++;
        }
        System.out.printf("Количетво вакансий на сайте %s в городе %s - %d\n",this.getClass().getSimpleName() ,searchString, vacancyList.size());
        return vacancyList;
    }

    private Document getDocument(String searchString, int pageNum) throws IOException {
        int id = 0;
        switch (searchString){
            case "Москва": id=678;
            break;
            case "Санкт-Петербург": id=679;
            break;
            case "Киев": id=908;
            break;
            case "Харьков": id =742;
            break;
        }

        String url=String.format(URL_FORMAT, id,searchString,  pageNum);
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .timeout(5000)
                .referrer("http://google.ru")
                .get();
    }
}





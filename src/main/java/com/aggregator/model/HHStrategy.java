package com.aggregator.model;

import com.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy { // Этот класс будет реализовывать конкретную стратегию работы с сайтом ХэдХантер
    private static final String URL_FORMAT="http://hh.ua/search/vacancy?text=%s+%s&page=%d";
    private int PAGE_VALUE=0;

    @Override
    public List<Vacancy> getVacancies(String typeVacancy, String city) {
//        Приконнекться к закешированной страничке ХэдХантера используя метод getDocument, нумерация начинается с 0.
        typeVacancy=  doСorrectString(typeVacancy );
        List<Vacancy> vacancyList=new ArrayList<>();

        try {
            Document document=getDocument(typeVacancy, city, PAGE_VALUE);


            while (true) {

                Elements vacancy=document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                if (vacancy.size() <= 0) {

                    break;
                }

                for (Element vacan : vacancy
                ) {
//
                    if (vacan != null) {

                        Vacancy vacancy1=new Vacancy();
                        vacancy1.setTitle(vacan.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                        vacancy1.setCity(vacan.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
                        vacancy1.setCompanyName(vacan.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text());
                        vacancy1.setSiteName(String.format(URL_FORMAT, typeVacancy, vacan.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text(), PAGE_VALUE));
                        String getURL=vacan.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href");
                        vacancy1.setUrl(getURL);
                        vacancy1.setSalary(vacan.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text());

                        vacancyList.add(vacancy1);
//                        if (vacancyList.size() % 20 == 0) {
//                            break;
//                        }
                    }


                }
                ++PAGE_VALUE;
                document=getDocument(typeVacancy, city, PAGE_VALUE);


            }
            System.out.printf("Количетво вакансий на сайте %s в городе %s - %d \n", this.getClass().getSimpleName(), city, vacancyList.size());


        } catch (IOException e) {

            e.printStackTrace();
        }
//        System.out.println(vacancyList.size());

        return vacancyList;
    }

    protected Document getDocument(String typeVacancy, String city, int page) throws IOException {

        return Jsoup.connect(String.format(URL_FORMAT, typeVacancy, city, page)).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36").referrer("")
                .get();
    }

    private String doСorrectString(String vacancy) {

        String s=vacancy;
        s=s.replaceAll(" ", "+");
        s=s.replaceAll("-", "+");
        s=s.replaceAll("_", "+");

        return s;
    }
//    private String getCorrectCity (String city){
    //ss
//        String cityForURL="";
//        switch (city.toLowerCase()) {
//            case "киев":
//                return cityForURL="kyiv";
//            case "харків":
//                return    cityForURL="kharkiv";
//            case "харьков":
//                return   cityForURL="kharkiv";
//            case "київ":
//                return cityForURL="kyiv";
//            case "львів":
//                return cityForURL="lviv";
//            case "львов":
//                return   cityForURL="lviv";
//
//            default: return "";
//        }


}

package com.aggregator.model;

import com.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkUaStrategy implements Strategy {
    private static String URL_FORMAT="https://www.work.ua/jobs-%s-%s/?page=%d";
    private static String URL_FormatIfCityNull="https://www.work.ua/jobs-%s/?page=%d";
    private int pageNum=0;

    @Override
    public List<Vacancy> getVacancies(String typeVacancy, String city) {
        typeVacancy=doСorrectString(typeVacancy);


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
                    vac.setTitle(element.getElementsByClass("add-bottom-sm").text());
                    vac.setCompanyName(element.getElementsByTag("b").text());
                    if (city.isEmpty()) {
                        vac.setSiteName(String.format(URL_FormatIfCityNull, typeVacancy, pageNum));
                    } else {
                        vac.setSiteName(String.format(URL_FORMAT, getCorrectCity(city), typeVacancy, pageNum));
                    }
                    vac.setUrl("https://www.work.ua" + element.getElementsByAttribute("href").attr("href"));
                    String salary=element.getElementsByAttributeValue("class", "nowrap").text();
                    vac.setSalary(salary);

                    Pattern pattern=Pattern.compile("\\Харків|\\Київ|\\Запоріжжя|\\Дніпро|\\Вінниця|\\Черкаси|\\Львів|\\Одеса|\bУкраїна");
                    Matcher matcher=pattern.matcher(element.getElementsByTag("span").text());

                    if (matcher.find()) {


                        String group=matcher.group();


                        vac.setCity(translateToRus(group));

                    }


                    vacancyList.add(vac);
                }
            }
            pageNum++;
        }
        System.out.printf("Количетво вакансий на сайте %s в городе %s - %d\n", this.getClass().getSimpleName(), city, vacancyList.size());
        return vacancyList;
    }

    private Document getDocument(String typeVacancy, String city, int pageNum) throws IOException {

//        String typeVacancyForUrl=typeVacancy.toLowerCase().replaceAll(" ", "+");
        String url=null;
        if (city.isEmpty()) {
            url=String.format(URL_FormatIfCityNull, typeVacancy, pageNum);
        } else {
            url=String.format(URL_FORMAT, getCorrectCity(city), typeVacancy, pageNum);
        }

        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .timeout(5000)
                .referrer("http://google.ru")
                .get();
    }

    private String getCorrectCity(String city) {
        String cityForURL="";
        switch (city.toLowerCase()) {
            case "киев":
                return cityForURL="kyiv";
            case "харків":
                return cityForURL="kharkiv";
            case "харьков":
                return cityForURL="kharkiv";
            case "київ":
                return cityForURL="kyiv";
            case "львів":
                return cityForURL="lviv";
            case "львов":
                return cityForURL="lviv";

            default:
                return "";
        }
    }

    private String translateToRus(String city) {
        if (city==null){
            return "Украина";
        }

        String cityForURL="";
        switch (city.toLowerCase()) {

            case "харків":
                return cityForURL="Харьков";

            case "київ":
                return cityForURL="Киев";
            case "львів":
                return cityForURL="Львов";
            case "дніпро":
                return cityForURL="Днепро";
            case "запоріжжя":
                return cityForURL="Запорожье";
            case "вінниця":
                return cityForURL="Винница";

            case "черкаси":
                return cityForURL="Черкассы";
            case "одеса":
                return cityForURL="Одесса";

            default:
                return "Украина";
        }
    }

    private String doСorrectString(String vacancy) {

        String s=vacancy;
        s=s.replaceAll(" ", "+");
        s=s.replaceAll("-", "+");
        s=s.replaceAll("_", "+");

        return s;
    }
}





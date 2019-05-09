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

public class RabotaStrategy implements Strategy {
    String URL_FORMAT="https://rabota.ua/zapros/%s/%s/pg%d";

    private int PAGE_VALUE=0;

    @Override
    public List<Vacancy> getVacancies(String typeVacancy, String city) {
        typeVacancy=doСorrectString(typeVacancy);


        List<Vacancy> vacancyList=new ArrayList<>();
        try {
            Document document=getDocument(typeVacancy, city, PAGE_VALUE);


            while (true) {

                Elements vacancy=document.getElementsByClass("f-vacancylist-vacancyblock");
                if (vacancy.size() <= 0) {

                    break;
                }

                for (Element vacan : vacancy
                ) {
//
                    if (vacan != null) {

                        Vacancy vacancy1=new Vacancy();

                        String title=vacan.getElementsByClass("f-visited-enable ga_listing").text();
                        vacancy1.setTitle(title);
                        String companyName=vacan.getElementsByClass("f-text-dark-bluegray f-visited-enable").text();
                        vacancy1.setCompanyName(companyName);


                        Pattern pattern=Pattern.compile("\\Киев|\\Херсон|\\Николаев|\\Львов|\\Харьков|\\Ужгород|\\Чернигов|\\Одесса|\\Ивано-Франковск|\\Сумы|\\Ровно|\\Запорожье|\\Днепр|\\Тернополь|\bУкраїна");
                        Matcher matcher=pattern.matcher(vacan.getElementsByClass("fd-merchant").text());

                        if (matcher.find()) {


                            String group=matcher.group();


                            vacancy1.setCity(group);

                        }


                        vacancy1.setSiteName(String.format(URL_FORMAT, typeVacancy, city, PAGE_VALUE));


                        String string=vacan.select("p").attr("onclick");
                        string=string.replaceAll("\\'", "").replace("\\s", "");
                        char[] s=string.toCharArray();
                        for (int i=0; i < s.length; i++) {
                            if (s[i] == '=') {
                                string=string.substring(i + 2);
                                break;
                            }


                        }
                        vacancy1.setUrl("https://rabota.ua" + string);
                        vacancy1.setSalary(vacan.getElementsByClass("fd-beefy-soldier -price").text());

//                        vacancy1.setUrl();


                        vacancyList.add(vacancy1);
//
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

    private String doСorrectString(String typeVAcancy) {
        typeVAcancy=typeVAcancy.replaceAll(" ", "-");
        typeVAcancy=typeVAcancy.replaceAll("\\+", "-");
        typeVAcancy=typeVAcancy.replaceAll("_", "-");
        return typeVAcancy;
    }

    protected Document getDocument(String typeOfVacancy, String city, int page) throws IOException {
//        String vacanc=typeOfVacancy.replaceAll(" ", "-");


        return Jsoup.connect(String.format(URL_FORMAT, typeOfVacancy, city.toLowerCase(), page)).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36").referrer("")
                .get();
    }


}
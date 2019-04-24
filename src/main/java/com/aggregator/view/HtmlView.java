package com.aggregator.view;


import com.aggregator.Controller;
import com.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;
 private final String filePath = "/Users/macuser/Desktop/projects/aggregatorVacancy/src/main/java/com/aggregator/view/vacancies.html";
    @Override
    public void update(List<Vacancy> vacancies) {
//<tr class="vacancy template" style="display: none">
//        <td class="title"><a href="url"></a></td>
//        <td class="city"></td>
//        <td class="companyName"></td>
//        <td class="salary"></td>


        updateFile(getUpdatedFileContent(vacancies));


    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;

    }

//    public void userCitySelectEmulationMethod(){
//
//        controller.onCitySelectAndTypeVacancy("Харьков");
//    }


    private String getUpdatedFileContent(List<Vacancy> list){
//         <tr class="vacancy template" style="display: none">
//        <td class="title"><a href="url"></a></td>
//        <td class="city"></td>
//        <td class="companyName"></td>
//        <td class="salary"></td>
//    </tr>



        Document document =null;
        try {
            document = getDocument();

//            выбираем шаблое, который хранится в конце html файла
        Element elementTemplateBeforeRemove = document.getElementsByClass("template").first();
//        и делаем копию
            Element copyTemplate = elementTemplateBeforeRemove.clone();   //Используй этот элемент в качестве шаблона для добавления новой строки в таблицу вакансий.
//            удаляем лишнии атрибуты в нем
//            style" и класс "template".
            copyTemplate.removeAttr("style");
            copyTemplate.removeClass("template");


            document.select("tr[class=vacancy]").remove(); //удалить


            for (Vacancy vacancy : list) {
                Element localClone = copyTemplate.clone();
                localClone.getElementsByClass("city").first().text(vacancy.getCity());
                localClone.getElementsByClass("companyName").first().text(vacancy.getCompanyName());
                localClone.getElementsByClass("salary").first().text(vacancy.getSalary());
                Element link =localClone.getElementsByTag("a").first();
                link.text(vacancy.getTitle());
                link.attr("href", vacancy.getUrl());
//добавляем outerHtml элемента в который мы записывали данные вакансии, непосредственно перед шаблоном <tr class="vacancy template" style="display: none">
                elementTemplateBeforeRemove.before(localClone.outerHtml());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }


        return document.html();
    }
    private void updateFile(String str){

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath))){


            bufferedOutputStream.write(str.getBytes());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    protected Document getDocument() throws IOException{
        return Jsoup.parse(new File(filePath), "UTF-8");


//


    }
}

//package com.aggregator;
//
//import com.aggregator.model.*;
//import com.aggregator.view.DatabaseView;
//import com.aggregator.view.HtmlView;
//import com.aggregator.view.View;
//
//public class Aggregator {
////    private static final String URL_FORMAT="https://jobs.dou.ua/vacancies/?city=%s&search=java";
//
//    public static void main(String[] args) {
//
//
////        Provider providers = new Provider(new HHStrategy());
//        Provider providers=new Provider(new WorkUaStrategy());
////        Provider providers = new Provider(new DouStrategy());
////        Provider providers=new Provider(new RabotaStrategy());
////        Provider[]providers = { new Provider( new HHStrategy()), new Provider(new WorkUaStrategy()), new Provider( new DouStrategy()), new Provider(new RabotaStrategy())};
////        Provider[] providers={new Provider(new RabotaStrategy()), new Provider(new DouStrategy()), new Provider(new HHStrategy())};
//
//
////        HtmlView htmlView = new HtmlView();
//        DatabaseView databaseView=new DatabaseView();
//        View[] views={databaseView};
//
//
//        Model model=new Model(views, providers);
//
//
////
//        Controller controller=new Controller(model);
//        controller.onCitySelectAndTypeVacancy("java", "киев");
//
//        databaseView.setController(controller);
////
//
//
//    }
//}

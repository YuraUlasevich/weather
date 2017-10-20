import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String url= "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if(matcher.find()){
            return matcher.group();
        }
        throw new Exception("Can't find");
    }
    private static int printFourValues(Elements values, int index){
        int iter = 4;
        if(index==0){
            Element valueLin = values.get(2);
            boolean ismorning = valueLin.text().contains("Утро");
            if(ismorning)
                iter=2;
            for(int i=0;i<iter; i++) {
                valueLin = values.get(index+i);
                for (Element td : valueLin.select("td")) {
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        }
        else{
            for(int i=0; i<4; i++){
                Element valueLine = values.get(index+i);
                for(Element td:valueLine.select("td")){
                    System.out.print(td.text()+"    ");
                }
                System.out.println();
            }
        }
        return iter;
    }
    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for(Element name : names){
            String dateString= name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date+"    Явления    Температура    Давление     Влажность     Ветер");
            int iter = printFourValues(values, index);
            index +=iter;
        }

    }
}

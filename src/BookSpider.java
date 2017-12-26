import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static sun.net.www.protocol.http.HttpURLConnection.userAgent;

/**
 * Created by 54472 on 2017/12/25.
 */
public class BookSpider {

    public static Elements getBookByUrl(String urlTemp) {
        Connection connection = Jsoup.connect(urlTemp);
        connection.header("User-Agent", userAgent);
        Document doc = null;
        try {
            doc = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element elementDiv = doc.getElementById("subject_list");
        Elements elementUl = elementDiv.getElementsByTag("ul");
        Elements elementLi = elementUl.first().getElementsByTag("li");

        return elementLi;
    }

    public static void main(String[] args) throws IOException {
        int start = 0;
        File file = new File("C:\\Users\\54472\\Desktop\\xiju.txt");
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);

        for(int i = 0; i < 50; i++) {
            Elements elementLi = getBookByUrl("https://book.douban.com/tag/武侠?start=" +start+ "&type=T");
            for (Element element : elementLi) {
                try {
                    Elements elements1 = element.children();
                    String title = elements1.get(1).getElementsByTag("a").attr("title");
                    String url = elements1.get(0).getElementsByTag("a").attr("href");
                    String img = elements1.get(0).getElementsByTag("img").attr("src");
                    String author = String.valueOf(elements1.select("div.pub"))
                            .replace("<div class=\"pub\">", "")
                            .replace("</div>", "")
                            .replaceAll(" ", "")
                            .replaceAll("\n", "");
                    String rate = String.valueOf(elements1.select("span.rating_nums"))
                            .replace("<span class=\"rating_nums\">", "")
                            .replace("</span>", "");
                    String rateCount = String.valueOf(elements1.select("span.pl"))
                            .replace("<span class=\"pl\"> (", "")
                            .replace("人评价) </span>", "");
                    String introduction = String.valueOf(elements1.select("p"))
                            .replace("<p>", "")
                            .replace("</p>", "");

                    String item = title + "\t" + url + "\t" + img + "\t" + author
                            + "\t" + rate + "\t" + rateCount + "\t" + introduction + "\t";

                    System.out.println(item);

                    bw.write(item + "\n");
                } catch (Exception e) {
                    continue;
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                continue;
            }
            start = start + 20;
        }

        bw.close();
        fw.close();
    }
}

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by 54472 on 2017/12/23.
 */
public class MovieSpider {


    public static JSONObject getJsonByUrl(String urlTemp) {
        URL url = null;
        InputStreamReader input = null;
        HttpURLConnection conn;
        JSONObject jsonObject = null;
        try {
            url = new URL(urlTemp);
            conn = (HttpURLConnection) url.openConnection();
            input = new InputStreamReader(conn.getInputStream(), "utf-8");
            Scanner inputStream = new Scanner(input);
            StringBuffer sb = new StringBuffer();
            while (inputStream.hasNext()) {
                sb.append(inputStream.nextLine());
            }
            jsonObject = JSONObject.fromObject(sb.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonObject;
    }

    public static void main(String[] args) throws IOException {
        int start = 0;
        File file = new File("C:\\Users\\54472\\Desktop\\xiju.txt");
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i  < 400 ; i++) {
            JSONObject jsonObject = JSONObject.fromBean(getJsonByUrl("https://movie.douban.com/j/new_search_subjects?sort=T&range=0,10&tags=大陆&start=" + start));

            for (int j = 0; j < 20; j++) {
                try {
                    JSONObject array = (JSONObject) jsonObject.getJSONArray("data").get(j);
                    String item = array.getString("title") + "\t" + array.getString("directors") + "\t" + array.getString("rate")
                            + "\t" + array.getString("id") + "\t" + array.getString("casts") + "\t" + array.getString("url")
                            + "\t" + array.getString("cover") + "\t";
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

    //https://movie.douban.com/j/new_search_subjects?sort=T&range=0,10&tags=&start=20
}

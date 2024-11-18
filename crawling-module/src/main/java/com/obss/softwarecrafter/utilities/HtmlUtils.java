package com.obss.softwarecrafter.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlUtils {

    public static String cleanHtml(String html) {
        Document doc = Jsoup.parse(html);

        return doc.html().replaceAll("\\s+", " ")  // Birden fazla boşluğu tek boşluğa çevir
                .replaceAll("> +<", "><")   // Tag'ler arasındaki boşlukları kaldır
                .trim();
    }

    public static String getProfileIdFromUrl(String url) {
        try {
            String[] split = url.split("/");
            for (int i = 0; i < split.length; i++) {
                if (split[i].equals("in")) {
                    String s = split[i + 1];
                    if(s.contains("?"))
                        return s.split("\\?")[0];
                    return s;
                }
            }
            return url;
        } catch (Exception e) {
            return url;
        }
    }
}

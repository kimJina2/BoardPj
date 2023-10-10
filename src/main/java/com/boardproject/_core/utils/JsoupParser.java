package com.boardproject._core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JsoupParser {
    public static List<String> parseImgName(String html){
        List<String> imgNameList = new ArrayList<>();

        Document doc= Jsoup.parse(html);
        Elements imgs = doc.getElementsByTag("img");

        imgs.forEach((x)->{
            String link=x.attr("src");
            String name = link.substring(link.indexOf("name=")+5);
            imgNameList.add(name);
        });

        return imgNameList;
    }
}

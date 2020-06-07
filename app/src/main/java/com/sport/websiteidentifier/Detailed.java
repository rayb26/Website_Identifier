package com.sport.websiteidentifier;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class Detailed implements Runnable {

    static String clientUrl;
    private static long startTimer;
    private static long stopTimer;


    public Detailed(String url) {
        this.clientUrl = domainParser(url);


    }

    @Override
    public void run() {
        try {
            if(getBody(clientUrl).equals("Invalid Website")){
                System.out.println("Invalid Website");
            }else{
                extractData(clientUrl);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Invalid Page Error");
        }

    }

    public static String extractData(String chosenUrl) throws IOException, InterruptedException {

        if (domainParser(chosenUrl).equals("Invalid URL")) {
            return "Invalid URL";
        } else {


//            return getProvidedUrl(domainParser(chosenUrl)) + "!" + getBody(domainParser(chosenUrl));

            return getProvidedUrl(domainParser(chosenUrl) + "->" + getTitle(domainParser(chosenUrl)) +  "->" + getBody(domainParser(chosenUrl)) + "->" +
                    getRedirectedURL(domainParser(chosenUrl)) + "->" + getMetaData(domainParser(chosenUrl)) + "->" + getHeaderData(domainParser(chosenUrl)) + "->" +
                    getDoctype(domainParser(chosenUrl)) + "->" + checkIfSiteIsValid(domainParser(chosenUrl)));



        }


    }


    public static String domainParser(String choseUrl) {
        String[] urlSplit = choseUrl.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();

        if (urlSplit.length == 2 && choseUrl.contains("www")) {

            return stringBuilder.append("http://www." + choseUrl).toString();
        } else if (urlSplit.length == 2 && choseUrl.contains("http")) {
            return choseUrl;
        } else if (urlSplit.length == 2) {
            return stringBuilder.append("http://www." + choseUrl).toString();

        } else if (urlSplit.length == 3 && !choseUrl.contains("http")) {
            return stringBuilder.append("http://") + choseUrl;
        }else if (urlSplit.length == 3 && choseUrl.contains("http")){
            return choseUrl;
        } else {
            return "Invalid Website";
        }
    }

    public static String getRedirectedURL(String chosenUrl) throws IOException, InterruptedException {
        startTimer();
        Connection.Response response = Jsoup.connect(chosenUrl).followRedirects(true).execute();
        if (response.equals(chosenUrl)) {
            stopTimer();
        }else if (response.equals(clientUrl)){
            return "Provided URL Did Not Redirect To Any Other Webpage";
        }

        return "Redirected URL: " + response.url().toString() + " in " + elapsedTimeMillis() + " milliseconds";
    }

    public static String getProvidedUrl(String choseUrl) {
        return "Provided Url: " + choseUrl;
    }

    public static long elapsedTimeMillis() {
        return (stopTimer() - startTimer());
    }

    public static long startTimer() {

        return startTimer = System.currentTimeMillis();

    }

    public static long stopTimer() {
        return stopTimer = System.currentTimeMillis();
    }

    public static String getTitle(String chosenUrl) {
        Document document = null;
        try {
            document = Jsoup.connect(chosenUrl).userAgent("mozilla/17.0").get();
            String webPageTitle = document.title();
            if (webPageTitle.contains("404")) {
                return "404 Error, Web Page Is Not Available";
               // System.out.println("404 Error, Web page is not available");
            } else {
                return "Title of Website: " + webPageTitle;
            }

        } catch (IOException e) {
            return "Invalid Page Error";
        }
    }
    public static String getBody(String chosenUrl){

        try {
            Document document = Jsoup.connect(chosenUrl).userAgent("mozilla/17.0").get();
            String getBody = document.body().text();
            return "Webpage Body: " + getBody;
        } catch (HttpStatusException e){
            return "Check Internet Connection";

        } catch (IllegalArgumentException e){
            return "Error Fetching Data";

        } catch (IOException e) {
            return "Error Fetching Data";
        }

    }

    public static String getDoctype(String chosenurl){

        try {
            Document document = Jsoup.connect(chosenurl).userAgent("mozilla/17.0").get();
            String getBody = String.valueOf(document.documentType());
            return "Webpage Doctype: " + getBody;
        }catch (HttpStatusException e){
            return "Check Your Internet Connection";
        } catch (IOException e) {
            return "Error Fetching Data";

        }

    }
    public static String getHeaderData(String chosenUrl){
        try {
            Document document = Jsoup.connect(chosenUrl).userAgent("mozilla/17.0").get();
            Elements htags = document.select("h1, h2");
            Elements h1tag = htags.select("h1");
            Elements h2tag = htags.select("h2");
            if(!h1tag.isEmpty()){
                return "Header 1: " + h1tag.text();
               // System.out.println("\tHeader 1: " + h1tag.text());
            } if(!h2tag.isEmpty()){
                return "Header 2: " + h2tag.text();
                //System.out.println("\tHeader 2: " + h2tag.text());
            }

        }catch (HttpStatusException e){
            return "Error";
        } catch (IOException e) {
            return "Error";

        }
        return "No Header Tags Available";

    }

    public static String getMetaData(String chosenUrl) {
        Document doc = null;

        int counter = 0;

        boolean reOccur = true;

        while (reOccur) {
            counter++;
            if (counter == 2) {
                break;
            } else {
                try {
                    doc = Jsoup.connect(chosenUrl).get();
                    if (doc.title().contains("404")) {
                        return "404 Error, Invalid Web Page";
                    }
                    String elements = String.valueOf(doc.select("meta[name=description]"));
                    String keywords = String.valueOf(doc.select("meta[name=keywords]"));
                    String origin = String.valueOf(doc.select("meta[name=referrer]"));
                    if (origin.isEmpty() && elements.isEmpty() && keywords.isEmpty()) {
                        return "Meta-data Values Incompatible";
                    }
                    if (keywords.isEmpty()) {

                    } else {
                        String mkeywords = doc.select("meta[name=keywords]").first().attr("content");
                        return "Keywords: " + mkeywords;
                    }
                    if (elements.isEmpty()) {
                    } else {
                        String mDescription = doc.select("meta[name=description]").get(0).attr("content");
                        return "Description: " + mDescription;
                    }
                    if (!origin.isEmpty()) {
                    } else {
                        String mRobots = doc.select("meta[name=robots]").first().attr("content");
                        return "Robots: " + mRobots;
                    }
                } catch (IOException e) {
                    return "Error Fetching Data";
                }
            }
        }

        return "No Meta-Data Available";

    }
    public static String checkIfSiteIsValid(String chosenUrl) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(chosenUrl).getHost());
            return "IP Address Of Website: " + ip;

        } catch (UnknownHostException e) {
            return "Invalid Website";

        } catch (MalformedURLException e) {
            return "Invalid Website";
        }
    }

}
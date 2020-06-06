package com.sport.websiteidentifier;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.Callable;

public class Simple implements Callable<String> {

    static String clientUrl;
    private static long startTimer;
    private static long stopTimer;



    public Simple(String url) throws IOException, InterruptedException {
        this.clientUrl = url;



            if(getTitle(clientUrl).contains("Invalid Website")){
                System.out.println("Invalid Website");
            }else{
                extractData(clientUrl);
            }

    }


        public Simple(String url, Map<String, String>arrangeData) {
        this.clientUrl = domainParser(url);

        try {
            if(getTitle(clientUrl).contains("Invalid Website")){
                System.out.println("Invalid Website");
            }else{
                extractData(clientUrl);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Invalid Page Error");
        }


    }
    public static String extractData(String chosenUrl) throws IOException, InterruptedException, IllegalArgumentException {
        if (domainParser(chosenUrl).equals("Invalid URL")) {
            //System.out.println("Invalid URL");
            return "Invalid URL";
        } else {



            return getProvidedUrl(domainParser(chosenUrl) + "!" + getTitle(domainParser(chosenUrl)) +  "!" +
                    getRedirectedURL(domainParser(chosenUrl)) + "!" + getMetaData(domainParser(chosenUrl)) + "!" + checkIfSiteIsValid(domainParser(chosenUrl)));



        }


    }


    public static Map<String, String> ArrangeData(Map<String, String> arrangeData, String chosenUrl) throws IOException, InterruptedException {
        arrangeData.put("Provided Url: ", getProvidedUrl(chosenUrl));
        arrangeData.put("Title of Web Page: ", getTitle(chosenUrl));
        arrangeData.put("Redirected URL: ", getRedirectedURL(chosenUrl));
        arrangeData.put("Meta-Data: ", getMetaData(chosenUrl));
        arrangeData.put("IP Address of Website: ", checkIfSiteIsValid(chosenUrl));

        return arrangeData;


    }


    public static String domainParser(String choseUrl) {
        if(choseUrl == null){
            return "Null Value";
        }

        String[] urlSplit = choseUrl.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();

        if (urlSplit.length == 2 && choseUrl.contains("www")) {

            return stringBuilder.append("http://www." + choseUrl).toString();
        } else if (urlSplit.length == 2 && choseUrl.contains("http")) {
            return choseUrl;
        } else if (urlSplit.length == 2) {
            return stringBuilder.append("http://www." + choseUrl).toString();

        } else if (urlSplit.length == 3) {
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
        }else if (response.equals(chosenUrl)){
            return "Provided URL Did Not Redirect To Any Other Webpage";
        }

        return "Redirected URL: " + response.url().toString() + " in " + elapsedTimeMillis() + " milliseconds";
    }

    public static String getProvidedUrl(String clientUrl) {
        return "Provided Url: " + clientUrl;
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
            } else {
                return "Title of Website: " + webPageTitle;
            }

        } catch (IllegalArgumentException e){
            return "Invalid Website, Illegal Arg Exception";
        }
        catch (IOException e) {
            return "Invalid Website";
        }
    }

    public static String getMetaData(String chosenUrl) {
        Document doc = null;

        int counter = 0;

        boolean reOccur = true;

        while(reOccur) {
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

                    if (elements.isEmpty()) {
                        //System.out.println("");
                    } else {
                        String mDescription = doc.select("meta[name=description]").get(0).attr("content");
                        return "Description: " + mDescription;
                        //System.out.println("\tDescription: " + mDescription);
                    }
                } catch (IOException e) {
                    return "Error";
                }
            }
        }

        return "Metadata Values Incompatible";
    }

    public static String checkIfSiteIsValid(String chosenUrl) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(chosenUrl).getHost());
            return "IP Address Of Website: " + ip;

        } catch (UnknownHostException e) {
            return "Invalid Page Error";

        } catch (MalformedURLException e) {
            return "Invalid Page Error";
        }
    }


    @Override
    public String call() throws Exception {

            return extractData(clientUrl);
    }
}


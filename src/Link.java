/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 *
 * @author Youssef
 */
public class Link {
    private String linkAddress;
    private Document htmlDocument;
    private boolean valid;
    private String text;
    private static int numValidURLs;
    private static int numInvalidURLs;
   
    public Link() {
    }
    public Link(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String getLinkAddress() {
        return linkAddress;
    }
    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }
    public Document getHtmlDocument() {
        return htmlDocument;
    }
    public void setHtmlDocument(Document htmlDocument) {
        this.htmlDocument = htmlDocument;
    }
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean validURL) {
        this.valid = validURL;
    }
     public static int getNumValidURLs() {
        return numValidURLs;
    }

    public static void incrementNumValidURLs() {
        numValidURLs ++;
    }

    public static int getNumInvalidURLs() {
        return numInvalidURLs;
    }

    public static void incrementNumInvalidURLs() {
        numInvalidURLs ++;
    }

    public static void setNumValidURLs(int numValidURLs) {
        Link.numValidURLs = numValidURLs;
    }

    public static void setNumInvalidURLs(int numInvalidURLs) {
        Link.numInvalidURLs = numInvalidURLs;
    }
    
    public void validateURLs(int depthNow, int depthFinal, int numberOfThreads) throws IOException, InterruptedException
    {
         if (isValidOneURL())
         {
             System.out.println("Valid: " + this.getLinkAddress());
             System.out.println("Text: " + this.getText());
             incrementNumValidURLs();
            if (depthNow == depthFinal)
            { 
                return;
            }
            List<Link> links;  
            links = this.extractAllLinksInside();
            int i;
            for (i = 0; i < links.size(); i++)
            {
                if (numberOfThreads != 1){
                ThreadValidator thread = new ThreadValidator(links.get(i), depthNow+1, depthFinal);
                ThreadValidator.es.execute(thread);}
                else
                {
                    links.get(i).validateURLs(depthNow+1, depthFinal, numberOfThreads);
                } 
            }
        }
        else
         {
             System.out.println("Invalid: " + this.getLinkAddress());
             System.out.println("Text: " + this.getText());
             incrementNumInvalidURLs();
         }
        
           
}
    public List<Link> extractAllLinksInside() throws IOException {
        List<Link> links = new ArrayList<>(); 
        setHtmlDocument(Jsoup.connect(this.getLinkAddress()).get());
        Elements htmlLinks = this.getHtmlDocument().select("a[href]");
        for (int i = 0; i < htmlLinks.size(); i++)
        {   
            links.add(new Link(htmlLinks.get(i).attr("href")));
            links.get(i).setText(htmlLinks.get(i).text());
            if (links.get(i).isRelative())
            {
                links.get(i).concatinateRelativeLink(this);
            }
        }
        return links;
    }
    public boolean isRelative()
    {
       return !this.getLinkAddress().startsWith("http");
    }
    public void concatinateRelativeLink(Link parentLink) throws MalformedURLException
    {
        URL u = new URL(parentLink.getLinkAddress());
        String protocolName = u.getProtocol();
        String hostName = u.getHost();
        String addedPart = protocolName + "://" + hostName;
        this.setLinkAddress(addedPart + this.getLinkAddress()); 
    }
    public boolean isValidOneURL() 
    {
        try {
            Document doc = Jsoup.connect(this.getLinkAddress()).get();
            this.setValid(true);
        }catch (HttpStatusException ex) { 
             this.setValid(false);
        } catch (IOException | IllegalArgumentException ex) {
            this.setValid(false);
        }
        return this.isValid();
    }
       
    
}

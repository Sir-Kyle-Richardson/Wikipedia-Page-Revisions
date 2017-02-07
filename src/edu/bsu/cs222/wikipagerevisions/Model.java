package edu.bsu.cs222.wikipagerevisions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public URL loadURL(String search) {
        String URLStart = "https://en.wikipedia.org/w/api.php?action=query&format=xml&prop=revisions&titles=";
        String URLEnd = "&rvprop=timestamp|comment|user&rvlimit=30&redirects";
        search = search.replace(" ", "_");
        search = URLStart + search + URLEnd;
        try {
            return new URL(search);
        }
        catch(IOException e) { //Handle exceptions better
            throw new RuntimeException(e);
        }
    }

    public Document URLtoDoc(URL url)
    {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(url.openStream());
            return doc;
        }
        catch(SAXException e) {
            System.out.println("Check internet connection or URL does not exist");
        }
        catch(IOException e) {
            System.out.println("A problem occured when retrieving URL");
        }
        catch(ParserConfigurationException e){
            System.out.println("URL is not in XML format");
        }
        return null;
    }

    public List<Revisions> parseRevisions(Document doc) {
        List<Revisions> revisionsList = new ArrayList<>();
        try {
            if (doesPageExist(doc) && doesPageHaveRevisions(doc)) {
                NodeList numberOfRevisions = doc.getElementsByTagName("rev");
                for (int i = 0; i < numberOfRevisions.getLength(); i++) {
                    Element tempElement = (Element) numberOfRevisions.item(i);
                    String user = tempElement.getAttribute("user");
                    String timestamp = tempElement.getAttribute("timestamp");
                    Revisions rev = new Revisions();
                    rev.setInformation(user, timestamp);
                    revisionsList.add(rev);
                }
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        return revisionsList;
    }

    public boolean doesPageExist(Document doc)
    {
        NodeList check = doc.getElementsByTagName("page");
        Element idx = (Element) check.item(0);
        return !(idx.getAttribute("_idx").equals("-1"));
    }

    public boolean doesPageHaveRevisions(Document doc)
    {
        NodeList check = doc.getElementsByTagName("rev");
        return check.getLength() >= 1;
    }

    public boolean isRedirection(Document doc)
    {
        NodeList check = doc.getElementsByTagName("redirects");
        return check.getLength() >= 1;
    }

    public String getRedirection(Document doc) {
        NodeList redirection = doc.getElementsByTagName("r");
        Element redirect = (Element) redirection.item(0);
        return redirect.getAttribute("to");
    }
}
package edu.bsu.cs222.wikipagerevisions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class wikiXMLParser {
    private List<Revisions> revisionsList = new ArrayList<>();
    private List<Revisions> uniqueUserRevisions = new ArrayList<>();
    private Document doc;
    private URL url;
    private revisionsParser revParser = new revisionsParser();

    void wikiParser(String search) {
        revParser = new revisionsParser();
        url = loadURL(search);
        if (isGoodConnection()) {
            doc = URLtoDoc();
            revisionsList = revParser.parseRevisions(doc);
            uniqueUserRevisions = revParser.getUniqueUserRevisionsList();
        }
    }

    void clear() {
        revisionsList = new ArrayList<>();
        uniqueUserRevisions = new ArrayList<>();
    }

    public URL loadURL(String search) {
        String URLStart = "https://en.wikipedia.org/w/api.php?action=query&format=xml&prop=revisions&titles=";
        String URLEnd = "&rvprop=timestamp|comment|user&rvlimit=30&redirects";
        search = search.replace(" ", "_");
        search = URLStart + search + URLEnd;
        try {
            URL url = new URL(search);
            url.openStream();
            return new URL(search);
        }
        catch(Exception e) {
            return null;
        }
    }

    private Document URLtoDoc() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(url.openStream());
        }
        catch(Exception e) {
            return null;
        }
    }

    public boolean doesPageExist() {
        NodeList check = doc.getElementsByTagName("page");
        Element idx = (Element) check.item(0);
        return !(idx.getAttribute("_idx").equals("-1"));
    }

    public boolean doesPageHaveRevisions() {
        NodeList check = doc.getElementsByTagName("rev");
        return check.getLength() >= 1;
    }

    public boolean isRedirection() {
        NodeList check = doc.getElementsByTagName("redirects");
        return check.getLength() >= 1;
    }

    boolean isGoodConnection() {
        try {
            url.openConnection();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public String getRedirection() {
        if (isRedirection()) {
            NodeList redirection = doc.getElementsByTagName("r");
            Element temp = (Element) redirection.item(0);
            return "Redirected: " + temp.getAttribute("from") + " to " + temp.getAttribute("to");
        }
        return "";
    }

    List<Revisions> getRevisionsList() {
        return revisionsList;
    }

    List<Revisions> getUniqueUserRevisionsList() {
        return uniqueUserRevisions;
    }

    //Below is for Test purposes only
    public void setDoc(Document doc) {
        this.doc = doc;
    }
}
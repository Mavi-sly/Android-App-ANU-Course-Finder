package com.example.project.TFIDF;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * @author u7619947 Xinlong Wu
 */
public class TFIDFFactory<T extends Document> {
    private static Map<String,Map<Document, Integer>> documentTerm = new HashMap<>();
    private static int documentCount = 0;

    public static void addDocument(Document doc){
        for(String term: doc.getTerms()) {
            term = term.toLowerCase();
            Map<Document, Integer> dt = documentTerm.get(term);
            if (dt == null) {
                dt = new HashMap<>();
                dt.put(doc, 1);
                documentTerm.put(term, dt);
                continue;
            }
            Integer termCount = dt.get(doc);
            if (termCount == null)
                dt.put(doc, 1);
            else
                dt.put(doc, termCount + 1);
        }
        documentCount += 1;
    }

    private static double getIDF(String term){
        double termCount = 0;
        Map<Document, Integer> docs = documentTerm.get(term);
        if (docs == null)
            return 0;
        for (Integer i: docs.values())
            termCount += i;
        return Math.log((double) documentCount / (1+termCount));
    }

    private static double getTF(Document doc, String term){
        Map<Document, Integer> dt = documentTerm.get(term);
        if (dt == null)
            return 0;
        Integer termCount = dt.get(doc);
        if (termCount == null)
            return 0;
        return (double)termCount / doc.size();
    }

    public static double getTFIDF(Document doc,String terms) {
        double tfidf = 0;
        for (String term: terms.toLowerCase().split(" "))
            tfidf += getTF(doc, term) * getIDF(term);
        return Math.abs(tfidf);
    }

    public static void addDocuments(Collection<Document> list){
        for(Document doc: list){
            addDocument(doc);
        }
    }
}

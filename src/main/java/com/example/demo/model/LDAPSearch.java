

package com.example.demo.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LDAPSearch {
    
    private String searchQuery;
    
    private List<Map<String,String>> dns;
    
    private Map<String,String> record = new HashMap();

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<Map<String, String>> getDns() {
        return dns;
    }

    public void setDns(List<Map<String, String>> dns) {
        this.dns = dns;
    }

    public Map<String, String> getRecord() {
        return record;
    }

    public void setRecord(Map<String, String> record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "LDAPSearch{" + "searchQuery=" + searchQuery + ", dns=" + dns + ", record=" + record + '}';
    }
    

    
}

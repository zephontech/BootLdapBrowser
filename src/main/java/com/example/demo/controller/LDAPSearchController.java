

package com.example.demo.controller;

import com.example.demo.model.LDAPSearch;
import com.example.demo.service.LDAPService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/LDAP")
public class LDAPSearchController {
    
    @Value("${PROVIDER_URL}")
    private String url;
    
    @Value("${PROVIDER_PORT}")
    private Integer port;
    
    @Value("${SECURITY_PRINCIPAL}")
    private String principle;
    
    @Value("${SECURITY_CREDENTIALS}")
    private String credentials;

    @Value("${ROOT_CONTEXT}")
    private String baseDn;
    
    private String defaultQry = "(objectclass=*)";
    
    @Autowired
    private LDAPService ldapService;
    
    private static final Logger logger = LoggerFactory.getLogger(LDAPSearchController.class);
    
   
    @PostMapping(path = "/search") // Map ONLY POST Requests
    public String showCreatePage(Model model)
    {
        logger.debug("showCreatePage");
        LDAPSearch ldapSearch = new LDAPSearch();
        model.addAttribute("ldapSearch", ldapSearch);
        return "ldapSearch";
    }
    
    
    
    @PostMapping(path = "/runsearch")
    public String runSearch(Model model, @ModelAttribute LDAPSearch ldapSearch)
    {
        model.addAttribute("ldapSearch", ldapSearch);
        logger.debug("runSearch:" + ldapSearch);
        
        List<Map<String,String>> dns = new ArrayList();
        
        try
        {
            dns = this.ldapService.runOneScope(this.baseDn, ldapSearch.getSearchQuery());
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }
        
        if (dns != null && !dns.isEmpty())
        {
            ldapSearch.setDns(dns);
            return "ldapSearch";
        }
        
        try
        {
            dns = this.ldapService.runSubTree(this.baseDn, ldapSearch.getSearchQuery());
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }
        
        if (dns != null && !dns.isEmpty())
        {
            ldapSearch.setDns(dns);
        }
        
        return "ldapSearch";
        
    }
    
    @PostMapping(path = "/clickcn")
    public String clickcn(Model model, @RequestParam("cn") String cn, @ModelAttribute LDAPSearch ldapSearch)
    {
        model.addAttribute("ldapSearch", ldapSearch);
        logger.debug("Run clickcn:" + cn);
        
        Map<String,String> record = new HashMap();
        String qry = "(cn=" + cn + ")";
        logger.debug("Run qry:" + qry);
        try
        {
            record = this.ldapService.getObject(this.baseDn, qry);
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }
        logger.debug("Record:" + record);
        if (record != null && !record.isEmpty())
        {
            ldapSearch.setRecord(record);
        }
        return "ldapSearch";
    }
    
    @PostMapping(path = "/clickdn")
    public String clickdn(Model model, @RequestParam("dn") String dn, @RequestParam("cn") String cn,@ModelAttribute LDAPSearch ldapSearch)
    {
        model.addAttribute("ldapSearch", ldapSearch);
        logger.debug("Run clickdn:" + ldapSearch);
        
        List<Map<String,String>> dns = new ArrayList();
        
        try
        {
            dns = this.ldapService.runOneScope(dn, this.defaultQry);
            if (dns != null && !dns.isEmpty())
            {
                ldapSearch.setDns(dns);
            }
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }

        if (ldapSearch.getDns() != null)
        {
            return "ldapSearch";
        }
        
        if (cn == null || cn.trim().isEmpty())
        {
            return "ldapSearch";
        }
        
        Map<String,String> record = new HashMap();
        String qry = "(cn=" + cn + ")";
        logger.debug("Run qry:" + qry);
        try
        {
            record = this.ldapService.getObject(this.baseDn, qry);
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }
        logger.debug("Record:" + record);
        if (record != null && !record.isEmpty())
        {
            ldapSearch.setRecord(record);
        }
        
        return "ldapSearch";
    }

}


package com.example.demo;

import com.example.demo.service.LDAPService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.message.Response;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchResultEntry;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class DemoApplicationTests {

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
    
    
    @Autowired
    private LDAPService ldapService;
    
    private String limitedOC = "(|(objectClass=container)(objectClass=organizationalUnit)(objectClass=person))";
    private String allOC = "(objectClass=*)";
    
    @Test
    void contextLoads() {

        this.ldapService.runOneScope(baseDn, this.allOC);
        
        System.out.println("Single objkect");
        this.ldapService.runOneScope(baseDn, "(cn=TPM*)");
    }
    
    private void singleObject()
    {
        LdapConnection connection = new LdapNetworkConnection(this.url, this.port);
        try
        {
            connection.bind( this.principle, this.credentials);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        
        
        SearchRequestImpl searchRequest = new SearchRequestImpl();
        try
        {
            searchRequest.setBase(new Dn("DC=example,DC=com"));
            searchRequest.setFilter("(cn=TPM*)");
            searchRequest.setScope(SearchScope.SUBTREE);
            searchRequest.addAttributes("*");
            searchRequest.ignoreReferrals();
        }
        catch(Exception e)
        {
            
        }
        
        List<Map<String,String>> dns = new ArrayList();
        
        try
        {
            //EntryCursor cursor = connection.search( this.baseDn, "(objectclass=*)", SearchScope.ONELEVEL, "*" );
            SearchCursor  cursor = connection.search(searchRequest);
            while ( cursor.next() )
            {
                //Entry entry = cursor.get();
                //System.out.println("Entry:" + entry.getAttributes());
                Response response = cursor.get();
                if ( response instanceof SearchResultEntry )
                {
                    Entry resultEntry = ( ( SearchResultEntry ) response ).getEntry();
                    System.out.println( resultEntry.getAttributes() );
                    Collection<Attribute> attrs = resultEntry.getAttributes();
                    for(Attribute attr:attrs)
                    {
                        System.out.println("type:" + attr.getAttributeType());
                        System.out.println("name:" + attr.getId());
                        System.out.println("valu:" + attr.get());
                        if (attr.getId().equalsIgnoreCase("distinguishedname"))
                        {
                            Map dnMap = new HashMap();
                            dnMap.put(attr.getId(),attr.getString());
                            dns.add(dnMap);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
    }
    
    private void fullScope()
    {
        LdapConnection connection = new LdapNetworkConnection(this.url, this.port);
        try
        {
            connection.bind( this.principle, this.credentials);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        
        
        SearchRequestImpl searchRequest = new SearchRequestImpl();
        try
        {
            searchRequest.setBase(new Dn("CN=TPM Devices,DC=example,DC=com"));
            searchRequest.setFilter("(|(objectClass=container)(objectClass=organizationalUnit)(objectClass=person))");
            //searchRequest.setFilter("(objectClass=*)");
            searchRequest.setScope(SearchScope.SUBTREE);
            searchRequest.addAttributes("*");
            searchRequest.ignoreReferrals();
        }
        catch(Exception e)
        {
            
        }
        
        List<Map<String,String>> dns = new ArrayList();
        
        try
        {
            //EntryCursor cursor = connection.search( this.baseDn, "(objectclass=*)", SearchScope.ONELEVEL, "*" );
            SearchCursor  cursor = connection.search(searchRequest);
            while ( cursor.next() )
            {
                //Entry entry = cursor.get();
                //System.out.println("Entry:" + entry.getAttributes());
                Response response = cursor.get();
                if ( response instanceof SearchResultEntry )
                {
                    Entry resultEntry = ( ( SearchResultEntry ) response ).getEntry();
                    System.out.println( resultEntry.getAttributes() );
                    Collection<Attribute> attrs = resultEntry.getAttributes();
                    for(Attribute attr:attrs)
                    {
                        //System.out.println("type:" + attr.getAttributeType());
                        //System.out.println("name:" + attr.getId());
                        //System.out.println("valu:" + attr.get());
                        if (attr.getId().equalsIgnoreCase("distinguishedname"))
                        {
                            Map dnMap = new HashMap();
                            dnMap.put(attr.getId(),attr.getString());
                            dns.add(dnMap);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        System.out.println("dns:" + dns);
    }
    

}

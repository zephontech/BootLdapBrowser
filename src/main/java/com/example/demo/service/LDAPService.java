

package com.example.demo.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class LDAPService {
    
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
    
    @Value("#{'${BINARY_ATTRIBUTES}'.split(',')}")
    private List<String> binaryAttrString;
    
    private static final Logger logger = LoggerFactory.getLogger(LDAPService.class);
    
    
    String baseQry = "(|(objectClass=container)(objectClass=organizationalUnit)(objectClass=person))";
    
    
    
    public List<Map<String,String>> runSubTree(String baseDn,String qry)
    {
        if (baseDn == null || baseDn.trim().isEmpty())
        {
            baseDn = this.baseDn;
        }
        if (qry == null || qry.trim().isEmpty())
        {
            qry = this.baseQry;
        }
        SearchRequestImpl searchRequest = new SearchRequestImpl();
        try
        {
            searchRequest.setBase(new Dn(baseDn));
            searchRequest.setFilter(qry);
            searchRequest.setScope(SearchScope.SUBTREE);
            searchRequest.addAttributes("*");
            searchRequest.ignoreReferrals();
        }
        catch(Exception e)
        {
            
        }
        
        List<Map<String,String>> dns = new ArrayList();
        LdapConnection connection = null;
        try
        {
            connection = this.getConnection(this.url, this.port);
            this.login(connection, this.principle, this.credentials);
            SearchCursor  cursor = connection.search(searchRequest);
            while ( cursor.next() )
            {
                //Entry entry = cursor.get();
                //System.out.println("Entry:" + entry.getAttributes());
                Response response = cursor.get();
                if ( response instanceof SearchResultEntry )
                {
                    Entry resultEntry = ( ( SearchResultEntry ) response ).getEntry();
                    Collection<Attribute> attrs = resultEntry.getAttributes();
                    Map dnMap = new HashMap();
                    for(Attribute attr:attrs)
                    {
                        //System.out.println("type:" + attr.getAttributeType());
                        logger.debug("name:" + attr.getId() + " value:" + attr.getString());
                        if (attr.getId().equalsIgnoreCase("distinguishedname"))
                        {
                            dnMap.put(attr.getId(),attr.getString());
                        }
                        if (attr.getId().equalsIgnoreCase("cn"))
                        {
                            dnMap.put(attr.getId(),attr.getString());
                        }
                    }
                    dns.add(dnMap);
                }
            }
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }
        finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch(Exception e)
            {
                
            }
        }
        return dns;
    }
    
    public List<Map<String,String>> runOneScope(String baseDn,String qry)
    {
        if (baseDn == null || baseDn.trim().isEmpty())
        {
            baseDn = this.baseDn;
        }
        if (qry == null || qry.trim().isEmpty())
        {
            qry = this.baseQry;
        }
        SearchRequestImpl searchRequest = new SearchRequestImpl();
        try
        {
            searchRequest.setBase(new Dn(baseDn));
            searchRequest.setFilter(qry);
            searchRequest.setScope(SearchScope.ONELEVEL);
            searchRequest.addAttributes("*");
            searchRequest.ignoreReferrals();
        }
        catch(Exception e)
        {
            
        }
        
        List<Map<String,String>> dns = new ArrayList();
        LdapConnection connection = null;
        try
        {
            connection = this.getConnection(this.url, this.port);
            this.login(connection, this.principle, this.credentials);
            SearchCursor  cursor = connection.search(searchRequest);
            while ( cursor.next() )
            {
                //Entry entry = cursor.get();
                //System.out.println("Entry:" + entry.getAttributes());
                Response response = cursor.get();
                if ( response instanceof SearchResultEntry )
                {
                    Entry resultEntry = ( ( SearchResultEntry ) response ).getEntry();
                    Collection<Attribute> attrs = resultEntry.getAttributes();
                    Map dnMap = new HashMap();
                    for(Attribute attr:attrs)
                    {
                        //System.out.println("type:" + attr.getAttributeType());
                        logger.debug("name:" + attr.getId() + " value:" + attr.getString());
                        if (attr.getId().equalsIgnoreCase("distinguishedname"))
                        {
                            dnMap.put(attr.getId(),attr.getString());
                        }
                        if (attr.getId().equalsIgnoreCase("cn"))
                        {
                            dnMap.put(attr.getId(),attr.getString());
                        }
                    }
                    dns.add(dnMap);
                }
            }
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }
        finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch(Exception e)
            {
                
            }
        }
        return dns;
    }
    
    public Map<String,String> getObject(String baseDn,String qry)
    {
        if (baseDn == null || baseDn.trim().isEmpty())
        {
            baseDn = this.baseDn;
        }
        if (qry == null || qry.trim().isEmpty())
        {
            qry = this.baseQry;
        }
        SearchRequestImpl searchRequest = new SearchRequestImpl();
        try
        {
            searchRequest.setBase(new Dn(baseDn));
            searchRequest.setFilter(qry);
            searchRequest.setScope(SearchScope.SUBTREE);
            searchRequest.addAttributes("*");
            searchRequest.ignoreReferrals();
        }
        catch(Exception e)
        {
            
        }
        
        Map<String,String> record = new HashMap();
        LdapConnection connection = null;
        try
        {
            connection = this.getConnection(this.url, this.port);
            this.login(connection, this.principle, this.credentials);
            SearchCursor  cursor = connection.search(searchRequest);
            while ( cursor.next() )
            {
                //Entry entry = cursor.get();
                //System.out.println("Entry:" + entry.getAttributes());
                Response response = cursor.get();
                if ( response instanceof SearchResultEntry )
                {
                    Entry resultEntry = ( ( SearchResultEntry ) response ).getEntry();
                    Collection<Attribute> attrs = resultEntry.getAttributes();
                    for(Attribute attr:attrs)
                    {
                        //System.out.println("type:" + attr.getAttributeType());
                        if (this.binaryAttrString.contains(attr.getId()))
                        {
                            logger.debug("bin name:" + attr.getId() + " value:" + attr.get());
                        }
                        System.out.println("name:" + attr.getId() + " value:" + attr.get());
                        record.put(attr.getId(),attr.get().toString());
                    }
                }
            }
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
        }
        finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch(Exception e)
            {
                
            }
        }
        return record;
    }
    
    
    private void login(LdapConnection connection,String user,String pw) throws Exception
    {
        try
        {
            connection.bind( this.principle, this.credentials);
        }
        catch(Exception e)
        {
            logger.error("ERROR:" + e.getMessage(),e);
            throw e;
        }
    }
    
    private LdapConnection getConnection(String url,int port)
    {
        return new LdapNetworkConnection(this.url, this.port);
    }

}

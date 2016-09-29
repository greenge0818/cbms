package com.prcsteel.platform.acl.utils;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.prcsteel.platform.common.exception.BusinessException;
import org.apache.log4j.Logger;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;

/**
 * Created by rolyer on 15-1-7.
 */
public class LdapOperUtils {
    private Logger logger = Logger.getLogger(LdapOperUtils.class);

    /**
     *
     * @return
     */
    private Properties getLdpaConfig() {
        Properties p = new Properties();
        try {
            p.load(LdapOperUtils.class.getClassLoader().getResourceAsStream("ldap.properties"));
        } catch (IOException e) {
            logger.error(e, e);
        }

        return p;
    }

    /**
     * LDAP connection method
     *
     * @return
     */
    private DirContext getLdapConnection() {
        Properties pro = getLdpaConfig();

        DirContext ctx = null;  //LDAP connection object
        Hashtable<String, String> env = new Hashtable<String, String>();

        env.put(Context.INITIAL_CONTEXT_FACTORY, pro.getProperty("initial.context.factory")); // specify JNDI FACTORY
        env.put(Context.PROVIDER_URL, pro.getProperty("provider.url")); // LDAP address
        env.put(Context.SECURITY_PRINCIPAL, pro.getProperty("security.principal")); // specify the username
        env.put(Context.SECURITY_CREDENTIALS, pro.getProperty("security.credentials")); // specify the password

        try {
            ctx = new InitialDirContext(env); // LDAP connection initialization
        } catch (NamingException e) {
            logger.error(e, e);
        }

        return ctx;
    }

    /**
     * find entry
     *
     * @param account
     * @return
     */
    public User find(String account) {
        DirContext ctx = getLdapConnection();
        String dn = getDomainComponent(account);

        Attributes attrs = null;
        try {
            attrs = ctx.getAttributes(dn);
            return mapToUser(attrs);
        } catch (NamingException e) {
            if (e instanceof NameNotFoundException){
                logger.info("entry not found");
            }
            logger.error(e, e);
        }

        return null;
    }
    
    public boolean login(String username,String password){
    	DirContext ctx = getLdapConnection();
    	Attributes attrs = null;
    	String dn = getDomainComponent(username);
        try {
            attrs = ctx.getAttributes(dn);
            String password_ldap = getAttribute(attrs, "userPassword").replace("userPassword: ", "");
            if(EncryptUtil.ldapMD5(password).equals(password_ldap)){
            	return true;
            }
        } catch (NamingException e) {
            if (e instanceof NameNotFoundException){
                logger.info("entry not found");
            }
            logger.error(e, e);
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "用户名或密码错误");
        } 
    	return false;
    }
    /**
     * User & Attribute Mapping
     *
     * @param attrs
     * @return
     * @throws NamingException
     */
    private User mapToUser(Attributes attrs) throws NamingException {
        User user = new User();

        user.setLoginId(getAttribute(attrs, "cn").replace("cn: ", ""));

        return user;
    }

    /**
     * get value from attribute
     *
     * @param attrs    attributes
     * @param attrName attribute name
     * @return attribute value
     * @throws NamingException
     */
    private String getAttribute(Attributes attrs, String attrName)
            throws NamingException {

        Attribute attr = attrs.get(attrName);
        if("userPassword".equals(attrName)) {
            String pwd = new String((byte[]) attr.get());

            return pwd;
        }
        if (attr == null) {
            return "";
        } else {
            return (String) attr.get();
        }
    }

    /**
     * build domain component name
     *
     * @param account
     * @return
     */
    private String getDomainComponent(String account) {
        return "cn=" + account + "," + getBaseDn();
    }

    /**
     * 设置LDAP DN
     * @return
     */
    private String getBaseDn(){
        Properties pro = getLdpaConfig();

        return pro.getProperty("root.domain.component");
    }

}
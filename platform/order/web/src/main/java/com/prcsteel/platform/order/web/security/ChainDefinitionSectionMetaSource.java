package com.prcsteel.platform.order.web.security;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.acl.service.PermissionService;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by rolyer on 15-7-18.
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

    @Autowired
    private PermissionService permissionService;

    private String filterChainDefinitions;

    @Value("${noAccess}")
    private String noAccess;

//    /**
//     * 默认premission字符串
//     */
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    private static final String BASE_KEY = "/**";
    private static final String BASE_VALUE = "authc";

    public Ini.Section getObject() throws BeansException {

        //获取所有ShiroFilterChain
        List<Permission> sfcl = permissionService.queryAllPermissionOnlyWithCodeAndUrl();

//        Permission base = new Permission();
//        base.setUrl(BASE_KEY);
//        base.setCode(BASE_VALUE);
//
//        sfcl.add(base);


        Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);

        // 设置受限配置
        if (StringUtils.isNotBlank(noAccess)) {
            List<NoAccessConfig> filters = getNoAccessConfig();
            for (NoAccessConfig cfg : filters) {
                section.put(cfg.getUrl(), cfg.getCode());
            }
        }

        //循环ShiroFilterChain的url,逐个添加到section中。section就是filterChainDefinitionMap,
        //里面的键就是链接URL,值就是存在什么条件才能访问该链接
        for (Iterator<Permission> it = sfcl.iterator(); it.hasNext(); ) {
            Permission sfc = it.next();
            //如果不为空值添加到section中
            if (StringUtils.isNotEmpty(sfc.getUrl()) && StringUtils.isNotEmpty(sfc.getCode())) {
                //section.put(resource.getValue(),  MessageFormat.format(PREMISSION_STRING, resource.getPermission()));
                section.put(sfc.getUrl(), MessageFormat.format(PREMISSION_STRING, sfc.getCode()));
            }
        }
        section.put(BASE_KEY, BASE_VALUE);

        return section;
    }

    /**
     * 通过filterChainDefinitions对默认的url过滤定义
     *
     * @param filterChainDefinitions 默认的url过滤定义
     */
    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public Class<?> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return false;
    }

    /**
     * 获取受限配置
     * @return
     */
    private List<NoAccessConfig> getNoAccessConfig() {
        List<NoAccessConfig> filters = new ArrayList<>();

        String[] f = noAccess.split(";");
        for (int i=0;i<f.length;i++){
            String[] u = f[i].split("=");
            if (u.length == 2) {
                filters.add(new NoAccessConfig(u[0], u[1]));
            }
        }

        return filters;
    }

    /**
     * 受限配置
     */
    class NoAccessConfig{
        private String url;
        private String code;

        public NoAccessConfig(String url, String code) {
            this.url = url;
            this.code = code;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}

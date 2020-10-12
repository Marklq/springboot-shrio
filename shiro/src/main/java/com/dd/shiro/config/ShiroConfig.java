package com.dd.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //shiroFliterFactoryBean   第三步
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier(value = "webSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //绑定securityManager，设置安全管理器
        factoryBean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro内置过滤器
        /** anon:     无须认证就可以访问
         *  authc:    必须认证了才能访问
         *  user:     必须拥有记住我功能才能使用
         *  perms:    拥有对某个资源的权限才能访问
         *  role:     拥有某个角色的权限才能访问
         */

        Map<String, String> filterMap = new LinkedHashMap<>();
//        filterMap.put("/user/add","anon");

        //进行授权，会拦截没有授权的
        filterMap.put("/user/add", "perms[user:add]");
        filterMap.put("/user/modify", "perms[user:modify]");

        filterMap.put("/user/*", "authc");

        filterMap.put("/auth/logout", "logout");

        factoryBean.setFilterChainDefinitionMap(filterMap);

        //登录页面
        factoryBean.setLoginUrl("/toLogin");

        //未进行授权跳转的页面
        factoryBean.setUnauthorizedUrl("/unAuth");


        return factoryBean;
    }


    //DefaultWebSecurityManager  第二步
    @Bean(name = "webSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier(value = "userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        //关联realm,绑定
        webSecurityManager.setRealm(userRealm);
        return webSecurityManager;
    }

    //创建realm对象，需要自定义类   第一步
    @Bean
    public UserRealm userRealm(@Qualifier("matcherMD5") HashedCredentialsMatcher matcher) {
        UserRealm realm = new UserRealm();
        //配置加密策略
        realm.setCredentialsMatcher(matcher);
        realm.setAuthenticationCachingEnabled(false);
        return realm;
    }


    /**
     * MD5  密码加解密规则
     */
    @Bean("matcherMD5")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");

        //加密次数
//        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);

        return credentialsMatcher;
    }


    //    配置ShiroDialect:用于thymeleaf和shiro标签配合使用
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

}

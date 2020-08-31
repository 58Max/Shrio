package com.max.config;

import com.max.realm.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 配置一个安全管理器
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm realm){

        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);

        return defaultWebSecurityManager;
    }

    /**
     * 配置一个自定义的Realm的bean，最终使用这个bean返回的对象来完成我的认证和授权
     * @return
     */
    @Bean
    public MyRealm myRealm(){
        MyRealm myRealm = new MyRealm();
        return myRealm;
    }

    /**
     * 配置一个Shiro的一个过滤器bean，这个bean配置Shiro相关的一个规则的拦截
     * 比如什么样的请求可以访问什么样的请求不可以访问
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){

        //创建过滤器配置bean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/");//配置用户的登录请求，如果需要进行登录时shiro就会转到这个登录请求进入登录请求
        shiroFilterFactoryBean.setSuccessUrl("/success");//配置登陆成功以后转向的登录请求
        shiroFilterFactoryBean.setUnauthorizedUrl("/noPermission");//配置没有权限时转向的请求地址
        /**
         * 配置权限拦截的规则
         */
        Map<String,String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("login","anon");//配置登录请求不需要认证 anon表示某个请求不需要认证
        filterChainMap.put("/logout","logout");//配置登出的请求，登出后会清空当前用户的内存
        filterChainMap.put("/admin/**","authc");//admin开头的所有请求需要的登录认证 authc表示登录需要认证
        filterChainMap.put("/user/**","authc");//user开头的所有请求需要的登录认证 authc表示登录需要认证
//        filterChainMap.put("/**","authc");//配置剩余的所有请求都需要登录认证（一定要写到最后面 不然白配） 可选配置
        //设置权限拦截规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);


        return shiroFilterFactoryBean;
    }
}

package com.max.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;

/**
 * 自定义的Realm用来实现用户的认证和授权
 * 父类的AuthenticatingRealm只负责用户认证(登录)

 */
public class MyRealm  extends AuthenticatingRealm {

    /**
     * 用户认证的方法 这个方法不能手动调用 Shiro会自动调用
     * @param authenticationToken 用户身份 这里存放着用户的账号和密码
     * @return 返回认证结果 用户登录成功后的身份证明
     * @throws AuthenticationException 如果认证失败Shiro会抛出各种异常
     * 常用异常信息
     * UnknownAccountException 账号不存在
     * AccountException 账号锁定
     * LockedAccountException 账户锁定异常（冻结异常）
     * IncorrectCredentialsException 密码认证失败后Shiro自动抛出的异常表示密码错误
     * 注意
     *    如果这些异常不够用可以自定义异常类并继承Shiro认证异常父类AuthenticationException
     * 建议在浏览器传递数据时加密，数据库存放的的也最好是加密数据 而且加密的次数和盐值完全一致
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String name = token.getUsername();//获取页面中传递的用户账号
        String password = new String(token.getPassword());//获取页面传的密码

        /**
         * 因为数据密码加密主要是为了防止前端到后端数据传递过程中被篡改或者被截获
         * 我们这里进行没有什么意义 所以我们应该在前端进行加密
         */
        /**
         * 如果我们需要对数据进行加密，那么我们需要对当前登录用户和从数据库中的密码同时进行同样的加密
         * 然后比较加密运算后的值进行比较
         */
        //对当前登录用户的密码进行数据加密
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");//设置加密算法
        credentialsMatcher.setHashIterations(2);//设置加密的次数
        this.setCredentialsMatcher(credentialsMatcher);//设置加密

        //假装是从数据库中取得数据并进行加密
        Object obj2 = new SimpleHash("MD5","123456","",2);

        /**
         * token中有很多信息，我们可以根据不同的信息执行不同的逻辑和业务处理
         */
        System.out.println(name + ":"+password );
        if(!"admin".equals(name)&&!"zhangsan".equals(name)){
            throw new UnknownAccountException();//未知账号异常
        }
        if("zhangsan".equals(name)){
            throw new LockedAccountException();//锁定账号异常
        }
        /**
         * 创建密码认证对象，有shiro自动认证密码
         * 参数 1 数据库中的账号
         * 参数 2 数据库中读取数据的密码
         * 参数 3 当前realm的名称
         * 如果认证成功就会返回一个用户身份对象，认证失败Shiro就会抛出IncorrectCredentialsException异常
         */
        return new SimpleAuthenticationInfo(name,"123456",getName());
    }

    public static void main(String[] args) {
        /**
         * 参数 1 加密的算法
         * 参数 2 数据源 即需要被加密的数据
         * 参数 3 加密的盐值  用来改变加密后的结果 不同的盐值加密后的数据不一致
         * 参数 4 被加密运算的次数
         */
        //MD5是不可逆加密算法
        /**
         * 注意 对一个数据加密2次的结果不等于对这个数据加密1次的结果并以此为数据源再次加密一次后的结果
         * 比如 1234 加密1次后等于  xxee1122  加密两次后 qqeesss222
         * 那么 xxee1122 加密1次后不等于 qqeesss222
         */
        Object obj = new SimpleHash("MD5","123456","",1);
        Object obj2 = new SimpleHash("MD5","123456","",2);

        /**
         * 使用盐值加密
         */
        Object obj3 = new SimpleHash("MD5","123456","admin",2);


    }
}

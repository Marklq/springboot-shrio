package com.dd.shiro.config;

import com.dd.shiro.pojo.User;
import com.dd.shiro.service.Impl.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权");
        //SimpleAuthorizationInfo
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //给当前用户添加一个 add的资源权限
        info.addStringPermission("user:add");

        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        //拿到user对象  user对象从下面的认证方法中返回   return new SimpleAuthenticationInfo(user, user.getPassword(), "");
        User currentUser = (User) subject.getPrincipal();

        //设置当前用户的权限
        info.addStringPermission(currentUser.getPerms());
        return info;
    }

    @Autowired
    UserServiceImpl userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();
        //将前台传过来的password进行md5加密
        String password = new String((char[]) userToken.getCredentials());
        System.out.println(userToken.getPassword());
        System.out.println(password);
        User user = userService.queryUserByName(userToken.getUsername());

        String md5Pwd = new Md5Hash(password, username).toHex();
        System.out.println(md5Pwd);

        if (user == null) {
            System.out.println("认证出错啦");
            return null;
        }

        //将当前登录的用户存入到session中去
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute("loginUser", user);

        return new SimpleAuthenticationInfo(user, md5Pwd, ByteSource.Util.bytes(username), getName());
        //return new SimpleAuthenticationInfo(user, user.getPassword(), getName());

/*
                //用户名  密码  实际上是从数据库中取
        String username = "root";
        String password = "root";
                UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        if (!userToken.getUsername().equals(username)) {
            //如果用户名不正确，就返回 UnknownAccountException
            return null;
        }
        return new SimpleAuthenticationInfo("", password, "");
*/
    }

}

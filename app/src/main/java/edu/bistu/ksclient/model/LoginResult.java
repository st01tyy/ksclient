package edu.bistu.ksclient.model;

public class LoginResult
{
    /**
     * 100：登录成功
     * 101：C语言平台API返回登录失败
     * 102：C语言平台API出现异常
     * 103：账号已登录
     */

    private Integer result;

    private User user;  //token的作用是防止他人通过http request恶意提交登出申请

    public LoginResult()
    {
        result = null;
        user = null;
    }

    public LoginResult(Integer result, User user)
    {
        this.result = result;
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Integer getResult()
    {
        return result;
    }

    public void setResult(Integer result)
    {
        this.result = result;
    }
}

package edu.bistu.ksclient.model;

public class User
{
    /**
     * 已通过C语言平台API登录的用户
     */

    private Long id;    //学号

    private String name;    //姓名

    private Integer token;

    public User()
    {
        id = null;
        name = "未命名";
        token = null;
    }

    public User(Long id, Integer token)
    {
        this.id = id;
        this.token = token;
    }

    public User(Long id, String name, Integer token)
    {
        this.id = id;
        this.name = name;
        this.token = token;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getToken()
    {
        return token;
    }

    public void setToken(Integer token)
    {
        this.token = token;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}

package edu.bistu.ksclient.model;

public class LoginRequest
{
    private Long id;    //学号
    private String pw;  //密码

    public LoginRequest()
    {
        id = null;
        pw = null;
    }

    public LoginRequest(Long id, String pw)
    {
        this.id = id;
        this.pw = pw;
    }

    public Long getId()
    {
        return id;
    }

    public String getPw()
    {
        return pw;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setPw(String pw)
    {
        this.pw = pw;
    }
}

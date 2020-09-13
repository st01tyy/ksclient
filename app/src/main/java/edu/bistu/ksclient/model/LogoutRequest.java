package edu.bistu.ksclient.model;

public class LogoutRequest
{
    private Long id;
    private Integer token;

    public LogoutRequest()
    {
        id = null;
        token = null;
    }

    public LogoutRequest(Long id, Integer token)
    {
        this.id = id;
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
}

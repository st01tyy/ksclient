package edu.bistu.ksclient.model;

public class Subject
{
    private Long id;
    private String name;
    private Byte[] icon;

    public Subject()
    {
        id = null;
        name = "未命名";
        icon = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte[] getIcon() {
        return icon;
    }

    public void setIcon(Byte[] icon) {
        this.icon = icon;
    }
}

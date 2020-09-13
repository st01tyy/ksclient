package edu.bistu.ksclient.model;

public class Question
{
    private Long id;
    private String description;
    private Integer timeLimit;  //单位：秒
    private Selection[] selections;
    private Long subjectID;

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Selection[] getSelections() {
        return selections;
    }

    public void setSelections(Selection[] selections) {
        this.selections = selections;
    }

    public Long getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(Long subjectID) {
        this.subjectID = subjectID;
    }
}

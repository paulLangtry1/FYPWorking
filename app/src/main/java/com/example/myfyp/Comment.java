package com.example.myfyp;

public class Comment
{
    String content;

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    String commentID;

    public Comment()
    {

    }

    public Comment(String content, String name,String datetime,String commentID)
    {
        this.content = content;
        this.name = name;
        this.datetime = datetime;
        this.commentID = commentID;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    String name;
    String datetime;


}

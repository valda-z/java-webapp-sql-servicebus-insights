package com.microsoft.azuresample.javawebapp.model;

import java.time.LocalDateTime;

/**
 * Created by vazvadsk on 2017-01-04.
 */
public class ToDoComment {
    private String id;
    private String todoGid;
    private String created;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTodoGid() {
        return todoGid;
    }

    public void setTodoGid(String todoGid) {
        this.todoGid = todoGid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

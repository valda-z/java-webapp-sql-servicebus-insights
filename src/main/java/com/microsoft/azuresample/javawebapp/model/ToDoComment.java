package com.microsoft.azuresample.javawebapp.model;

import java.time.LocalDateTime;

/**
 * Created by vazvadsk on 2017-01-04.
 */
public class ToDoComment {
    private String id;
    private String todoGid;
    private LocalDateTime created;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

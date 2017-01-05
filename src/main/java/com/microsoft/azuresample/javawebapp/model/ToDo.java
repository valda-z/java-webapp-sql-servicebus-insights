package com.microsoft.azuresample.javawebapp.model;

/**
 * Created by vazvadsk on 2016-12-02.
 */
public class ToDo {
    private int id;
    private String note;
    private String category;
    private String gid;

    public ToDo(){

    }

    public ToDo(int id, String note, String category, String gid){
        this.setId(id);
        this.setNote(note);
        this.setCategory(category);
        this.setGid(gid);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}

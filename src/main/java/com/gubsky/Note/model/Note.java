package com.gubsky.Note.model;

public class Note {

    private Long id;
    private String text;

    public Note() {

    }

    public Note(String text) {
        this.text = text;
    }

    public Note(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null) {
            throw new UnsupportedOperationException("ID нельзя изменить после создания");
        }
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
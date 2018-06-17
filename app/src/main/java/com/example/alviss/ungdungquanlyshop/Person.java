package com.example.alviss.ungdungquanlyshop;

import java.io.Serializable;

/**
 * Created by Alviss on 12/21/2017.
 */

public class Person implements Serializable {
    private int id;
    private String code;
    private String name;
    private String phone;
    private String add;
    private String mail;
    private String type;
    private String note;

    public Person(){}

    public Person( String name, String phone, String add, String mail, String type, String note) {
        this.id = -1;
        // this.code = code;
        this.name = name;
        this.phone = phone;
        this.add = add;
        this.mail = mail;
        this.type = type;
        this.note = note;
    }

    public Person(int id, String name, String phone, String add, String mail, String type, String note) {
        this.id = id;
        //  this.code = code;
        this.name = name;
        this.phone = phone;
        this.add = add;
        this.mail = mail;
        this.type = type;
        this.note = note;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    //  public String getCode() {return code;}
    //  public void setCode(String code) {this.code = code;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getAdd() {return add;}
    public void setAdd(String add) {this.add = add;}

    public String getMail() {return mail;}
    public void setMail(String mail) {this.mail = mail;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note;}
}

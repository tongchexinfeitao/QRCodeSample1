package com.example.qrcodesample1.mycircle.greendao;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MyCircleBeanTable {
    @Id(autoincrement = true)
    private Long id;

    private String json;  //用来存储json

    @Generated(hash = 76800213)
    public MyCircleBeanTable(Long id, String json) {
        this.id = id;
        this.json = json;
    }

    @Generated(hash = 437995930)
    public MyCircleBeanTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}

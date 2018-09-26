package org.tangze.work.Test;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/6 0006.
 * 类描述   地址测试类
 * 版本
 */
public class AddressEntity implements Serializable {

    private int id;

    private String name;

    private String number;


    private String addrDetai;

    private String addr;

    private boolean isDefault;


    public AddressEntity(String name,  String addr,boolean isDefault, String addrDetai, String number) {
        this.name = name;
        this.isDefault = isDefault;
        this.addr = addr;
        this.number = number;
        this.addrDetai = addrDetai;
    }



    public AddressEntity() {
    }


    public String getAddrDetai() {
        return addrDetai;
    }

    public void setAddrDetai(String addrDetai) {
        this.addrDetai = addrDetai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}

package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/19 0019.
 * 类描述 地址表 用于编辑用户收货地址
 * 版本
 */
public class Address extends DataSupport implements Serializable {






      private int id; //自增id

    //    1.地址id
      private int address_Id;

//    2.收件人姓名
      private String consignee;


//    3.联系电话
      private String telNum;

//    4.区域名称
      private String area;

//    5.详细地址
      private String addressDetail;

//    6.是否默认地址
      private String isdefault;  // 服务器返回：1:默认地址  0:非默认地址，本地应该处理为：true/false



//    7.用户id
      private int user_id;


    public Address() {
    }


    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public int getAddress_Id() {
        return address_Id;
    }

    public void setAddress_Id(int address_Id) {
        this.address_Id = address_Id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

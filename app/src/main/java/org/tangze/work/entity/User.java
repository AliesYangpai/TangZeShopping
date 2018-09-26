package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 类描述   账户表，三级用户
 * 版本
 */
public class User extends DataSupport implements Serializable{

//     自增id
      private int id;



    //    1.用户_id
    private int user_id;

//    2.用户名
      private String userName;


//    3.密码
      private String passWord;


//    4.注册码

      private String regCode;


//    5.联系电话
      private String telNum;



//    6.邮箱
    private String email;



//    7.昵称
    private String nickName;



//    8.地址id【集合】
    private List<Integer> addressIds = new ArrayList<>();


//    9.收藏Id【集合】
    private List<Integer> collecIds = new ArrayList<>();


//  10.订单id 【集合】

    private List<Integer> orderIds = new ArrayList<>();

//    11.库存id 【集合】

    private List<Integer> inventoryIds = new ArrayList<>();

//    12.账户等级

    private int grade;//账号等级 0:厂商 1：省代 2：市代 3：终端店铺
//    13.用户头像

    private String headPic;//用户头像地址
//    14.用户上家id

    private int topId;//用户上家id
//    15.用户头像


//    16.区域
    private String area;//区域


//    17.注册时间
    private String regTime;



    // 18.是否启动
    private int open;

    // 19.当前用户qq
    private String user_qq;

    public User() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<Integer> getAddressIds() {
        return addressIds;
    }

    public void setAddressIds(List<Integer> addressIds) {
        this.addressIds = addressIds;
    }

    public List<Integer> getCollecIds() {
        return collecIds;
    }

    public void setCollecIds(List<Integer> collecIds) {
        this.collecIds = collecIds;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    public List<Integer> getInventoryIds() {
        return inventoryIds;
    }

    public void setInventoryIds(List<Integer> inventoryIds) {
        this.inventoryIds = inventoryIds;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getUser_qq() {
        return user_qq;
    }

    public void setUser_qq(String user_qq) {
        this.user_qq = user_qq;
    }
}

package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14 0014.
 * 类描述  //登陆用户所对应的上级用户的信息
 * 版本
 */
public class TopUser extends DataSupport implements Serializable{


    //                "UID":"20",
//                "username":"666",

//                "top_id":"20",
//                "name":"One",
//                "tel":"12324242",
//                "email":"212@127.com",
//                "grade":"1",
//                "uimg":null,
//                "area":"\u6d59\u6c5f",
//                "code":"12345",
//                "open":"1",
//                "qq":"12345",
//                "time":"1475906902"


    //     自增id
    private int id;


    //    1.上级用户_id
    private int topUser_id;

    //    2.上级用户名
    private String topUserName;

    //    3.上级用户昵称
    private String topNickName;

    //  4.上级用户电话
    //  5.

}

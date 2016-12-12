package com.nanchen.retrofitrxdemoo.model;


import java.io.Serializable;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/10  09:05
 */

public class UserModel implements Serializable {
    public String username;
    public String password;

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

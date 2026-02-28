package com.example.utils;

import com.example.entity.UserTLDTO;

public class UserContext {

    private static final ThreadLocal<Long> userID = new ThreadLocal<>();

    private static final ThreadLocal<String> role = new ThreadLocal<>();

    private static final ThreadLocal<String> auth = new ThreadLocal<>();



    public static Long getId(){
        return userID.get();
    }

    public static void setUserID(Long ID){
        userID.set(ID);
    }

    public static void removeUserID(){
        userID.remove();
    }


    public static void setRole(String r){
        role.set(r);
    }

    public static String getRole(){
        return role.get();
    }

    public static void removeRole(){
        role.remove();
    }

    public static void setAuth(String Auth){
        auth.set(Auth);
    }

    public static String getAuth(){
        return auth.get();
    }

    public static void removeAuth(){
        auth.remove();
    }


}

package com.company.linkedln.user_service.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {


    // hash the password for thr first time
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // check the plainText Password matches rhe previously hashed one
    public static boolean checkPassword(String plainTextPassword, String hashedPassword){
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}

package com.ihsinformatics.dynamicformsgenerator.utils;

/**
 * Created by Admin on 5/3/2017.
 */
public class TestClass {
    public static void main(String[] args){
        try {
            /*String username = "user";
            String password = "password";
            AES256Endec aes = AES256Endec.getInstance();
            SecretKey secKey = aes.generateSecretKey(password, username);
            System.out.print(aes.encrypt(password, secKey));*/
            System.out.print(SecurityUtils.encrypt("user","password"));
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import net.sp1d.chym.entities.User;

/**
 *
 * @author sp1d
 */
public class Butler {

    private static byte[] byteArraysConcat(byte[] b1, byte[] b2) {
        byte[] res = Arrays.copyOf(b1, b1.length + b2.length);
        System.arraycopy(b2, 0, res, b1.length, b2.length);
        return res;
    }

    static private byte[] crypt(byte[] salt, byte[] password) throws NoSuchAlgorithmException {
        
        byte[] hash = null;
        String hashString = null;

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        hash = md.digest(byteArraysConcat(salt, password));
        hashString = md.toString();

        for (int i = 0; i < 1000000; i++) {
            hash = md.digest(hash);
        }
        Base64.Encoder b64 = Base64.getEncoder();
        hash = b64.encode(hash);
        
        return hash;

    }

    static byte[] crypt(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("wrong user object");
        }
        byte[] salt = user.getEmail().getBytes("UTF-16");
        byte[] password = user.getPassword().getBytes("UTF-16");
        return crypt(salt, password);

    }

}

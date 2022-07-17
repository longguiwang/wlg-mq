package com.wlg.mqprotocol.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: ObjectByteUtils to convert between obj and byte array
 */
@Slf4j
public class ObjectByteUtils {

    /**
     * obj to array
     * @param obj
     * @return
     */
    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return bytes;
    }

    /**
     * byte array to obj
     * @param bytes
     * @return
     */
    public static Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (Exception ex) {
            //log.error("ex",ex);
            return null;
        }
        return obj;
    }
}

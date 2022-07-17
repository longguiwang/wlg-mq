package com.wlg.mqstorage.fuse;

import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: MessageFuse
 */
@Data
public class MessageFuse implements Serializable {
    private static final long serialVersionUID = -20220713L;
    private long offset;
    private int length;
    private int tagHashcode;
    private long index;

    @Override
    public String toString() {
        return new StringJoiner(", ", MessageFuse.class.getSimpleName() + "[", "]")
                .add("offset=" + offset)
                .add("length=" + length)
                .add("tagHashcode=" + tagHashcode)
                .add("index=" + index)
                .toString();
    }
}

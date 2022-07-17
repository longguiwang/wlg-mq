package com.wlg.mqstorage.persistence;

import com.wlg.mqcommon.exception.FileException;
import com.wlg.mqprotocol.entity.PullMessage;
import com.wlg.mqprotocol.entity.SendMessage;
import com.wlg.mqprotocol.utils.ObjectByteUtils;
import com.wlg.mqstorage.fuse.MessageFuse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: FileOperation
 */
@Slf4j
public class FileOperation {

    private static final int MessageFuseLength = 24;

    public static  void save(String fileName, List<MessageFuse> data) throws IOException {
        if (data==null||data.size()==0){
            return;
        }
        verifyDirectory(fileName);
        RandomAccessFile rws = new RandomAccessFile(fileName, "rw");
        FileChannel fileChannel = rws.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.size()* MessageFuseLength);

        long index = fileChannel.size()/ MessageFuseLength;
        for (MessageFuse messageFuse : data) {
            byte[] bytes = ObjectByteUtils.toByteArray(messageFuse);
            if (bytes==null){
                throw new FileException("文件转化异常：object not can cast bytes");
            }
            messageFuse.setIndex(index++);

            byteBuffer.putLong(messageFuse.getOffset());
            byteBuffer.putInt(messageFuse.getLength());
            byteBuffer.putInt(messageFuse.getTagHashcode());
            byteBuffer.putLong(messageFuse.getIndex());
        }

        byteBuffer.flip();
        fileChannel.position(fileChannel.size());
        fileChannel.write(byteBuffer);

        fileChannel.force(true);
        fileChannel.close();
    }


    public static List<MessageFuse> readMessageQueue(String fileName, long index) throws IOException {
        List<MessageFuse> data = new ArrayList<>();

        RandomAccessFile rws = new RandomAccessFile(fileName, "rw");
        long offset = index * MessageFuseLength;

        FileChannel fileChannel = rws.getChannel();
        while (true){
            ByteBuffer byteBuffer = ByteBuffer.allocate(MessageFuseLength);
            int read = fileChannel.read(byteBuffer, offset);
            if (read<=0){
                break;
            }
            byteBuffer.flip();
            MessageFuse messageFuse = new MessageFuse();
            messageFuse.setOffset(byteBuffer.getLong());
            messageFuse.setLength(byteBuffer.getInt());
            messageFuse.setTagHashcode(byteBuffer.getInt());
            messageFuse.setIndex(byteBuffer.getLong());

            offset+= MessageFuseLength;

            data.add(messageFuse);
        }
        fileChannel.close();
        return data;
    }



    public static synchronized MessageFuse save(String fileName, Object object) throws IOException, InterruptedException{
        verifyDirectory(fileName);
        RandomAccessFile rws = new RandomAccessFile(fileName, "rw");
        FileChannel fileChannel = rws.getChannel();

        byte[] bytes = ObjectByteUtils.toByteArray(object);
        if (bytes==null){
            throw new FileException("文件转化异常：object not can cast bytes");
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length+4);
        byteBuffer.putInt(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();

        MessageFuse messageFuse = new MessageFuse();
        messageFuse.setLength(byteBuffer.limit());
        messageFuse.setOffset(fileChannel.size());

        fileChannel.position(fileChannel.size());
        fileChannel.write(byteBuffer);
        fileChannel.force(true);
        fileChannel.close();

        return messageFuse;
    }

    public static byte[] read(FileChannel fileChannel,long offset,int length) throws IOException{
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        fileChannel.read(byteBuffer,offset+4);
        byteBuffer.flip();
        byte[] bytes = new byte[length-4];
        byteBuffer.get(bytes);
        return bytes;
    }

    public static List<PullMessage> readMessages(String fileName, List<MessageFuse> messageFuse){
        List<PullMessage> data = new ArrayList<>();
        try {
            FileChannel fileChannel =
                    new RandomAccessFile(fileName, "r").getChannel();
            for (MessageFuse durability : messageFuse) {
                byte[] read = read(fileChannel, durability.getOffset(), durability.getLength());
                Object object = ObjectByteUtils.toObject(read);
                if (object==null){
                    continue;
                }
                if (object instanceof SendMessage){
                    SendMessage sendMessage = (SendMessage) object;
                    PullMessage pullMessage = new PullMessage();
                    pullMessage.setMsgId(sendMessage.getMsgId());
                    pullMessage.setBody(sendMessage.getBody());
                    pullMessage.setKey(sendMessage.getKey());
                    pullMessage.setTopic(sendMessage.getTopic());
                    pullMessage.setTag(sendMessage.getTag());
                    pullMessage.setIndex(durability.getIndex());
                    data.add(pullMessage);
                }
            }
            fileChannel.close();
            return data;
        } catch (Exception e) {
            log.error("read filer error",e);
        }
        return data;
    }

    public static List<MessageFuse> readMessageFuse(String fileName, long offset, int size, String topic){
        List<MessageFuse> data = new ArrayList<>();
        try {
            FileChannel fileChannel =
                    new RandomAccessFile(fileName, "r").getChannel();
            for (int i=0;i<size;i++){
                ByteBuffer byteBuffer = readBuff(fileChannel, offset, 4);
                if (byteBuffer==null){
                    break;
                }

                int length = byteBuffer.getInt();
                MessageFuse messageFuse = new MessageFuse();
                messageFuse.setOffset(offset);
                messageFuse.setLength(length+4);

                offset = offset+4;
                ByteBuffer bodyBuffer = readBuff(fileChannel, offset, length);
                offset = offset+length;

                if (bodyBuffer==null){
                    continue;
                }
                byte[] bytes = new byte[length];
                bodyBuffer.get(bytes);
                SendMessage sendMessage = (SendMessage)ObjectByteUtils.toObject(bytes);
                if (sendMessage==null||!topic.equals(sendMessage.getTopic())){
                    continue;
                }
                data.add(messageFuse);
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ByteBuffer readBuff(FileChannel fileChannel, long offset, int length) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        int read = fileChannel.read(byteBuffer, offset);
        if (read<=0){
            return null;
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public static void saveString(String fileName ,String object){
        try {
            //先删除
            if (Files.exists(Paths.get(fileName))){
                Files.delete(Paths.get(fileName));
            }
            //再写
            FileChannel fileChannel =
                    new RandomAccessFile(fileName, "rw").getChannel();

            byte[] bytes = object.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();

            fileChannel.write(byteBuffer);
            fileChannel.force(true);
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String fileName){
        try {
            FileChannel fileChannel =
                    new RandomAccessFile(fileName, "r").getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            int read = fileChannel.read(byteBuffer);
            if (read<=0){
                return "";
            }
            byteBuffer.flip();
            fileChannel.close();

            byte[] bytes = new byte[read];
            byteBuffer.get(bytes);

            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void verifyDirectory(String fileName){
        File file = new File(fileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
    }
}

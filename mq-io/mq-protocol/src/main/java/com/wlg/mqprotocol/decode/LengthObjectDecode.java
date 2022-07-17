package com.wlg.mqprotocol.decode;

import com.wlg.mqprotocol.entity.Head;
import com.wlg.mqprotocol.enums.MessageEnum;
import com.wlg.mqprotocol.protocol.DecodedData;
import com.wlg.mqprotocol.utils.ObjectByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: LengthObjectDecode
 */
public class LengthObjectDecode extends LengthFieldBasedFrameDecoder {


    private static final int FRAME_MAX_LENGTH =
            Integer.parseInt(System.getProperty("com.hsmq.frameMaxLength", "16777216"));

    public LengthObjectDecode() {
        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = null;

        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;

            }
            ByteBuffer byteBuffer = frame.nioBuffer();

            int headLength = byteBuffer.getInt();
            byte[] headData = new byte[headLength];
            byteBuffer.get(headData);

            Head head = Head.toHead(headData);
            if (head==null){
                return null;
            }

            int dataLength = byteBuffer.getInt();
            byte[] dataData = new byte[dataLength];
            byteBuffer.get(dataData);

            MessageEnum msgTypeEnum = head.getMsgTypeEnum();
            if (msgTypeEnum==null){
                return null;
            }

            DecodedData decodeData = new DecodedData();
            decodeData.setHead(head);
            decodeData.setData(ObjectByteUtils.toObject(dataData));
            decodeData.setMsgTypeEnum(msgTypeEnum);
            return decodeData;
        }catch (Exception e){
            e.printStackTrace();
            ctx.channel().close();
        }finally {
            if (frame!=null){
                frame.release();
            }
        }
        return null;
    }

}
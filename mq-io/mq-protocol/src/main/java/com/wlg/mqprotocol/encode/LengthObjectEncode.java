package com.wlg.mqprotocol.encode;

import com.wlg.mqprotocol.protocol.EncodedData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: LengthObjectEncode
 */
public class LengthObjectEncode extends MessageToByteEncoder<EncodedData> {


    @Override
    protected void encode(ChannelHandlerContext ctx, EncodedData encodeData, ByteBuf out) throws Exception {
        out.writeInt(encodeData.getLength());
        out.writeInt(encodeData.getHeadLength());
        out.writeBytes(encodeData.getHead());
        out.writeInt(encodeData.getDataLength());
        out.writeBytes(encodeData.getData());
    }
}
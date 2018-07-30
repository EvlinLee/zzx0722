package com.eryue.util;

import com.library.util.CommonFunc;

import java.io.DataInputStream;
import java.io.IOException;


/**
 * @author wchen.wa
 * @version 1.0.0 2009/11/10
 */
public class AdvanceReader {
    private DataInputStream mDataInputStream;

    /**
     * @param dis ������
     */
    public AdvanceReader(DataInputStream dis) {
        this.mDataInputStream = dis;
    }

    /**
     * @return byte
     * @throws IOException
     */
    public byte readByte() throws IOException {
        return this.mDataInputStream.readByte();
    }

    public long readCLong() throws IOException {
        byte[] temp = new byte[8];
        this.mDataInputStream.read(temp);
        return NumberUtils.byte8ToJavaILong(temp);
    }

    /**
     * @return int
     * @throws IOException
     */
    public int readCInt() throws IOException {
        byte[] temp = new byte[4];
        this.mDataInputStream.read(temp);
        return CommonFunc.byte4ToJavaInt(temp);
    }

    /**
     * @return int
     * @throws IOException
     */
    public int readCShortInt() throws IOException {
        byte[] temp = new byte[2];
        this.mDataInputStream.read(temp);
        return CommonFunc.byte2ToJavaInt(temp);
    }

    /**
     * @param len �ֽ���
     * @return String
     * @throws IOException
     */
    public String readString(int len) throws IOException {
        byte[] temp = new byte[len];
        this.mDataInputStream.read(temp);
        // for(int i = 0 ; i < temp.length / 2 ;){
        // i = 2 * ()
        // }"UTF-16LE"
        return Assist.string(temp, "UTF-8");
    }

    public String readString() throws IOException {
        int len = this.readCInt();
        return readString(len);
    }

    public byte[] readBytes(int len) throws IOException {
        byte[] bytes = new byte[len];
        this.mDataInputStream.read(bytes);
        return bytes;
    }

    /**
     * @throws IOException
     */
    public void close() throws IOException {
        this.mDataInputStream.close();
    }

    /**
     * @return byte
     * @throws IOException
     */
    public byte[] readByte(int i) throws IOException {
        byte b[] = new byte[i];
        this.mDataInputStream.read(b);
        return b;
    }

    /**
     * @return boolean
     * @throws IOException
     */
    public boolean readBoolean() throws IOException {
        return this.mDataInputStream.readBoolean();
    }

    public int available() throws IOException {
        return this.mDataInputStream.available();
    }

    public int readushort() throws IOException {
        byte[] bytes = new byte[2];
        this.mDataInputStream.read(bytes);
        return CommonFunc.byte2ToJavaInt(bytes);
    }
}

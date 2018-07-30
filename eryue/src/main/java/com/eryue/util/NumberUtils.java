package com.eryue.util;


/**
 * ����ת��������
 *
 * @author mldeng
 */
public class NumberUtils {

    /**
     * 整型数据转为字节数组
     *
     * @param n         待转换的整数
     * @param len       数组长度
     * @param bigEndian true 大端模式 高位字节排放在内存的低端，低位字节排放在内存的高端； false 小段模式
     *                  低位字节排放在内存的低端，高位字节排放在内存的高端
     * @return
     */
    public static byte[] toBytes(long n, int len, boolean bigEndian) {
        byte[] b = new byte[len];
        if (bigEndian) {
            for (int i = 0; i < len; i++) {
                b[i] = (byte) ((n >> ((len - 1 - i) * 8)) & 0xff);
            }
        } else {
            for (int i = 0; i < len; i++) {
                b[i] = (byte) ((n >> (i * 8)) & 0xff);
            }
        }
        return b;
    }

    /**
     * float���תΪ�ֽ�����
     *
     * @param value
     * @param bigEndian true ���ģʽ ��λ�ֽ��ŷ����ڴ�ĵͶˣ���λ�ֽ��ŷ����ڴ�ĸ߶ˣ� false С��ģʽ
     *                  ��λ�ֽ��ŷ����ڴ�ĵͶˣ���λ�ֽ��ŷ����ڴ�ĸ߶�
     * @return
     */
    public static byte[] floatToBytes(float value, boolean bigEndian) {
        int tmp = Assist.floatToIntBits(value);
        return toBytes(tmp, 4, bigEndian);
    }

    /**
     * double���תΪ�ֽ�����
     *
     * @param value
     * @param bigEndian true ���ģʽ ��λ�ֽ��ŷ����ڴ�ĵͶˣ���λ�ֽ��ŷ����ڴ�ĸ߶ˣ� false С��ģʽ
     *                  ��λ�ֽ��ŷ����ڴ�ĵͶˣ���λ�ֽ��ŷ����ڴ�ĸ߶�
     * @return
     */
    public static byte[] doubleToBytes(double value, boolean bigEndian) {
        long tmp = Assist.doubleToLongBits(value);
        //return toBytes(tmp, 4, bigEndian);
        return toBytes(tmp, 8, bigEndian);
    }

    /**
     * ��4���ֽ����ת��Ϊint����
     *
     * @param temp
     * @return
     */
    public static int byte4ToJavaInt(byte[] temp) {
        return (int) (0xff000000 & (temp[0] << 24)) + (int) (0x00ff0000 & (temp[1] << 16)) + (int) (0x0000ff00 & (temp[2] << 8)) + (int) (0x000000ff & (temp[3]));
    }

    /**
     * ��4���ֽ����ת��Ϊint����
     *
     * @param temp
     * @return
     */
    public static int byte4ToJavaIInt(byte[] temp) {
        return (int) (0xff000000 & (temp[3] << 24)) + (int) (0x00ff0000 & (temp[2] << 16)) + (int) (0x0000ff00 & (temp[1] << 8)) + (int) (0x000000ff & (temp[0]));
    }

    /**
     * ��2���ֽ����ת��Ϊshort����
     *
     * @param temp
     * @return
     */
    public static int byte2ToJavaShort(byte[] temp) {
        return (int) (0x00ff & (temp[1] << 0)) + (int) (0xff00 & (temp[0] << 8));
    }

    /**
     * ��2���ֽ����ת��Ϊshort����
     *
     * @param temp
     * @return
     */
    public static int byte2ToJavaIShort(byte[] temp) {
        return (int) (0x00ff & (temp[0] << 0)) + (int) (0xff00 & (temp[1] << 8));
    }

    /**
     * ��2���ֽ����ת��Ϊint����,
     *
     * @param temp
     * @return
     */
    public static int byte2ToJavaInt(byte[] temp) {
        return (int) ((int) (0x000000ff & (temp[1] << 0)) + (int) (0x0000ff00 & (temp[0] << 8)));
    }

    /**
     * ��8���ֽ����ת��Ϊlong����
     *
     * @param temp
     * @return
     */
//	public static long byte8ToJavaLong(byte[] temp) {
//		long value = 0L;
//		for (int i = 0; i < 8; i++) {
//			value |= (temp[i] & 0xff) << ((7 - i) * 8);
//		}
//		return value;
//	}
    public static long byte8ToJavaLong(byte[] temp) {
        long value = 0L;
        for (int i = 0; i < 8; i++) {
            value |= ((long) temp[i] & 0xff) << ((7 - i) * 8);
        }
        return value;
    }

    public static long byte8ToJavaILong(byte[] temp) {
        long value = 0L;
        for (int i = 0; i < 8; i++) {
            value |= ((long) temp[7 - i] & 0xff) << ((7 - i) * 8);
        }
        return value;
    }

    /**
     * ��4���ֽ����ת��Ϊfloat����
     *
     * @param temp
     * @return
     */
    public static float byte4ToJavaFloat(byte[] temp) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value |= (temp[i] & 0xff) << (i * 8);
        }
        return Assist.intBitsToFloat(value);
    }

    /**
     * ��8���ֽ����ת��Ϊdouble����
     *
     * @param temp
     * @return
     */
//	public static double byte8ToJavaDouble(byte[] temp) {
//		long value = 0;
//		for (int i = 0; i < 8; i++) {
//			value |= (temp[i] & 0xff) << (i * 8);
//		}
//		return Assist.longBitsToDouble(value);
//	}
//	public static double byte8ToJavaDouble(byte[] temp) {
//		long value = 0;
//		for (int i = 0; i < 8; i++) {
//			value |= ((long)temp[i] & 0xff) << (i * 8);
//		}
//		return Assist.longBitsToDouble(value);
//	}

    /**
     * ��8���ֽ����ת��Ϊdouble����
     *
     * @param temp
     * @return
     */
    public static double byte8ToJavaDouble(byte[] temp) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) temp[i] & 0xff) << ((7 - i) * 8);
        }
        return Assist.longBitsToDouble(value);
    }

    /**
     * ��8���ֽ����ת��Ϊdouble����
     *
     * @param temp
     * @return
     */
    public static double byte8ToJavaDoubleB(byte[] temp) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) temp[i] & 0xff) << (i * 8);
        }
        return Assist.longBitsToDouble(value);
    }
}

package cc.gnaixx.tools.util;

import java.nio.ByteBuffer;

import cc.gnaixx.tools.model.Uleb128;

import static cc.gnaixx.tools.model.DexCon.UINT_LEN;

/**
 * 名称: StreamUtil
 * 描述:
 *
 * @author xiangqing.xue
 * @date 2016/11/24
 */

public class BufferUtil {

    public static byte[] subdex(byte[] stream, int off, int len) {
        byte[] sub = new byte[len];
        for (int i = 0; i < len; i++) {
            sub[i] = stream[i + off];
        }
        return sub;
    }

    public static int getUint(byte[] buffer, int offset) {
        int value = 0;
        for (int i = 0; i < UINT_LEN; i++) {
            int seg = buffer[offset + i];
            if (seg < 0) {
                seg = 256 + seg;
            }
            value += seg << (8 * i);
        }
        offset += UINT_LEN; //int 四个字节
        return value;
    }

    public static Uleb128 getUleb128(byte[] stream, int off) {
        int value = 0;
        int count = 0;
        byte realVal[] = new byte[4];
        boolean flag = false;
        do {
            byte seg = stream[off];
            if ((seg >> 7) == 1) {
                flag = true;
            }
            seg = stream[off];
            value += ((seg << 1) >> 1) << (7 * count);
            realVal[count] = stream[off];
            count++;
            off++;
        } while (flag);
        return new Uleb128(realVal, value);
    }

    public static byte[] replace(byte[] source, byte[] replacement, int off, int len) {
        for (int i = 0; i < len; i++) {
            source[i+off] = replacement[i];
        }
        return source;
    }

    public static byte[] append(byte[] source, byte[] element, int len){
        ByteBuffer bb = ByteBuffer.allocate(source.length + element.length);
        bb.put(source);
        bb.put(element);
        return bb.array();
    }
}

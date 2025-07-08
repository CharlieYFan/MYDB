package top.guoziyang.mydb.transport;

import java.util.Arrays;

import com.google.common.primitives.Bytes;

import top.guoziyang.mydb.common.Error;

/**
 * 加密解码
 */
public class Encoder {

    public byte[] encode(Package pkg) {
        if(pkg.getErr() != null) {
            Exception err = pkg.getErr();
            String msg = "Intern server error!";
            if(err.getMessage() != null) {
                msg = err.getMessage();
            }
            //(加密) 将一个字节 1 和字符串 msg 的字节表示进行拼接，生成一个新的字节数组
            /**
             * 假设 msg = "Hello"，那么：
             * new byte[]{1} → [0x01]
             * "Hello".getBytes() → [0x48, 0x65, 0x6C, 0x6C, 0x6F]
             * Bytes.concat([0x01], [0x48, 0x65, 0x6C, 0x6C, 0x6F]) → [0x01, 0x48, 0x65, 0x6C, 0x6C, 0x6F]
             * 这里 0x01 是错误err标识
             */
            return Bytes.concat(new byte[]{1}, msg.getBytes());
        } else {
            /**
             * (加密) 0x00 是正常数据标识
             */
            return Bytes.concat(new byte[]{0}, pkg.getData());
        }
    }

    public Package decode(byte[] data) throws Exception {
        if(data.length < 1) {
            throw Error.InvalidPkgDataException;
        }
        if(data[0] == 0) {
            /**
             * (解密) 0x00 是正常数据标识
             */
            return new Package(Arrays.copyOfRange(data, 1, data.length), null);
        } else if(data[0] == 1) {
            /**
             * (解密) 0x01 是错误err标识
             * Arrays.copyOfRange 取到的是加密时加入的msg
             */
            return new Package(null, new RuntimeException(new String(Arrays.copyOfRange(data, 1, data.length))));
        } else {
            throw Error.InvalidPkgDataException;
        }
    }

}

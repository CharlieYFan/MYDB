package top.guoziyang.mydb.transport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * 数据传输器，封装BufferedReader、BufferedWriter
 * 传输编码之后的数据
 */
public class Transporter {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Transporter(Socket socket) throws IOException {
        this.socket = socket;
        /**
         * TODO 知识点：
         * BufferedReader BufferedWriter
         * InputStreamReader OutputStreamWriter
         */
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * 发送数据方法
     * @param data
     * @throws Exception
     */
    public void send(byte[] data) throws Exception {
        String raw = hexEncode(data);
        // write - 写入缓冲区
        writer.write(raw);
        /**
         * BufferedWriter 是带 缓冲 的字符输出流。
         * 默认情况下，只有当缓冲区 满 时才会自动刷新（写入）数据。
         * flush - 强制将缓冲区中的数据立即写入目标输出流（如网络 Socket），确保数据不会滞留在内存缓冲区中
         */
        writer.flush();
    }

    /**
     * 接收数据方法
     * @return
     * @throws Exception
     */
    public byte[] receive() throws Exception {
        String line = reader.readLine();//hexEncode方法加了\n
        if(line == null) {
            close();
        }
        return hexDecode(line);
    }

    public void close() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }

    /**
     * 将字节数组 buf 编码为十六进制字符串，并在末尾添加换行符 \n
     * (将二进制数据转为可打印的十六进制字符串)
     * @param buf
     * @return
     */
    private String hexEncode(byte[] buf) {
        /**
         * byte[] bytes = "Hello".getBytes();  --》
         * byte[] bytes = new byte[]{0x01, 0x48, 0x65, 0x6C, 0x6C, 0x6F};
         * return "0148656c6c6f\n"
         * 01 → 对应字节 0x01
         * 48 → 对应字符 'H'
         * 65 → 对应字符 'e'
         * 6c → 对应字符 'l'
         * 6c → 再次 'l'
         * 6f → 'o'
         * 最后加一个换行符 \n  --》 这样接收方可以通过 BufferedReader.readLine() 按行读取每条消息。(receive方法)
         */
        return Hex.encodeHexString(buf, true)+"\n";
    }

    /**
     * 将十六进制字符串解码为字节数组
     * @param buf
     * @return
     * @throws DecoderException
     */
    private byte[] hexDecode(String buf) throws DecoderException {
        /**
         * buf = "48656c6c6f"
         * 输出：ASCII
         * 72 --> 'H'
         * 101 --> 'e'
         * 108 --> 'l'
         * 108 --> 'l'
         * 111 --> 'o'
         */
        return Hex.decodeHex(buf);
    }
}

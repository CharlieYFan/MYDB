package top.guoziyang.mydb.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import top.guoziyang.mydb.transport.Encoder;
import top.guoziyang.mydb.transport.Packager;
import top.guoziyang.mydb.transport.Transporter;

public class Launcher {
    public static void main(String[] args) throws UnknownHostException, IOException {
        //java.net.Socket类用于客户端
        //1.创建Socket并指定服务器地址和端口
        Socket socket = new Socket("127.0.0.1", 9999);
        //编解码 - 用于将数据打上标识01err / 00正常，并根据标识取数据
        Encoder e = new Encoder();
        //传输器 - 用于发送、接收数据；并对数据做十六进制编解码转换
        Transporter t = new Transporter(socket);
        //封装编解码器 + 传输器
        Packager packager = new Packager(t, e);

        Client client = new Client(packager);
        Shell shell = new Shell(client);
        shell.run();
    }
}

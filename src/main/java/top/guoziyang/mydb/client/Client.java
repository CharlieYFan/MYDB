package top.guoziyang.mydb.client;

import top.guoziyang.mydb.transport.Package;
import top.guoziyang.mydb.transport.Packager;

public class Client {
    private RoundTripper rt;

    public Client(Packager packager) {
        this.rt = new RoundTripper(packager);
    }

    public byte[] execute(byte[] stat) throws Exception {
        //stat是控制台输入的sql语句
        Package pkg = new Package(stat, null);
        //RoundTripper发送并接收处理结果
        Package resPkg = rt.roundTrip(pkg);
        if(resPkg.getErr() != null) {
            throw resPkg.getErr();
        }
        return resPkg.getData();
    }

    public void close() {
        try {
            rt.close();
        } catch (Exception e) {
        }
    }

}

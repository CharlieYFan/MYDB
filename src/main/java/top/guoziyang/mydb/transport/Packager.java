package top.guoziyang.mydb.transport;

/**
 * 包装传输器和编码器 进行发送接收操作
 */
public class Packager {
    private Transporter transpoter;
    private Encoder encoder;

    public Packager(Transporter transpoter, Encoder encoder) {
        this.transpoter = transpoter;
        this.encoder = encoder;
    }

    public void send(Package pkg) throws Exception {
        byte[] data = encoder.encode(pkg);
        transpoter.send(data);
    }

    public Package receive() throws Exception {
        byte[] data = transpoter.receive();
        // data带有标识 01 / 00
        return encoder.decode(data);
    }

    public void close() throws Exception {
        transpoter.close();
    }
}

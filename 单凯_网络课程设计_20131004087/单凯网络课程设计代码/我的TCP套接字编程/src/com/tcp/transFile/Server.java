package com.tcp.transFile;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ������
 */
public class Server extends ServerSocket{

    private static final int PORT = 2013;
    
    private ServerSocket server;
    private Socket client;
    private DataInputStream dis;
    private FileOutputStream fos;
    
    public Server() throws Exception{
        try {
            try {
                server = new ServerSocket(PORT);
                
                while(true){
                    client = server.accept();
                    
                    dis = new DataInputStream(client.getInputStream());
                    //�ļ����ͳ���
                    String fileName = dis.readUTF();
                    long fileLength = dis.readLong();
                    fos = new FileOutputStream(new File("d:/" + fileName));
                    
                    byte[] sendBytes = new byte[1024];
                    int transLen = 0;
                    System.out.println("----��ʼ�����ļ�<" + fileName + ">,�ļ���СΪ<" + fileLength + ">----");
                    while(true){
                        int read = 0;
                        read = dis.read(sendBytes);
                        if(read == -1)
                            break;
                        transLen += read;
                        System.out.println("�����ļ�����" + 100 * transLen/fileLength + "%...");
                        fos.write(sendBytes, 0, read);
                        fos.flush();
                    }
                    System.out.println("----�����ļ�<" + fileName + ">�ɹ�-------");
                    client.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(dis != null)
                    dis.close();
                if(fos != null)
                    fos.close();
                server.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new Server();
    }
}
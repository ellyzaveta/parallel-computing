package lab4_5.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Services {

    public static void sendText(String data, DataOutputStream dos) {
        try {
            sendInt(data.length(), dos);

            dos.write(data.getBytes(StandardCharsets.UTF_8));
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendInt(int value, DataOutputStream dos) {
        byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(value).array();
        try {
            dos.write(bytes);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int receiveInt(DataInputStream dis) {
        try {
            byte[] bytes = new byte[4];
            dis.readFully(bytes);
            return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String receiveText(DataInputStream dis) {
        try {
            int len = receiveInt(dis);

            byte[] data = new byte[len];
            dis.readFully(data);

            return new String(data, 0, data.length - 1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static char[] receiveCharArr(DataInputStream dis) {
        try {
            int len = receiveInt(dis);

            byte[] data = new byte[len];
            dis.readFully(data);

            byte[] newData = new byte[data.length - 1];
            System.arraycopy(data, 0, newData, 0, data.length - 1);

            return new String(newData, StandardCharsets.UTF_8).toCharArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendCharArr(char[] arr, DataOutputStream dos) {
        sendInt(arr.length, dos);

        byte[] byteArray = new String(arr).getBytes(StandardCharsets.UTF_8);

        try {
            dos.write(byteArray);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sendStatus(char[] inputArr, DataOutputStream dos, TaskProcessing tp) {
        Services.sendInt(inputArr.length, dos);

        while(!tp.getStatus()) {
            Services.sendText("cont", dos);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Services.sendInt(tp.getProgress(), dos);
        }

        Services.sendText("stop", dos);
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendCurrentStatus(TaskProcessing tp, DataOutputStream dos) { //can be used to get the current status instead of sendStatus()
                                                                                //in combination with receiveCurrentStatus() in Client
        if(tp.getStatus()) sendText("result is ready. use command 'RESULT' to receive.", dos);
        else sendText("task is still processing...", dos);
    }

}

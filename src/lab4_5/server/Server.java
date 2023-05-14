package lab4_5.server;

import lab1.StringReverse;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    public Server(int port) {

        ServerSocket ss;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            Socket s = null;

            try {
                s = ss.accept();

                System.out.println("\na new client is connected : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                Thread t = new ClientHandler(s, dis, dos);

                System.out.println("assigning new thread for this client " + t);

                t.start();

            } catch (Exception e) {
                try {
                    assert s != null;
                    s.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server(5056);
    }
}

class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {

        System.out.println();

        String mes = "the connection is established. expecting for [array length, char array, num of threads]...";
        System.out.println("<--- " + mes);

        Services.sendText("-> SERVER: " + mes, dos);

        TaskProcessing tp = new TaskProcessing();

        char[] inputArr = null;
        int numOfThreads = 0;

        outerLoop:
        while (true) {

            String command = Services.receiveText(dis);

            System.out.println("-> CLIENT (port=" + s.getPort() + "): " + command);

            Services.sleep(1);
            mes = "command " + command + " received";
            Services.sendText("-> SERVER: " + mes, dos);

            if (command.equals("EXIT")) {
                Services.sendText("Command EXIT received. Connection closed", dos);
                System.out.println("Client (port=" + s.getPort() + ") - connection closed.");
                break;
            }

            switch (command) {

                case "SEND" -> {

                    inputArr = Services.receiveCharArr(dis);
                    numOfThreads = Services.receiveInt(dis);

                    System.out.println("-> CLIENT (port=" + s.getPort() + "): sending data...");
                    Services.sleep(1);

                    if (inputArr.length == 0 || numOfThreads == 0) {
                        mes = "incorrect data";
                        System.out.println("<--- " + mes);

                        Services.sendText("-> SERVER: " + mes, dos);
                        break outerLoop;
                    }

                    mes = "char arr of length " + inputArr.length + " received. num of threads: " + numOfThreads;
                    System.out.println("<--- " + mes);
                    Services.sendText("-> SERVER: " + mes, dos);

                }

                case "START" -> {
                    if(inputArr == null) break outerLoop;
                    else {
                        System.out.println("<--- sending status to client (port=" + s.getPort() + ")");
                        tp.addTask(inputArr, numOfThreads);
                        Services.sendStatus(inputArr, dos, tp);
                        Services.sleep(1);
                    }
                }

                case "RESULT" -> {
                    if(inputArr == null) break outerLoop;
                    else {
                        System.out.println("<--- sending result to client (port=" + s.getPort() + ")");
                        char[] arr = tp.getResult();
                        Services.sendCharArr(arr, dos);
                    }
                }
            }
        }

        try {
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class TaskProcessing {

    private CompletableFuture<char[]> taskProcessingThread;
    private final AtomicInteger progress;

    public TaskProcessing() {
        progress = new AtomicInteger(0);
        taskProcessingThread = new CompletableFuture<>();
    }

    public void addTask(char[] arr, int numOfThreads) {
        taskProcessingThread = CompletableFuture.supplyAsync(() -> {
            char[] res;
            try {
                res = StringReverse.parallelSolution(arr, numOfThreads, progress);
            } catch (InterruptedException e) {
                throw new RuntimeException("Task execution was interrupted", e);
            }
            return res;
        });
    }

    public boolean getStatus() {
        return taskProcessingThread.isDone();
    }

    public int getProgress() {
        return progress.get();
    }

    public char[] getResult() {
        if (taskProcessingThread.isDone()) {
            try {
                return taskProcessingThread.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error retrieving task result", e);
            }
        }
        else return null;
    }
}

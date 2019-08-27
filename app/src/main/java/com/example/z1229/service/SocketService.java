package com.example.z1229.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.z1229.bean.Message;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketService extends Service {

    private Handler handler = new Handler(Looper.getMainLooper());
//    private static final String IP="10.0.2.2";
    private static final String IP="47.95.146.87";
    private static final int PORT=5678;
    private PrintWriter writer;
    private Socket socket;
    private static final long HEART_BEAT_RATE = 5 * 1000;//心跳间隔
    private long sendTime = 0L;
    private Gson gson = new Gson();
    LocalBroadcastManager localBroadcastManager;
    ReadThread readThread;
    Handler mHandler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder{
        public boolean send(String string){
            return sendMsg(string);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.postDelayed(heartRunnable,0);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(heartRunnable);
        if(readThread!=null){
            readThread.releaseR();
        }
        release();
    }

    private void connect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP,PORT);
                    readThread = new ReadThread(socket);
                    readThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    msgToast("服务器连接失败！");
                }
            }
        }).start();
    }

    private Runnable heartRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                Message message = new Message("心跳","");
                boolean isSuccess = sendMsg(gson.toJson(message));
                if (!isSuccess) {
                    mHandler.removeCallbacks(heartRunnable);
                    if(readThread!=null){
                        readThread.releaseR();
                    }
                    release();
                    connect();
                }
            }
            mHandler.postDelayed(this,HEART_BEAT_RATE);
        }
    };


    private void release(){//释放资源
        if (socket != null) {
            if(!socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket=null;
        }
    }

    private void msgToast(final String string){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean sendMsg(final String msg){//发送消息
        if(socket != null&&socket.isConnected()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        writer=new PrintWriter(socket.getOutputStream());
                        writer.println(msg);
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            sendTime=System.currentTimeMillis();
        }else{
            return false;
        }
        return true;
    }

    class ReadThread extends Thread{//接收消息
        Socket socket = new Socket();
        private BufferedReader reader;

        public ReadThread(Socket socket){
            this.socket=socket;
        }

        public void releaseR(){
            Thread.currentThread().interrupt();
        }

        @Override
        public void run() {
            super.run();
            if(null!=socket){
                try {
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line=null;
                    while(!socket.isInputShutdown()&&!socket.isClosed()&&(line=reader.readLine())!=null){
                        System.out.println(line);
                        Intent intent = new Intent("com.example.z1229.receiver.socketReceiver");
                        intent.putExtra("text",line);
                        localBroadcastManager.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    release();
                    e.printStackTrace();
                }
            }
        }
    }
}

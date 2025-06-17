package com.example.musicappdemo.music;

import static com.example.musicappdemo.page.SongPage.HTTP_CLIENT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.musicappdemo.R;
import com.example.musicappdemo.entity.LrcLine;
import com.example.musicappdemo.entity.vo.MusicVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MusicService extends Service {
    int currentPosition;
    List<LrcLine> lrcLines = new ArrayList<>();
    private MediaPlayer player; //定义多媒体播放器变量
    private Timer timer;        //定义计时器变量
    private static final int NOTIFICATION_ID = 1;
    public MusicService() {
    }    //构造函数

    private static final String CHANNEL_ID = "music_channel";

    private NotificationCompat.Builder notificationBuilder;
    private String musicTitle = "";

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl(this);
    }

    public interface OnLyricUpdateListener {
        void onLyricUpdated(String currentLrcLine);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();//创建音乐播放器对象
        //createNotificationChannel(); // 创建通知渠道
        // 初始化通知（避免后续空指针）
        //updateNotification(false); // 初始化为暂停状态
        //startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    /*// 创建通知渠道（Android 8.0+必需）
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Music playback controls");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // 更新通知的方法
    private void updateNotification(boolean isPlaying) {
        // 创建操作按钮的PendingIntent
        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction("ACTION_PLAY");
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent prevIntent = new Intent(this, MusicService.class);
        prevIntent.setAction("ACTION_PREV");
        PendingIntent prevPendingIntent = PendingIntent.getService(this, 1, prevIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction("ACTION_NEXT");
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 2, nextIntent, PendingIntent.FLAG_IMMUTABLE);

        // 关键修改：将新创建的builder赋值给成员变量notificationBuilder
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle("正在播放")
                .setContentText(musicTitle)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .addAction(R.drawable.pre, "上一首", prevPendingIntent)
                .addAction(isPlaying ? R.drawable.pause : R.drawable.play,
                        isPlaying ? "暂停" : "播放", playPendingIntent)
                .addAction(R.drawable.next, "下一首", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2));

        Notification notification = notificationBuilder.build();
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(NOTIFICATION_ID, notification);
    }

    // 启动前台服务
    private void startForegroundService(String musicTitle) {
        this.musicTitle = musicTitle;
        updateNotification(true);  // 初始化 notificationBuilder
        startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    // 处理Intent动作
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "ACTION_PLAY":
                        if (player.isPlaying()) {
                            pausePlay();
                        } else {
                            continuePlay();
                        }
                        break;
                    case "ACTION_PREV":
                        // 处理上一首逻辑，需要与Activity通信
                        break;
                    case "ACTION_NEXT":
                        // 处理下一首逻辑，需要与Activity通信
                        break;
                }
            }
        }
        return START_STICKY;
    }*/

    //添加计时器用于设置音乐播放器中的播放进度条
    private void addTimer(List<LrcLine> lines) {
        if (timer == null) {
            stopTimer(); //先停止旧定时器，避免重复
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (player == null || !player.isPlaying()) return;
                    int duration = player.getDuration();//获取歌曲总时长
                    int currentPosition = player.getCurrentPosition();//获取播放进度
                    Message msg = MusicActivity.handler.obtainMessage();//创建消息对象
                    //将音乐的总时长和播放进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列
                    MusicActivity.handler.sendMessage(msg);
                    String currentLrcLine = findCurrentLrcLine(lines, currentPosition);
                    if (onLyricUpdateListener != null) {
                        onLyricUpdateListener.onLyricUpdated(currentLrcLine);
                    }
                }
            };
            // 调整延时和周期以适应你的需求，比如每500毫秒执行一次
            timer.schedule(task, 5, 500);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void pausePlay() {
        player.pause();
        stopTimer();
    }

    public void continuePlay() {
        player.start();
        // 如果需要在继续播放时重新启动定时器
        addTimer(lrcLines); // 假设这里的 lrcLines 是之前传入的数据
    }

    private void parseMusicLrc(String lrcUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "");

                    Request request = new Request.Builder()
                            .url(lrcUrl)
                            .get()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    lrcLines = parseLrcTextToLines(json);
                    addTimer(lrcLines);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    // 解析歌词文本方法
    private List<LrcLine> parseLrcTextToLines(String rawLrcText) {
        List<LrcLine> lines = new ArrayList<>();

        String[] rawLines = rawLrcText.split("\n");

        for (String rawLine : rawLines) {
            if (!rawLine.startsWith("[") || rawLine.contains("作词") || rawLine.contains("作曲") || rawLine.contains("编曲") || rawLine.contains("制作人")) {
                continue;
            }
            if (rawLine.contains("ti") || rawLine.contains("ar") || rawLine.contains("al") || rawLine.contains("by") || rawLine.contains("offset")) {
                continue;
            }
            if (rawLine.contains("id") || rawLine.contains("hash") || rawLine.contains("sign") || rawLine.contains("qq")) {
                continue;
            }
            if (rawLine.contains("total")) {
                continue;
            }
            int timeTagEndIndex = rawLine.indexOf(']');
            String timeStr = rawLine.substring(1, timeTagEndIndex);
            String lyricText = rawLine.substring(timeTagEndIndex + 1).trim();

            long timestamp = parseTimestamp(timeStr);
            lines.add(new LrcLine(timestamp, lyricText));
        }

        return lines;
    }


    // 解析时间标签的方法
    private long parseTimestamp(String timeStr) {
        String[] parts = timeStr.split(":|\\.");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        int milliseconds = Integer.parseInt(parts[2]);

        return minutes * 60 * 1000 + seconds * 1000 + milliseconds;
    }

    private String findCurrentLrcLine(List<LrcLine> lrcLines, int currentPosition) {
        for (int i = 0; i < lrcLines.size(); i++) {
            if (currentPosition < lrcLines.get(i).getTimestamp()) {
                return i == 0 ? "" : lrcLines.get(i - 1).getText();
            }
        }
        return lrcLines.isEmpty() ? "" : lrcLines.get(lrcLines.size() - 1).getText();
    }

    // 添加播放结束监听器
    private void setupCompletionListener() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 通知Activity歌曲播放结束
                if (onCompletionListener != null) {
                    onCompletionListener.onSongCompleted();
                }
            }
        });
    }

    // 播放完成回调接口
    public interface OnCompletionListener {
        void onSongCompleted();
    }
    private OnCompletionListener onCompletionListener;

    public void setOnCompletionListener(OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }

    //定义音乐播放控制类
    class MusicControl extends Binder {//Binder是一种跨进程的通信方式
        //播放音乐
        private MusicService musicService;

        public MusicControl(MusicService service) {
            this.musicService = service;
        }

        public MusicService getService() {
            return musicService;
        }

        public void play(MusicVO musicVO) {
            if (musicVO == null || musicVO.getMusicUrl() == null)
                return;
            setOnLyricUpdateListener(onLyricUpdateListener);
            Uri uri = Uri.parse(musicVO.getMusicUrl());
            try {
                //重置音乐播放器
                player.reset();
                stopTimer();
                //加载多媒体文件
                player.reset();
                player.setDataSource(getApplicationContext(), uri);
                player.prepare();
                player.start();
                //播放音乐
                player.start();
                setupCompletionListener(); // 设置播放完成监听
                parseMusicLrc(musicVO.getLrcUrl());
                //添加计时器
            } catch (Exception e) {
                e.printStackTrace();
            }
            //startForegroundService(musicVO.getMusicName()); // 启动前台服务
            //updateNotification(true);
        }

        public void pausePlay() {
            player.pause();              //暂停播放音乐
            //updateNotification(true);
        }

        public void continuePlay() {
            player.start();              //继续播放音乐
            //updateNotification(true);
        }

        public void seekTo(int progress) {
            player.seekTo(progress);     //设置音乐的播放位置
        }

        public void playNewMusic(String url, String lrcUrl) {
            if (url == null || url.isEmpty()) return;

            try {
                stopTimer(); // 停止之前的定时器
                player.reset(); // 重置播放器
                player.setDataSource(getApplicationContext(), Uri.parse(url)); // 设置新数据源
                player.prepare(); // 准备播放
                player.start(); // 播放
                setupCompletionListener(); // 设置播放完成回调
                parseMusicLrc(lrcUrl); // 如果有歌词地址就解析歌词
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("MusicService", "播放新音乐失败: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);  // 停止前台服务并移除通知
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private OnLyricUpdateListener onLyricUpdateListener;

    public void setOnLyricUpdateListener(OnLyricUpdateListener listener) {
        this.onLyricUpdateListener = listener;
    }


}
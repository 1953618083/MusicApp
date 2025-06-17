package com.example.musicappdemo.music;

import static com.example.musicappdemo.page.SongPage.HTTP_CLIENT;
import static com.example.musicappdemo.page.SongPage.musicDB;
import static com.example.musicappdemo.page.SongPage.musicNameList;
import static com.example.musicappdemo.page.SongPage.musicPicList;
import static java.lang.Integer.parseInt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.musicappdemo.R;
import com.example.musicappdemo.entity.LrcLine;
import com.example.musicappdemo.entity.vo.MusicVO;

import com.example.musicappdemo.utils.NetWorkAPi;
import com.example.musicappdemo.utils.SharedPreferenceUtil;
import com.example.musicappdemo.view.LrcView;

import org.json.JSONObject;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MusicActivity extends AppCompatActivity implements View.OnClickListener, MusicService.OnLyricUpdateListener,MusicService.OnCompletionListener {
    private HorizontalScrollView scrollView;

    private List<LrcLine> lrcLines;
    private TextView lyricsTextView;
    //定义歌曲名称的数组
    private static SeekBar sb;//定义进度条
    private static TextView tv_progress, tv_total, name_song;//定义开始和总时长,歌曲名控件
    private ObjectAnimator animator;//定义旋转的动画
    private MusicService.MusicControl musicControl;//音乐控制类

    private MusicService musicService;

    private Button loveButton;
    private Button collectButton;
    private ImageButton playModeButton; // 播放模式按钮
    private Button play;        //播放按钮
    private Button pause;       //暂停按钮
    private Button con;         //继续播放按钮
    private Button pre;         //上一首按钮
    private Button next;        //下一首按钮
    private ImageView exit;        //退出按钮
    private ImageView iv_music; //歌手图片框

    private TextView tv_title;//标题
    Intent intent1, intent2;    //定义两个意图
    MyServiceConn conn;         //服务连接
    private boolean isUnbind = false;//记录服务是否被解绑
    public int change = 0;      //记录下标的变化值
    LrcView lyricView;

    private MusicVO musicVO;
    // 播放模式相关
    private int playMode = 0; // 0:顺序播放 1:单曲循环 2:随机播放
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        //去除标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //获得意图
        intent1 = getIntent();
        //初始化
        initView();
    }


    //初始化
    private void initView() {
        //依次绑定控件
        tv_progress = findViewById(R.id.tv_progress);
        tv_total = findViewById(R.id.tv_total);
        sb = findViewById(R.id.sb);
        name_song = findViewById(R.id.song_name);
        tv_title = findViewById(R.id.tv_title);
        iv_music = findViewById(R.id.iv_music);

        loveButton = findViewById(R.id.loveBtn);
        collectButton = findViewById(R.id.collectBtn);
        playModeButton = findViewById(R.id.btn_play_mode); // 播放模式按钮
        lyricView = new LrcView(this);
        lyricsTextView = findViewById(R.id.lyricsTextView);

        play = findViewById(R.id.btn_play);
        pause = findViewById(R.id.btn_pause);
        con = findViewById(R.id.btn_continue_play);
        pre = findViewById(R.id.btn_pre);
        next = findViewById(R.id.btn_next);
        exit = findViewById(R.id.btn_exit);

        //依次设置监听器
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        con.setOnClickListener(this);
        pre.setOnClickListener(this);
        next.setOnClickListener(this);
        exit.setOnClickListener(this);
        playModeButton.setOnClickListener(this); // 播放模式按钮监听

        loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loveMusicApi(musicVO);
            }
        });


        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectMusicApi(musicVO);
            }
        });


        //创建意图对象
        intent2 = new Intent(this, MusicService.class);
        conn = new MyServiceConn();//创建服务连接对象
        bindService(intent2, conn, BIND_AUTO_CREATE);//绑定服务
        // 在适当的地方绑定 MusicService
        //从歌曲列表传过来的歌曲名
        String name = intent1.getStringExtra("name");
        //设置歌曲名显示
        name_song.setText(name);
        //定义歌曲列表传过来的下标position
        int position = Integer.parseInt(getIntent().getStringExtra("position"));
        musicVO = musicDB.get(position);
        getLoveMusicStatus();
        getCollectMusicStatus();

        //图像框设置为frag1里面的图标数组，下标为i
        Glide.with(getApplicationContext()).load(musicPicList.get(position)).transform(new CircleCrop()).into(iv_music);
        //为滑动条添加事件监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //当滑动条到末端时，将message对象发送出去
                if (progress == sb.getMax()) {
                    // 根据播放模式处理歌曲结束逻辑
                    handleSongEnd();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//滑动条开始滑动时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//滑动条停止滑动时调用
                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();//获取seekBar的进度
                musicControl.seekTo(progress);//改变播放进度
            }
        });

        animator = ObjectAnimator.ofFloat(iv_music, "rotation", 0f, 360.0f);
        animator.setDuration(10000);//动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//-1表示设置动画无限循环

    }

    // 切换播放模式
    private void switchPlayMode() {
        playMode = (playMode + 1) % 3; // 循环切换0,1,2

        // 更新播放模式按钮图标
        switch (playMode) {
            case 0:
                playModeButton.setImageResource(R.drawable.ic_play_mode_normal);
                break;
            case 1:
                playModeButton.setImageResource(R.drawable.ic_play_mode_single);
                break;
            case 2:
                playModeButton.setImageResource(R.drawable.ic_play_mode_random);
                break;
        }

        // 可以在这里添加Toast提示
        String modeText = "";
        switch (playMode) {
            case 0: modeText = "顺序播放"; break;
            case 1: modeText = "单曲循环"; break;
            case 2: modeText = "随机播放"; break;
        }
        // Toast.makeText(this, modeText, Toast.LENGTH_SHORT).show();
    }

    private void getLoveMusicStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SharedPreferenceUtil.getString("userId",""));
                    map.put("musicId", musicVO.getId());

                    JSONObject jsonObject = new JSONObject(map);
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.loveMusicInfo)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    String status = resultObject.getJSONObject("data").getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("1".equals(status)) {
                                loveButton.setBackground(getDrawable(R.drawable.love_clicked));
                            } else if ("0".equals(status)) {
                                loveButton.setBackground(getDrawable(R.drawable.love));
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }


    private void getCollectMusicStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SharedPreferenceUtil.getString("userId",""));
                    map.put("musicId", musicVO.getId());

                    JSONObject jsonObject = new JSONObject(map);
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.collectMusicInfo)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    String status = resultObject.getJSONObject("data").getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("1".equals(status)) {
                                collectButton.setBackground(getDrawable(R.drawable.collect_clicked));
                            } else if ("0".equals(status)) {
                                collectButton.setBackground(getDrawable(R.drawable.collect));
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    private void loveMusicApi(MusicVO musicVO) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SharedPreferenceUtil.getString("userId",""));
                    map.put("musicId", musicVO.getId());

                    JSONObject jsonObject = new JSONObject(map);
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.addLoveMusic)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    String status = resultObject.getJSONObject("data").getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("1".equals(status)) {
                                loveButton.setBackground(getDrawable(R.drawable.love_clicked));
                            } else if ("0".equals(status)) {
                                loveButton.setBackground(getDrawable(R.drawable.love));
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void collectMusicApi(MusicVO musicVO) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SharedPreferenceUtil.getString("userId",""));
                    map.put("musicId", musicVO.getId());

                    JSONObject jsonObject = new JSONObject(map);
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.addCollectMusic)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    String status = resultObject.getJSONObject("data").getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("1".equals(status)) {
                                collectButton.setBackground(getDrawable(R.drawable.collect_clicked));
                            } else if ("0".equals(status)) {
                                collectButton.setBackground(getDrawable(R.drawable.collect));
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    //歌曲进度条的消息机制
    public static Handler handler = new Handler() {//创建消息处理器对象
        //在主线程中处理从子线程发送过来的消息
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();//获取从子线程发送过来的音乐播放进度
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPosition);
            //歌曲总时长，单位为毫秒
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMinute = null;
            String strSecond = null;
            if (minute < 10) {//如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {//如果歌曲中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }
            tv_total.setText(strMinute + ":" + strSecond);
            //歌曲当前播放时长
            minute = currentPosition / 1000 / 60;
            second = currentPosition / 1000 % 60;
            if (minute < 10) {//如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            } else {
                strMinute = minute + " ";
            }
            if (second < 10) {//如果歌曲中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            } else {
                strSecond = second + " ";
            }
            tv_progress.setText(strMinute + ":" + strSecond);
        }
    };


    //用于实现连接服务
    class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
            musicService = musicControl.getService();
            musicService.setOnLyricUpdateListener(MusicActivity.this);
            musicService.setOnCompletionListener(MusicActivity.this); // 设置播放完成监听
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    // 实现播放完成回调
    @Override
    public void onSongCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleSongEnd(); // 处理歌曲结束逻辑
            }
        });
    }
    // 处理歌曲结束的逻辑
    private void handleSongEnd() {
        //String index = intent1.getStringExtra("position");
        int currentIndex = musicDB.indexOf(musicVO); // 直接获取当前歌曲的索引

        switch (playMode) {
            case 0: // 顺序播放
                if (currentIndex == musicDB.size() - 1) {
                    // 如果是最后一首，停止播放
                    musicControl.pausePlay();
                    animator.pause();
                    tv_title.setText("播放结束");
                } else {
                    // 播放下一首
                    playNextSong(currentIndex + 1);
                }
                break;

            case 1: // 单曲循环
                // 重新播放当前歌曲
                musicControl.play(musicDB.get(currentIndex));
                break;

            case 2: // 随机播放
                int randomIndex;
                do {
                    randomIndex = random.nextInt(musicDB.size());
                } while (randomIndex == currentIndex); // 确保不是当前歌曲
                playNextSong(randomIndex);
                break;
        }
    }

    // 播放指定位置的歌曲
    private void playNextSong(int newIndex) {
        String originalIndex = intent1.getStringExtra("position");
        int originalPosition = Integer.parseInt(originalIndex);
        change = newIndex - originalPosition; // 更新change值

        MusicVO nextMusic = musicDB.get(newIndex);
        Glide.with(getApplicationContext())
                .load(musicPicList.get(newIndex))
                .transform(new CircleCrop())
                .into(iv_music);
        name_song.setText(musicNameList.get(newIndex));
        musicControl.play(nextMusic);
        pause.setVisibility(View.VISIBLE);
        play.setVisibility(View.INVISIBLE);
        animator.start();
        tv_title.setText("正在播放");
        musicVO = nextMusic;
        getLoveMusicStatus();
        getCollectMusicStatus();
    }

    private void changeMusic(int index) {
        musicVO = musicDB.get(index);
        name_song.setText(musicVO.getMusicName());

        Glide.with(getApplicationContext())
                .load(musicPicList.get(index))
                .transform(new CircleCrop())
                .into(iv_music);

        intent1.putExtra("position", String.valueOf(index));
        musicControl.playNewMusic(musicVO.getMusicUrl(),musicVO.getLrcUrl()); // 假设你有这个方法

        getLoveMusicStatus();
        getCollectMusicStatus();
    }
    //未解绑则解绑
    private void unbind(boolean isUnbind) {
        if (!isUnbind) {//判断服务是否被解绑
            musicControl.pausePlay();//暂停播放音乐
            unbindService(conn);//解绑服务
        }
    }


    @Override
    public void onLyricUpdated(String currentLrcLine) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lyricsTextView.setText(currentLrcLine + "");
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        //获取歌曲名的下标字符串
        String index = intent1.getStringExtra("position");
        //将字符串转为整数
        int i = Integer.parseInt(index);
        if (v.getId() == R.id.btn_play) {
            MusicVO musicVO = musicDB.get(i);
            play.setVisibility(View.INVISIBLE);
            musicControl.play(musicVO);
            animator.start();
            tv_title.setText("正在播放");
        } else if (v.getId() == R.id.btn_pre) {
            getLoveMusicStatus();
            getCollectMusicStatus();
            if (playMode == 2) { // 随机播放模式
                int randomIndex;
                do {
                    randomIndex = random.nextInt(musicDB.size());
                } while (randomIndex == (i + change)); // 确保不是当前歌曲
                change = randomIndex - i;
                playNextSong(randomIndex);
            } else {
                if ((i + change) < 1) {
                    change = musicDB.size() - 1 - i;
                    playNextSong(i + change);
                } else {
                    change--;
                    playNextSong(i + change);
                }
            }
        } else if (v.getId() == R.id.btn_next) {
            getLoveMusicStatus();
            getCollectMusicStatus();
            if (playMode == 2) { // 随机播放模式
                int randomIndex;
                do {
                    randomIndex = random.nextInt(musicDB.size());
                } while (randomIndex == (i + change)); // 确保不是当前歌曲
                change = randomIndex - i;
                playNextSong(randomIndex);
            } else {
                if ((i + change) == musicDB.size() - 1) {
                    change = -i;
                    playNextSong(i + change);
                } else {
                    change++;
                    playNextSong(i + change);
                }
            }

        } else if (v.getId() == R.id.btn_pause) {
            pause.setVisibility(View.INVISIBLE);
            con.setVisibility(View.VISIBLE);
            musicControl.pausePlay();
            animator.pause();
            tv_title.setText("暂停播放");
        } else if (v.getId() == R.id.btn_continue_play) {
            con.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            musicControl.continuePlay();
            animator.start();
            tv_title.setText("正在播放");
        } else if (v.getId() == R.id.btn_exit) {
            unbind(isUnbind);
            isUnbind = true;
            finish();
        }else if (v.getId() == R.id.btn_play_mode) {
            switchPlayMode(); // 切换播放模式
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind(isUnbind);//解绑服务
        if (musicService != null) {
            musicService.setOnLyricUpdateListener(null);
            musicService.setOnCompletionListener(null);
        }
        //unbindService(conn); // 解绑服务
        //unregisterReceiver(notificationReceiver); // 移除广播接收器
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_PREV");
        filter.addAction("ACTION_NEXT");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ 必须指定 RECEIVER_NOT_EXPORTED
            registerReceiver(
                    notificationReceiver,
                    filter,
                    Context.RECEIVER_NOT_EXPORTED
            );
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(notificationReceiver);
    }

    // 添加广播接收器处理通知按钮点击
    /*private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "ACTION_PREV":
                        pre.performClick();
                        break;
                    case "ACTION_NEXT":
                        next.performClick();
                        break;
                }
            }
        }
    };*/
}


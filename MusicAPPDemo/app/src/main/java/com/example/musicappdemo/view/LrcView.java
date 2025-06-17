package com.example.musicappdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.musicappdemo.entity.LrcLine;

import java.util.List;


@SuppressLint("AppCompatCustomView")
public class LrcView extends TextView {
    private List<LrcLine> lyrics;
    private int currentLineIndex = -1;
    private long lastUpdateTime;

    public LrcView(Context context) {
        super(context);
    }

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 初始化歌词数据
    public void setLyrics(List<LrcLine> lyrics) {
        this.lyrics = lyrics;
    }

    // 根据当前播放时间更新歌词
    public void updateLyrics(long currentTimeSeconds) {
        if (lyrics == null || lyrics.isEmpty()) return;

        for (int i = currentLineIndex + 1; i < lyrics.size(); i++) {
            LrcLine line = lyrics.get(i);
            long lineTimestamp = line.timestamp / 1000; // 转换为秒数
            if (lineTimestamp > currentTimeSeconds) {
                currentLineIndex = i - 1;
                setText(getCurrentLyricsText());
                break;
            }
        }

        scrollUpOneLine();
    }

    // 获取当前应该显示的歌词文本
    private String getCurrentLyricsText() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = Math.max(0, currentLineIndex - 6); i <= currentLineIndex; i++) {
            if (i < lyrics.size()) {
                sb.append(lyrics.get(i).text).append("\n");
                count++;
                if (count == 7) break;
            }
        }
        return sb.toString().trim();
    }

    // 滚动一行歌词（具体逻辑需要结合实际布局高度调整）
    private void scrollUpOneLine() {
        Layout layout = getLayout();
        if (layout != null) {
            int lineHeight = layout.getLineBottom(currentLineIndex - 6) - layout.getLineTop(currentLineIndex - 6);
            scrollBy(0, -lineHeight);
        }
    }
}
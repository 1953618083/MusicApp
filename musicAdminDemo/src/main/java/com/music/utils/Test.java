package com.music.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String content = "[0:0.360,0:2.620]  但是这个微信有那个。\\n[0:2.860,0:6.300]  万一他没有，有没有手机呢。\\n[0:7.560,0:17.620]  这个合同是第二采购申请才有合同，这个是采购申请嘛，我是吧，第二采购申费合同，对呀，我就把合同腾讯这个合同里面，能不能把这个上面合同前面。\\n[0:21.880,0:26.520]  干什么啊，我填好了给你转。\\n[0:31.300,0:36.360]  其实我我还不懂，你们了解。\\n[0:37.860,0:41.560]  因为我都了我。\\n[0:46.840,0:52.600]  你们姓名，你们姓名是原来是做的人民对的。\\n";
        String contentWithoutTimestamps = content.replaceAll("\\[.*?\\]\\s*", "");
        String result = contentWithoutTimestamps.trim();
        System.out.println(result);
    }
}

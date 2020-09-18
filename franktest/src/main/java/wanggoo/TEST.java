package wanggoo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TEST {
    public static void main(String[] args) throws Exception{
        wanggooutil wgu = new wanggooutil();
        wgu.post();
        System.out.println("get_json_response_option = "+wgu.get_json_response_option());
    }


//解决方案如下：
//1、打开Android Studio
//2、打开Configure —> Edit Custom VM Options
//3、添加如下内容后重启Android Studio
//
// -Dfile.encoding=UTF-8
}

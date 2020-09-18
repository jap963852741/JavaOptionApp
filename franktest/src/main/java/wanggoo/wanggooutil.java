package wanggoo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class wanggooutil extends StockUtil{
    public wanggooutil(){
        int IntTime= Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));  //幾點幾分
        System.out.println(IntTime);
        IntTime = 1500;
        if(IntTime > 830 && IntTime < 1455){
            this.seturl("https://www.wantgoo.com/investrue/wtx&/daily-candlestick"); //早盤
        }else{
            this.seturl("https://www.wantgoo.com/investrue/wtxp&/daily-candlestick"); //晚盤
        }
    }
}

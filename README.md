# 期貨選擇權相關使用Demo
-----------------------------------------------------------------------------------------------------------------
 <Strong>此 Demo 目的僅供個人練習而非營利使用</Strong>

## 資料來源:
* [台灣期貨交易所](https://www.taifex.com.tw/cht/3/dlFutDailyMarketView)
* [Cmoney 貓頭鷹資料API](https://owl.cmoney.com.tw/Owl/tutorial/list.do?id=333619d0042711e9be97000c29e493f4)
* [玩股網](https://www.wantgoo.com/futures/wmt&)
* [奇摩股市](https://tw.stock.yahoo.com/future/)


<br></br>

## 績效回測方法寫於單元測試內
    因為資料單位為天，所以動作計算的單位也是以天做計算
    可回測台指期及選擇權歷史20年資料並顯示勝率與績效
    先使用測試內的 update 方法準備好測試資料，option_long_test()開始進行測試
    績效及勝率會打印在log console內
 ![image](https://github.com/jap963852741/JavaOptionApp/blob/master/win.png)


<br></br>
<br></br>


## 1.CmoneyAPI串接 : 可看盤後資料
    可看各種盤後資料，並選擇有資料之日期
 ![image](https://github.com/jap963852741/JavaOptionApp/blob/master/341x595_Cmoney.gif)

<br></br>
<br></br>

## 2.台指期立即資訊 : 
    使用 canvas 呈現 短期 10 日 K線圖
    可查看目前台指期資訊，並對應目前策略顯示該有動作
 ![image](https://github.com/jap963852741/JavaOptionApp/blob/master/strategy.png)

<br></br>
<br></br>

## 3.台指期選擇權立即資訊 : 
    右側有List可選擇履約期，default為最近履約期標的
 ![image](https://github.com/jap963852741/JavaOptionApp/blob/master/option.png)
 
 
 

 

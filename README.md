Twitter Streaming
=================

This project uses Twitter's streaming API to show the top 10 retweeted tweets in a rolling window of time,
where the window's start is n minutes ago (where n is defined by the user)
and the window's end is the current time.

Please see http://spark.apache.org/ for more information about Apache Spark.

A binary, with all dependencies, has been compiled for this project: https://www.dropbox.com/s/subwi35c1cjmynq/twitter-streaming-assembly-1.0.jar?dl=0

## Running from SBT

```
> sbt
> run-main MedisasJob
Usage: MedisasJob <Run Interval (seconds)> <Window Length (seconds)>
> run-main MedisasJob 5 3600
[info] Running MedisasJob
...
```

## Packaging binary

```
> sbt assembly
```

## Executing binary

```
> java -jar twitter-streaming-assembly-1.0.jar
Usage: MedisasJob <Run Interval (seconds)> <Window Length (seconds)>
> java -jar twitter-streaming-assembly-1.0.jar 5 3600
Running MedisasJob
[info] Running MedisasJob
```

## Example output

```
## Start of Window ##
(ID: 540793681385906177, Retweets in window: 84)
	Aktif Gerçek Türk Takipçi Satışı Sadecehttp://t.co/iikxMQRVpD da !HEMEN SATIN AL !https://t.co/RUGKlk0VkG

(ID: 540792196681969664, Retweets in window: 33)
	Haydi bakalım... Yürüyüş zamanı.... 😊 http://t.co/mFsmEL0u3y

(ID: 540794253136244736, Retweets in window: 26)
	#فلل الرحاب#الرياض حي #غرناطةللاستفسار الاتصال : 0555910888http://t.co/UjyW80gcuhدار زايد للتسويق العقاريeg2

(ID: 540779837816729600, Retweets in window: 24)
	これはさすがにすごすぎ(^^ゞ急いで！！http://t.co/yLiKMczgFU

(ID: 540783414979203073, Retweets in window: 24)
	Jual followers twitter-500 Followers : 10 rb-2000 Followers : 25 rbMinat? sms 087870856038bukti : cek followers saya / cek favorite :D

(ID: 540794777080705024, Retweets in window: 22)
	Transformers olsam şahine dönüşürüm o kadar şanssızım.

(ID: 540795218602127360, Retweets in window: 20)
	رتويت باقل الاسعار#أنا_أول_سعودي#كل_ما_نتمناه#العام_الهجري_الجديد#نهائي_دوري_أبطال_آسيا#اذا_ما_عندك_واسطةhttp://t.co/wTqShpo0yj

(ID: 540790552338247682, Retweets in window: 18)
	クリスマスぼっち確定のやつ↓↓ここ見てみ↓↓http://t.co/MMB9CbVd7V確定じゃなくなると思うけど簡単、簡単！！健闘を祈るｗｗ http://t.co/3yGTqTYVj4

(ID: 540777757530001408, Retweets in window: 18)
	俺を救ってくれた救世主このアプリで10分で2人と連絡先交換しちゃいました笑マジでセフレ募集多すぎっす♥♥試す価値ありっすww→http://t.co/XYVNufzoZu初めてクリスマスに予定が入るかも笑 http://t.co/q3R9J7WLz6

(ID: 540693930421936128, Retweets in window: 18)
	まじかぁ～メールするだけで月70万だよぉ～http://t.co/rJ1frkn08Uまじ神サイトｗ http://t.co/8klaOadTO8

** End of Window **
```

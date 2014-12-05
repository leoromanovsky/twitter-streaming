import org.apache.spark.streaming.{Seconds, StreamingContext}
import StreamingContext._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.twitter._
import org.apache.spark.SparkConf

object MedisasJob {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: MedisasJob <Run Interval (seconds)> <Window Length (seconds)>")
      System.exit(1)
    }

    System.setProperty("twitter4j.oauth.consumerKey", "8AqQCy7umStCyNN356v7fw")
    System.setProperty("twitter4j.oauth.consumerSecret", "vOvKV1QwuS1AeKPMIvJqErBxW7i1N12OL4UY2tNMs0c")
    System.setProperty("twitter4j.oauth.accessToken", "29463499-9Og6hxW4HqFxcQyIrAdmLpbAnrwIk290ghOE0ez5f")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "elXVYJRFmFFit3PiVTmI9eU0IvHqqD7H4yeEmClJ8c")

    val Array(runInterval, windowLength) = args.take(2)

    val sparkConf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("TwitterPopularTags")
    // Run the job every 10 seconds.
    val ssc = new StreamingContext(sparkConf, Seconds(runInterval.toInt))
    val stream = TwitterUtils.createStream(ssc, None, Seq())

    val hashTags = stream
      .flatMap(status => status.getText.split(" ")
      .filter(_.startsWith("#")))

    // Accumulate 60 seconds worth of tweets.
    val topCounts10 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(windowLength.toInt))
      .map{ case (topic, count) => (count, topic)}
      .transform(_.sortByKey(false))

    topCounts10.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("Top 10")
      topList.foreach {
        case(count, tag) => println("%s (%s tweets)".format(tag, count))
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}

import org.apache.spark.streaming.{Seconds, StreamingContext}
import StreamingContext._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.twitter._
import org.apache.spark.SparkConf
import org.apache.spark.Logging
import org.apache.log4j.{Level, Logger}

/*
Medisas Instructions:
Use Twitter's sample streaming API to show the top 10 retweeted tweets
(note the retweeted_status field) in a rolling window of time,
where the window's start is n minutes ago (where n is defined by the user)
and the window's end is the current time.

Output should continuously update and include the tweet text and
number of times retweeted in the current rolling window.
You should not use the retweet_count field,
but instead count the retweets that your program actually processes.

How to run, for example:
MedisasJob 5 3600

This accumulates one hour worth of data and runs the job every 5 seconds.
While not exact, there should not be any overlap in this case.
 */

object MedisasJob {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: MedisasJob <Run Interval (seconds)> <Window Length (seconds)>")
      System.exit(1)
    }

    // Configuration.
    Logger.getRootLogger.setLevel(Level.ERROR)
    System.setProperty("twitter4j.oauth.consumerKey", "8AqQCy7umStCyNN356v7fw")
    System.setProperty("twitter4j.oauth.consumerSecret", "vOvKV1QwuS1AeKPMIvJqErBxW7i1N12OL4UY2tNMs0c")
    System.setProperty("twitter4j.oauth.accessToken", "29463499-9Og6hxW4HqFxcQyIrAdmLpbAnrwIk290ghOE0ez5f")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "elXVYJRFmFFit3PiVTmI9eU0IvHqqD7H4yeEmClJ8c")

    val Array(runInterval, windowLength) = args.take(2)

    val sparkConf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("TwitterPopularTags")
    // Run the job every `runInterval` seconds.
    val ssc = new StreamingContext(sparkConf, Seconds(runInterval.toInt))
    val stream = TwitterUtils.createStream(ssc, None, Seq())

    /*
      1. Filter out Tweets that
        * Are not retweets. This checks whether `retweeted_status` is not null.
        * Those marked `possibly_sensitive`

      2. Map: Having verified that `retweeted_status` is present for each element in the RDD,
        the key is made to be the parent tweet (that which is being retweeted) and value is scalar 1.
        This is a common approaching for performing map-reduce counts.

      3. reduceByKeyAndWindow:
        By reducing with the parent tweet as the key, this operation produces a resultant list such as:
        ( (tweet, count), (tweet, count), ... )

      4. Map: Reverse the members of the tuple. This prepares it for sorting.

      5. Sort: Sort by key, which is now the count. False parameter signifies descending order.
     */
    val tweets = stream
      .filter { case(tweet) =>
        tweet.isRetweet() && !tweet.isPossiblySensitive()
      }
      .map { case(tweet) => (tweet.getRetweetedStatus(), 1) }
      .reduceByKeyAndWindow(_ + _, Seconds(windowLength.toInt))
      .map{ case (tweet, count) => (count, tweet)}
      .transform(_.sortByKey(false))

    // Take the top 10 elements in the RDD and produce human readable results.
    tweets.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\n\n\n\n\n\n## Start of Window ##")
      topList.foreach {
        case(count, tweet) =>
          println("(ID: %s, Retweets in window: %s)".format(tweet.getId, count))
          println("\t%s\n".format(tweet.getText().split('\n').map(_.trim.filter(_ >= ' ')).mkString))
      }
      println("** End of Window **")
    })

    ssc.start()
    ssc.awaitTermination()
  }
}

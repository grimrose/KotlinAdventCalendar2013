package sample

import twitter4j.Status
import twitter4j.Twitter
import twitter4j.Query
import java.util.Collections
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.json.JsonArray
import java.util.Date
import org.apache.commons.lang3.time.DateFormatUtils as DateFormatter
import java.util.ArrayList
import twitter4j.QueryResult


class KotlinTwitterSearch(val twitter: Twitter) {

    fun search(keyword: String): JsonObject? {
        var array = JsonArray()

        val format = { Date.() :String -> DateFormatter.format(this, "yyyy-MM-dd HH:mm:ss") as String }

        val list = searchStatus(keyword)

        for (status in list) {
            val tweet = JsonObject()
            tweet.putString("user", status.getUser()?.getScreenName())
            tweet.putString("time", status.getCreatedAt()?.format())
            tweet.putString("tweet", status.getText())

            array.addObject(tweet)
        }

        return JsonObject().putArray("tweets", array)
    }

    fun searchStatus(keyword: String):List<Status> {
        val result = twitter.search(Query(keyword))
        val asTweets = { QueryResult.(): List<Status> -> this.getTweets() ?: Collections.emptyList() }
        return result!!.asTweets()
    }

}

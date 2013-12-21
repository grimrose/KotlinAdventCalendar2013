import org.junit.Test as test
import sample.KotlinTwitterSearch
import twitter4j.Twitter
import twitter4j.Query
import twitter4j.Status
import java.util.ArrayList
import twitter4j.QueryResult
import org.mockito.Mockito
import org.mockito.Matchers
import kotlin.test.assertNotNull
import org.junit.Assert
import org.hamcrest.CoreMatchers
import twitter4j.User
import org.apache.commons.lang3.time.DateUtils
import org.vertx.java.core.json.JsonObject
import kotlin.test.assertEquals


class KotlinTwitterSearchTest {

    test fun searchTest() {
        // when
        val twitter = Mockito.mock(javaClass<Twitter>())!!
        val result = Mockito.mock(javaClass<QueryResult>())!!

        var list = ArrayList<Status>()

        val status = Mockito.mock(javaClass<Status>())!!
        val user = Mockito.mock(javaClass<User>())!!

        Mockito.`when`(user.getScreenName())?.thenReturn("user1")

        Mockito.`when`(status.getUser())?.thenReturn(user)
        Mockito.`when`(status.getText())?.thenReturn("Hello Kotlin!")

        val date = DateUtils.parseDate("2013-12-22 12:34:56", "yyyy-MM-dd HH:mm:ss")
        Mockito.`when`(status.getCreatedAt())?.thenReturn(date)

        list.add(status)

        Mockito.`when`(result.getTweets())?.thenReturn(list)
        Mockito.`when`(twitter.search(Matchers.any(javaClass<Query>())))?.thenReturn(result)

        var sut = KotlinTwitterSearch(twitter)

        // then
        val actual = sut.search("hoge")

        // expect
        assertNotNull(actual)

        val tweets = actual?.getArray("tweets")
        assertNotNull(tweets)
        assertEquals(1, tweets?.count())

        val tweet = tweets?.get<JsonObject>(0)
        assertEquals("user1", tweet?.getString("user"))
        assertEquals("2013-12-22 12:34:56", tweet?.getString("time"))
        assertEquals("Hello Kotlin!", tweet?.getString("tweet"))

    }


}


package sample

import org.vertx.java.busmods.BusModBase
import org.vertx.java.core.Handler
import org.vertx.java.core.eventbus.Message
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

class Twitter4JSearch extends BusModBase {

    void start() {
        super.start()

        logger.info 'twitter4j search start'

        def oAuthConsumerKey = getMandatoryStringConfig('OAuthConsumerKey')
        def oAuthConsumerSecret = getMandatoryStringConfig('OAuthConsumerSecret')
        def oAuthAccessToken = getMandatoryStringConfig('OAuthAccessToken')
        def oAuthAccessTokenSecret = getMandatoryStringConfig('OAuthAccessTokenSecret')

        def config = new ConfigurationBuilder()
                .setOAuthConsumerKey(oAuthConsumerKey)
                .setOAuthConsumerSecret(oAuthConsumerSecret)
                .setOAuthAccessToken(oAuthAccessToken)
                .setOAuthAccessTokenSecret(oAuthAccessTokenSecret)
                .build()
        def twitter = new TwitterFactory(config).instance

        eb.registerHandler('twitter4j.search', new Handler<Message>() {

            void handle(Message message) {
                def keyword = message.body().toString()

                logger.debug "keyword: $keyword"

                def result = new KotlinTwitterSearch(twitter).search(keyword)

                logger.debug "result: $result"

                message.reply(result)
            }

        })

    }

}

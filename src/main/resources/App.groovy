import org.vertx.groovy.core.http.RouteMatcher
import org.vertx.java.core.json.impl.Json

container.deployWorkerVerticle("groovy:sample.Twitter4JSearch", container.config)

def logger = container.logger

def matcher = new RouteMatcher()

matcher.get('/') { request ->
    request.response.sendFile('index.html')
}

vertx.createHttpServer().websocketHandler { webSocket ->
    if (webSocket.path == '/ws') {
        webSocket.dataHandler { buffer ->
            vertx.eventBus.send('twitter4j.search', buffer.toString()) { message ->
                webSocket.writeTextFrame(Json.encode(message.body()))
            }
        }
    } else {
        webSocket.reject()
    }
}.requestHandler(matcher.asClosure()).listen(8080, 'localhost')

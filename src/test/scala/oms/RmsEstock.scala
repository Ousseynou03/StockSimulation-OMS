package oms

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.concurrent.atomic.AtomicLong
import scala.language.postfixOps

class RmsEstock extends Simulation {

  val httpProtocol = http
    .baseUrl("http://10.8.9.33:9308/oms-external-mock/eStock/rest/v1/stocks/article/")
    .contentTypeHeader("application/json")

  // Initialisation de la valeur à incrémenter
  val idArticleRMS = new AtomicLong(1243565)

  val scnRmsStock = scenario("Scénario RMS Stock")
    .exec { session =>
      val idArticle = idArticleRMS.incrementAndGet()
      session.set("idArticle", idArticle)
    }
    .exec { session =>
      println("idArticle :" + session("idArticle").as[String])
      session
    }
    .exec(
      http("RMS Stock 1")
        .put("DA${idArticle}-3004/10")
        .check(status.is(200))
    )
    .exec(
      http("RMS Stock 2")
        .put("DA${idArticle}-BSG/10")
        .check(status.is(200))
    )
    .exec(
      http("RMS Stock 3")
        .put("DA${idArticle}-IDA/10")
        .check(status.is(200))
    )

  setUp(
    scnRmsStock.inject(
      atOnceUsers(1)
    ).protocols(httpProtocol)
  )

}

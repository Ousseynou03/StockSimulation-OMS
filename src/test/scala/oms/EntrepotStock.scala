package oms

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.concurrent.atomic.AtomicLong
import scala.language.postfixOps

class EntrepotStock extends Simulation{

  val httpProtocol = http
    .baseUrl("http://10.8.9.33:9308/oms-external-mock/eStock/rest/v1/stocks/article/")
    .contentTypeHeader("application/json")

  // Initialisation de la valeur à incrémenter
  val idArticleEntrepot = new AtomicLong(1243565)

  val scnEntrepotStock = scenario("Scénario Entrepot Stock")
    .exec { session =>
      val idArticle = idArticleEntrepot.incrementAndGet()
      session.set("idArticle", idArticle)
    }
    .exec { session =>
      println("idArticle :" + session("idArticle").as[String])
      session
    }
    .exec(
      http("Entrepot Stock 1")
        .put("DA${idArticle}-82010/10")
        .check(status.is(200))
    )
    .exec(
      http("Entrepot Stock 2")
        .put("DA${idArticle}-81750/10")
        .check(status.is(200))
    )
    .exec(
      http("Entrepot Stock 3")
        .put("DA${idArticle}-81751/10")
        .check(status.is(200))
    )
    .exec(
      http("Entrepot Stock 4")
        .put("DA${idArticle}-38300/10")
        .check(status.is(200))
    )
    .exec(
      http("Entrepot Stock 5")
        .put("DA${idArticle}-38301/10")
        .check(status.is(200))
    )

  setUp(
    scnEntrepotStock.inject(
      atOnceUsers(1)
    ).protocols(httpProtocol)
  )

}

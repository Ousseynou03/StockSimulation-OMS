import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class StockSimulation extends Simulation {
  val baseURL = "http://10.8.9.33:9308/oms-external-mock/eStock/rest/v1/stocks/article"

  val httpConf = http
    .baseUrl(baseURL)
    .acceptHeader("application/json")
    .doNotTrackHeader("1")


  val code: Iterator[String] = Iterator.from(1).map(i => f"${(i-1) % 50000 + 1}%05d")


  val createArticleScenario = scenario("Create Article Scenario")
    .exec { session =>
      val code_uga: String = code.next()
      session.set("code_uga", code_uga)
    }
    .exec { session =>
      println("code_uga : " + session("code_uga").as[String])
      session
    }
    .exec(
      http("Create Article")
        .put("/do10000024-#code_uga/100")
    )

  val modifyStockScenario = scenario("Modify Stock Scenario")
  .repeat(5){
    exec(
      http("Modify Stock")
        .put("/do10000024-#code_uga/10")
    )
  }


  val deleteArticleScenario = scenario("Delete Article Scenario")
    .exec(
      http("Delete Article")
        .put("/do10000024-#code_uga/0")
    )

  setUp(
    createArticleScenario.inject(atOnceUsers(1)
    ),
    modifyStockScenario.inject(
      atOnceUsers(1)
    ),
    deleteArticleScenario.inject(
      atOnceUsers(1)
    )
  ).protocols(httpConf)
}

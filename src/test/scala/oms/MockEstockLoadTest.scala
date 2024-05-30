package OmsEstockLoad
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class EstockLoadTest extends Simulation  {

  val baseURL = "http://10.8.9.33:9308/oms-external-mock/eStock/rest/v1/stocks/article"

  val httpConf = http
    .baseUrl(baseURL)
    .acceptHeader("application/json")
    .doNotTrackHeader("1")


  val code: Iterator[String] = Iterator.from(1).map(i => f"${(i - 1) % 50000 + 1}%05d")


  val createArticleScenario = scenario("Create Article Scenario")
    .exec { session =>
      val code_uga: String = code.next()
      session.set("code_uga", code_uga)
    }
    .pace(3000)
    .exec { session =>
      println("code_uga : " + session("code_uga").as[String])
      session
    }
    .pause(10 seconds)
    .pace(3000)
    .exec(
      http("Create Article")
        .put("/do10000024-${code_uga}/100")

    )
    .pause(10 seconds)
    .repeat(5) {
      exec(
        http("Modify Stock")
          .put("/do10000024-${code_uga}/10")
      )
    }
    // suppression article
    .pace(3000)
    .exec(
      http("Delete Article")
        .put("/do10000024-${code_uga}/0")
    )
    .pause(10 seconds)
  setUp(
    createArticleScenario.inject(atOnceUsers(1))
  ).protocols(httpConf)

}

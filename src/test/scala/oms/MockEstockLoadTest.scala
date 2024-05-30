package OmsEstockLoad

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.language.postfixOps

class EstockLoadTest extends Simulation {

  val baseURL = "http://10.8.9.33:9308/oms-external-mock/eStock/rest/v1/stocks/article"

  val httpConf = http
    .baseUrl(baseURL)
    .acceptHeader("application/json")
    .doNotTrackHeader("1")

  private val FichierPath: String = System.getProperty("dataDir", "data/")
  private val FichierjddCodeUga: String = "jddCodeUga.csv"

  val jddCodeUga = csv(FichierPath + FichierjddCodeUga).circular


  val createArticleScenario = scenario("Create Article Scenario")
    .feed(jddCodeUga)
    .exec(
      http("Create Article")
        .put("${code-uga}/10")
        .check(status.is(200))
    ).pause(3)

  setUp(
    createArticleScenario.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
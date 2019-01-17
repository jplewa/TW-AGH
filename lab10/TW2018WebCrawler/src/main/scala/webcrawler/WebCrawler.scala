package webcrawler

import scala.concurrent.duration._

object WebCrawler extends App {

  import java.net.URL

  import org.htmlcleaner.HtmlCleaner

  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def operate(url: String, level: Int, maxLevel: Int): Seq[String] = {
    println("[" + level + "] " + url)
    if (level < maxLevel) {
      try {
        val cleaner = new HtmlCleaner
        val rootNode = cleaner.clean(new URL(url))
        val nodes = rootNode
          .getElementsByName("a", true)
          .map(_.getAttributeByName("href")).map {
          case ent if !(ent contains "http") => url + "/" + ent
          case ent => ent
        }
        val futureUrls = nodes.map(node => Future {
          operate(node, level + 1, maxLevel)
        }).toSeq
        val future = Future.sequence(futureUrls).map(_.flatten)
        val urls = Await.result(future, 5 minutes)
        return urls :+ url
      }
      catch {
        case _: Throwable => Seq()
      }
    }
    Seq()
  }

  val levelLimit = 10
  val start = "https://theuselessweb.com/"

  operate(start, 1, levelLimit)

}


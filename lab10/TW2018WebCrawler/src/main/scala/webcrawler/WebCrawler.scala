package webcrawler

import scala.concurrent.duration._
import scala.util.Success

object WebCrawler extends App {

  import java.net.URL

  import org.htmlcleaner.HtmlCleaner

  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def operate(url: String, level: Int, maxLevel: Int): Seq[String] = {
    if (level < 4) {
      println("[" + level + "] " + url)
      try {
        val cleaner = new HtmlCleaner
        val rootNode = cleaner.clean(new URL(url))

        val nodes = rootNode
          .getElementsByName("a", true)
          .map(_.getAttributeByName("href")).map {
          case ent if !(ent contains "http") => url + "/" + ent
          case ent => ent
        }
        val futureUrls2 = nodes.map(node => Future {
          operate(node, level + 1, levelLimit)
        } ).toSeq
        val future2 = Future.sequence(futureUrls2).map(_.flatten)
        val urls2 = Await.result(future2, 5 minutes)
        return urls2
      }
      catch {
        case _: Throwable => Seq()
      }
    }
    Seq()
  }

  val levelLimit = 5
  val start = "http://www.google.pl"
  var urls = Seq(start)


  val futureUrls = urls.map(url => Future {
      operate(url, 1, levelLimit)
    } )

  val future = Future.sequence(futureUrls).map(_.flatten)
  urls = Await.result(future, 5 minutes)


}

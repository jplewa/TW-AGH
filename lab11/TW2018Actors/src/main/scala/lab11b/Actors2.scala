package lab11b

import java.util

import akka.actor._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.Random

object Player {

  case class Init(country1: ActorRef, country2: ActorRef, playerName: String)

  case object Kick

  case object Stop

}

class Player extends Actor {

  import Player._

  val actorSystem = ActorSystem()
  val scheduler: Scheduler = actorSystem.scheduler
  implicit val executor: ExecutionContextExecutor = actorSystem.dispatcher

  var name: String = _
  var myTeam: ActorRef = _
  var otherTeam: ActorRef = _

  def receive: PartialFunction[Any, Unit] = {

    case Init(country1, country2, playerName) =>
      this.myTeam = country1
      this.otherTeam = country2
      this.name = playerName
      println(s"<$name> initialized")
      this.myTeam ! Country.PlayerReady

    case Kick =>
      println(s"<$name> has the ball")
      (scheduler scheduleOnce (Random.nextInt(3) seconds)) {
        val nextMove: Int = Random.nextInt(3)
        if (nextMove == 0) {
          println(s"<$name> passes ball to team mate")
          myTeam ! Country.PassBall
        } else if (nextMove == 1) {
          println(s"<$name> passes ball to opposite team member")
          otherTeam ! Country.PassBall
        } else {
          println(s"<$name> attempts to make a shot")
          otherTeam ! Country.Shot
        }
      }

    case Stop =>
      (scheduler scheduleOnce (2 seconds)) {
        context.system.terminate
      }
  }
}


object Country {

  case class Init(game: Match, countryName: String)

  case class Start(otherCountry: ActorRef)

  case object Wait

  case class HeyOtherTeam(otherCountry: ActorRef)

  case object Ping

  case object Pong

  case object Stop

  case object Shot

  case object PassBall

  case object PlayerReady

}

class Country extends Actor {

  import Country._

  val actorSystem = ActorSystem()
  val scheduler: Scheduler = actorSystem.scheduler
  implicit val executor: ExecutionContextExecutor = actorSystem.dispatcher

  var name: String = _
  var currentGame: Match = _
  val NUMBER_OF_PLAYERS: Int = 11
  var opponent: ActorRef = _
  val players: util.LinkedList[ActorRef] = new util.LinkedList[ActorRef]

  def getRandomPlayer: ActorRef = {
    players.get(Random.nextInt(NUMBER_OF_PLAYERS))
  }

  def receive: PartialFunction[Any, Unit] = {

    case Init(game, countryName) =>
      this.name = countryName
      this.currentGame = game
      println(s"<$name> initialized")


    case Start(otherCountry) =>
      println(s"<$name> starting game")
      this.opponent = otherCountry
      for (i <- 0 until NUMBER_OF_PLAYERS) {
        context.actorOf(Props[Player], this.name + "_player" + i) ! Player.Init(self, this.opponent, this.name + "_player" + i)
      }
      self ! Wait

    case Wait =>
      if (players.size() < NUMBER_OF_PLAYERS) {
        self ! Country.Wait
      } else {
        println(s"<$name> ready")
        this.opponent ! HeyOtherTeam(self)
      }

    case HeyOtherTeam(otherCountry) =>
      this.opponent = otherCountry
      for (i <- 0 until NUMBER_OF_PLAYERS) {
        context.actorOf(Props[Player], this.name + "_player" + i) ! Player.Init(self, this.opponent, this.name + "_player" + i)
      }
      self ! Ping

    case Ping =>
      if (players.size() < NUMBER_OF_PLAYERS) {
        self ! Country.Ping
      } else {
        println(s"<$name> ready")
        this.opponent ! Pong
      }

    case Pong =>
      println("[game starting]")
      this.getRandomPlayer ! Player.Kick

    case Stop =>
      for (i <- 0 until NUMBER_OF_PLAYERS) {
        players.get(i) ! Player.Stop
      }

    case Shot =>
      val goal: Int = Random.nextInt(2)
      if (goal == 0) {
        println(s"<$name> defended the goal")
        this.getRandomPlayer ! Player.Kick
      } else {
        println(s"<$name> failed to defend the goal")
        currentGame.self ! Match.Goal
      }

    case PassBall =>
      this.getRandomPlayer ! Player.Kick

    case PlayerReady =>
      this.players.add(sender())
  }
}

object Match {

  case object Init

  case object Goal

  case object FinalScore

}

class Match extends Actor {

  import Match._

  val actorSystem = ActorSystem()
  val scheduler: Scheduler = actorSystem.scheduler
  implicit val executor: ExecutionContextExecutor = actorSystem.dispatcher

  val countryName1: String = "Spain"
  val countryName2: String = "Portugal"

  var country1: ActorRef = _
  var country2: ActorRef = _

  var score1: Int = 0
  var score2: Int = 0

  def receive: PartialFunction[Any, Unit]
  = {
    case Init =>
      this.country1 = context.actorOf(Props[Country], "country1")
      this.country2 = context.actorOf(Props[Country], "country2")

      country1 ! Country.Init(this, countryName1)
      country2 ! Country.Init(this, countryName2)

      if (Random.nextBoolean()) {
        country1 ! Country.Start(country2)
      } else {
        country2 ! Country.Start(country1)
      }

      scheduler.scheduleOnce(30 seconds, self, FinalScore)

    case Goal =>
      if (sender().compareTo(country1) == 0) {
        this.score2 += 1
        country1 ! Country.PassBall
        println(s"[current score] ($countryName1) $score1 - $score2 ($countryName2)")
      } else {
        this.score1 += 1
        println(s"[current score] ($countryName1) $score1 - $score2 ($countryName2)")
        country2 ! Country.PassBall
      }

    case FinalScore =>
      country1 ! Country.Stop
      country2 ! Country.Stop
      (scheduler scheduleOnce (5 seconds)) {
        println(s"[final score] ($countryName1) $score1 - $score2 ($countryName2)")
        actorSystem.terminate()
        // self ! PoisonPill
      }
  }
}

object Soccer extends App {
  val system = ActorSystem("Reactive1")
  val mainActor = system.actorOf(Props[Match], "match")

  mainActor ! Match.Init

  Await.result(system.whenTerminated, Duration.Inf)
}

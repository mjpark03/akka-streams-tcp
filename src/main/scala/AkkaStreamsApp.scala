/**
  * Created by Rachel on 2017. 5. 27..
  */

import akka.actor.{ActorSystem, Terminated}
import akka.stream.{ActorMaterializer}

import scala.concurrent.{ExecutionContext, Future}

trait AkkaStreamsApp extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val context: ExecutionContext = system.dispatcher

  def akkaStreamsExample: Future[_]

  def runExample: Future[Terminated] = (for {
    _ <- akkaStreamsExample
    term <- system.terminate()
  } yield term).recoverWith { case cuase: Throwable =>
      println("Exception while executing example")
      system.terminate()
  }

  sys.addShutdownHook {
    system.terminate()
  }
}

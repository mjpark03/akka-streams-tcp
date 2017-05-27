/**
  * Created by Rachel on 2017. 5. 27..
  */

import akka.stream.scaladsl._
import akka.stream.scaladsl.Tcp._
import akka.util.ByteString

import scala.concurrent.Future

object TCPCalculator extends AkkaStreamsApp {

  val Calculation = """(\d+)(?:\s*([-+*\/])\s*((?:\s[-+])?\d+)\s*)+$""".r
  val calcFlow =
    Flow[String].
      map {
        case Calculation(a, "+", b) => a.toInt + b.toInt
        case Calculation(a, "-", b) => a.toInt - b.toInt
        case Calculation(a, "*", b) => a.toInt * b.toInt
        case Calculation(a, "/", b) => a.toInt / b.toInt
        case other => 0
      }

  val connection: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind("localhost", 8888)

  override def akkaStreamsExample: Future[_] =
    connection runForeach{ connection =>
      println(s"New connection from: ${connection.remoteAddress}")

      val calc = Flow[ByteString]
        .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 256, allowTruncation = true))
        .map(_.utf8String)
        .via(calcFlow)
        .map(i => ByteString(s"$i\n"))

      connection.handleWith(calc)
    }

  runExample
}

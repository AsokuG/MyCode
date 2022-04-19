package playground

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

import scala.util.{Failure, Success}

object MaterializingStreams extends App {

  implicit val system = ActorSystem("MaterializingStreams")
  implicit val actorMaterializer = ActorMaterializer()

  val simpleGraph = Source(1 to 10).to(Sink.foreach(println))
  // val simpleMaterializedValue = simpleGraph.run()

  val source = Source(1 to 10)
  val sink = Sink.reduce[Int]((a, b) => a + b)
  //val sumFuture = source.runWith(sink)

  import system.dispatcher

  /*  sumFuture.onComplete {
      case Success(value) => println(s"The sum of all elements is $value")
      case Failure(exception) => println(s"The sum of elements not computed due to $exception")
    }*/

  val simpleSource = Source(1 to 10)
  val simpleFlow = Flow[Int].map(x => x + 1)
  val simpleSink = Sink.foreach(println)

  val graph = simpleSource.viaMat(simpleFlow)(Keep.right).toMat(simpleSink)(Keep.right)
 /* graph.run().onComplete {
    case Success(value) => println(s"stream processing finished.")
    case Failure(ex) => println(s"stream processing failed due to $ex")
  }
*/
  // Sugars
 // Source(1 to 10).runWith(Sink.reduce(_ + _)) //source.to(Sink.reduce)(Keep.right)

  //backwards

 // Sink.foreach[Int](println).runWith(Source.single(42))

 // Flow[Int].map(x => 2*x).runWith(simpleSource, simpleSink)

  /**
    * - return the last element out of a source (use Sink.last)
    * - compute the total word count out of a stream of sentences
    *   - map, fold, reduce
    */

 val f1 = Source(1 to 10).toMat(Sink.last)(Keep.right).run()
  /*f1.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/

val f2 =  Source(1 to 10).runWith(Sink.last)
 /* f2.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/

  val sentenceSource = Source(List(
    "Akka is awesome", //4 + 2 + 7 = 13
    "I love streams", // 1 + 4 + 7 =12
    "Materialized values are killing me" //12 + 6 + 3 + 7 + 2 =30
  ))

val wordCountSink =  Sink.fold[Int, String](0)((currentWords, newSentences) => currentWords + newSentences.split("").length)
 val g1 = sentenceSource.toMat(wordCountSink)(Keep.right).run()
 /* g1.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/

 val g2 = sentenceSource.runWith(wordCountSink)
  /*g2.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/
val g3 = sentenceSource.runFold(0)((currentWords, newSentences) => currentWords + newSentences.split("").length)
 /* g3.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/

 val wordCountFlow = Flow[String].fold[Int](0)((currentWords, newSentences) => currentWords + newSentences.split("").length)
val g4 = sentenceSource.viaMat(wordCountFlow)(Keep.left).toMat(Sink.head)(Keep.right).run()
  /*g4.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/

val g5 =  sentenceSource.via(wordCountFlow).toMat(Sink.head)(Keep.right).run()
  /*g5.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/

 val g6 = sentenceSource.via(wordCountFlow).runWith(Sink.head)
 /* g6.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }*/

 val g7 = wordCountFlow.runWith(sentenceSource, Sink.head)._2
  g7.onComplete{
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"$exception")
  }

}

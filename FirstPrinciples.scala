package playground

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future

object FirstPrinciples extends App {

  implicit val actorSystem = ActorSystem("firstprinciple")
  implicit val materializer = ActorMaterializer()

  //source
  val source = Source(1 to 10)

  //sink
  val sink = Sink.foreach[Int](println)

  val graph = source.to(sink)

 // graph.run()


  val flow = Flow[Int].map(x => x + 1)
  val sourceWithFlow = source.via(flow)
  // sourceWithFlow.to(sink).run()
  val flowWithSink = flow.to(sink)
  // source.to(flowWithSink).run()
  // source.via(flow).to(sink).run()

 // val illegalSource = Source.single[String](null)
  //illegalSource.to(Sink.foreach(println)).run()

  // various kinds of sources

  val finiteSource = Source.single(1)
  val anotherFiniteSource = Source(List(1, 2, 3))
  val emptySource = Source.empty[Int]
  val infiniteSource = Source(Stream.from(1))

  import scala.concurrent.ExecutionContext.Implicits.global

  val futureSource = Source.fromFuture(Future(42))


  //sinks
 val theMostBoringSink = Sink.ignore
 val foreachSink = Sink.foreach[Int](println)
val foldSink = Sink.fold[Int, Int](0)((a, b) => a+b)
 val headSink = Sink.head[Int]

  //flows
// val mapFlow =  Flow[Int].map(x => 2*x)
  val takeFlow = Flow[Int].take(5)

  //source -> flow -> flow -> .... -> sink
// val doubleFlowGraph = source.via(mapFlow).via(takeFlow).to(sink)
  //doubleFlowGraph.run()

//val mapSource =  Source(1 to 10).map(x => 2*x) //Source(1 to 10).via(Flow[Int].map(x => 2*x))
  //run streams directly
 // mapSource.runForeach(println) //mapSource.to(Sink.foreach[Int](println)).run()


  val names = List("Alice", "Bob", "Charlie", "David", "Martin", "AkkaStreams")
  val nameSource = Source(names)
  val longNameFlow = Flow[String].filter(name => name.length > 5)
  val limitFlow = Flow[String].take(2)
  val nameSink = Sink.foreach[String](println)
  nameSource.via(longNameFlow).via(limitFlow).to(nameSink).run()
 // nameSource.filter(_.length > 5).take(2).runForeach(println)
}

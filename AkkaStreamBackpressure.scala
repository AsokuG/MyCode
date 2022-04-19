package playground

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}

object AkkaStreamBackpressure extends App {

  implicit val system = ActorSystem("StreamsSystem")
  implicit val actorMaterializer = ActorMaterializer()

  val source = Source(1 to 1000)
  val flow = Flow[Int].map(_ * 10)
  val sink = Sink.foreach[Int](println)

  val graph = source.via(flow).to(sink)

 // graph.run()

val slowSink =  Sink.foreach[Int]{x =>
    Thread.sleep(1000)
    println(x)
  }

 val debuggingFlow = Flow[Int].map{x =>
  println(s"[flow] ${x}")
    x
  }

  def demoNoBackpressure()={
    source.via(debuggingFlow).to(slowSink).run()
  }
 // demoNoBackpressure

  def demoBackpressure()={
   // source.via(debuggingFlow).async.to(slowSink).run()
    source.via(debuggingFlow.buffer(10, OverflowStrategy.backpressure)).async.to(slowSink).run()
  }

  demoBackpressure
}

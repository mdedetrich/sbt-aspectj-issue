import akka.actor.ActorRef
import org.aspectj.lang.annotation.{Aspect, Before}

@Aspect
class WeaveActor {
  @Before(value = "execution(* akka.pattern.AskableActorRef$.internalAsk*(..)) && args(actorRef, message, ..)")
  def printMessage(actorRef: ActorRef, message: AnyRef): Unit = {
    val msg = message.toString
    if (!msg.startsWith("InitializeLogger")) println("Actor asked " + message)
  }

}

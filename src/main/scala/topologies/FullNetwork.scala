package topologies

import actors.BaseActor
import akka.actor.ActorSystem
import akka.actor.Props
import actors.GossipActor
import scala.util.Random

/**
 * @author user
 */
class FullNetwork extends Topology{
  val rand = new Random()
  val N = 10
  var actors = new Array[BaseActor](N)
  
  val serverSystem = ActorSystem("ServerSystem")
  for (i <- 0 to 9){
    actors(i) +=  serverSystem.actorOf(Props(new GossipActor(this, 1)), name = "Actor")
  }
  def getRandonNeighbor(actor: BaseActor): BaseActor = {
    var x = rand.nextInt(N - 1)
    if(x != ((GossipActor)actor).getID()){
      return actors(x)
    }
    return null 
  }
  
  def start() ={
    actors(1) ! "gossip"
  }
  
  
  
  
}
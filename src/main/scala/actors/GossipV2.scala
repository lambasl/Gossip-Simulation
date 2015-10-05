package actors

import main.Main
import topologies.Topology
import scala.util.Random


/**
 * @author user
 */
class GossipV2(val identity: Array[Int]) extends BaseActor{
  
    var gossipsCount = 0;
    val r = Random
      def receive = {


        case "stop" =>
      context.stop(self)

    case "gossip" => {
      gossipsCount += 1;
      if (r.nextBoolean()) {
        Main.stopRandomNode(identity)
      }
      if (gossipsCount < 10) {
        var randomNeighbor = Topology.randomNeighbor(identity)
        /*var i = 0
        while (randomNeighbor.isTerminated && i<2) {
          i += 1
          randomNeighbor = Topology.randomNeighbor(identity)
        }*/
        if(randomNeighbor.isTerminated){
          self ! "gossip"
        }
        else{
        randomNeighbor ! "gossip"
        }
      } else {
        println("Final Count for actor: " + Topology.identityString(identity) + " is:" + gossipsCount)
        println("end time:" + System.currentTimeMillis())
        println("Total number of stopped nodes:" + Main.count)
        context.system.shutdown()
      }
    }
  
}
}
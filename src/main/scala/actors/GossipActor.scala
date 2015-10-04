package actors

import topologies.Topology

/**
 * @author user
 */
class GossipActor(val identity: Array[Int]) extends BaseActor {

  var gossipsCount = 0;


  def receive = {
    case "Hello" => {
      println("GossipActor")
    }

    case "gossip" => {
      gossipsCount += 1;
      if(gossipsCount < 10){
      
      var randomNeighbor = Topology.randomNeighbor(identity)
      randomNeighbor ! "gossip"
      }
      else{
        println("Final Count for actor: " + Topology.identityString(identity)  + " is:"+ gossipsCount)
        println("end time:" + System.currentTimeMillis())
        context.system.shutdown()
      }
    }




  }

}

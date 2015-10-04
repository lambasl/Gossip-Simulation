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
      //Topology.randomNeighbor(identity) ! "gossip"
      //Topology.randomNeighbor(identity) ! "gossip"
      }
      else{
        println("Final Count for actor: " + Topology.printIdentity(identity)  + " is:"+ gossipsCount)
        context.stop(self)
        //context.system.shutdown()
      }
    }




  }

}

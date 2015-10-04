package actors

import topologies.Topology

/**
 * @author user
 */
class GossipActor(val topology: String,val identity: Array[Int], numberOfNodes: Int) extends BaseActor {

  var gossipsCount = 0;


  def receive = {
    case "Hello" => {
      println("GossipActor")
    }

    case "gossip" => {
      //sprintln("recieved message in Actor:" + id)
      if(gossipsCount < 10){
      gossipsCount += 1;
      var randomNeighbor = Topology.randomNeighbor(, identity, numberOfNodes)
      randomNeighbor ! "gossip"
      }
      else{
        println("Final Count: "+ gossipsCount + "ID: " + identity(0))
      }
    }




  }

}

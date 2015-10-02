package actors

import topologies.Topology

/**
 * @author user
 */
class GossipActor(val topology: Topology,val id: Int) extends BaseActor {
  
  var gossipsCount = 0;  
  
  def getID(): Int ={
    return id
  }
  def receive = {
    case "Hello" => {
      println("GossipActor")  
    }
    
    case "gossip" => {
      println("recieved message in Actor:" + id)
      if(gossipsCount < 10){
      gossipsCount += 1;
      //var randomNeighbor = topology.getRandonNeighbor(this)
      //randomNeighbor ! "gossip"
      }
    }
    
    
    
    
  }
  
}
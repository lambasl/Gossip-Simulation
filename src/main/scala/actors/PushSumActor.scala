package actors

import topologies.Topology
import common.Messages

/**
 * @author user
 */
class PushSumActor(val identity: Array[Int]) extends BaseActor{
  
  var s: Double = 0
  var w: Double = 0
  var r0 : Double = 0
  var r1 : Double = 0
  var r2 : Double = 0
  var r3 : Double = 0
  var r4 : Double = 0
  var convergernceCount = 0
  
  def receive = {
    case "Hello" =>{
      println("PushSumActor")
    }
    
    case Messages.push(s1: Double, w1: Double) =>{
      convergernceCount += 1;
      var news = s + s1
      var neww = w + w1
      r0 = r1
      r1 = r2
      r2 = r3
      r3 = r4
      r4 = ratio(news, neww)
      s = news/2
      w = neww/2
      convergernceCount += 1
      if((r4 - r0) < Math.pow(10, -10)){
        println("Actor" + Topology.identityString(identity) + ":" + r4 + ", count:" + convergernceCount + ", Final value:s=" + s*2 + ",w=" + w*2 + ", avg=" + ratio(s*2, w*2))
        println("end time:" + System.currentTimeMillis())

        context.system.shutdown()
      }
      else{
        Topology.randomNeighbor(identity) ! Messages.push(news/2, neww/2)

      }
    }
       
      
    
  }
  
  def ratio(s1: Double, w1: Double): Double ={
    return s1/w1;
  }
}
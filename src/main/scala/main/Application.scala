

package main

import topologies._
import actors.GossipActor
import actors.PushSumActor
import common.Messages

/* @author user
 */
object Application extends App {
  
  if(args.length != 3){
    println("Invalid arguments. Enter sbt \"run <numberofNodes> <topology[fullnetwork|line|grid|imperfectgrid]> <protocol[gossip|pushsum]>")
    System.exit(-1)
  }
  var numberOfNodes: Int = args(0).toInt
  val topology: String = args(1).toString
  val gossipOrPushsum: String = args(2).toString
  
  if (!(topology == "fullnetwork" || topology == "line" || topology == "grid" || topology == "imperfectgrid")) {
    println("Invalid topology.")
    System.exit(-1)
  }
  
  if(topology.contains("grid") && numberOfNodes < 4){
    println("For grid based topology, number of nodes should be atleast 4")
    System.exit(-1)
  }
  else if (numberOfNodes < 2) {
    println("You should enter atleast 2 nodes. Exiting...")
    System.exit(-1)
  }

  if (!(gossipOrPushsum == "gossip" || gossipOrPushsum == "pushsum")) {
    println("Invalid protocol.")
    System.exit(-1)

  }
  println("initialising topology....")
  Topology.initialiseTopology(numberOfNodes, topology, gossipOrPushsum)
  println("Starting protocol....")
  println("starting at :" + System.currentTimeMillis())
  if (gossipOrPushsum == "gossip") {

    Topology.randomNeighbor(Array.fill[Int](3)(0)) ! "gossip"
  } else {
    Topology.randomNeighbor(Array.fill[Int](3)(0)) ! Messages.push(5000.0, 1.0)

  }
}

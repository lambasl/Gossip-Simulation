

package main

import topologies._
import actors.GossipActor
import actors.PushSumActor
import common.Messages

/* @author user
 */
object Test extends App{
    var numberOfNodes: Int = args(0).toInt
    val topology: String = args(1).toString
    val gossipOrPushsum: String = args(2).toString
    Topology.initialiseTopology(numberOfNodes, topology, gossipOrPushsum)
    Topology.randomNeighbor(Array.fill[Int](3)(0)) ! Messages.push(1.0, 1)

}

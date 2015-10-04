

package main

import topologies._

/* @author user
 */
object Test extends App{
    var numberOfNodes: Int = args(0).toInt
    val topology: String = args(1).toString
    val gossipOrPushsum: String = args(2).toString
    val networkStructure: Array[BaseActor] = Topology.networkGenerator(numberOfNodes, topology, gossipOrPushsum)
    networkStructure(1,0,0) ! "gossip"

}

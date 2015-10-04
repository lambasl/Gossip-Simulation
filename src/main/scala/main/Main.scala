package main

import topologies.Topology

/**
 * @author user
 */
object Main {
  def main(args: Array[String]): Unit = {
    var numberOfNodes: Int = args(0).toInt
    val topology: String = args(1).toString
    val gossipOrPushsum: String = args(2).toString
    Topology.initialiseTopology(numberOfNodes, topology, gossipOrPushsum)
    Topology.randomNeighbor(Array.fill[Int](3)(0)) ! "gossip"
  }
}
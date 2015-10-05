package main

import topologies.Topology
import scala.util.Random

/**
 * @author user
 */
object Main {

  var count = 0;
  var ratioFailedNodes = 0.20
  val totalNumNodes = Topology.N
  val numFailedNodes = (totalNumNodes * ratioFailedNodes).intValue()
  val r = new Random()

  def incrementCount(): Unit = {
    count = count + 1
  }

  def stopRandomNode(identity: Array[Int]) = {
    if (count < numFailedNodes) {
      var randomNeighbor = Topology.randomNeighbor(identity)
      /*while (randomNeighbor.isTerminated) {
        randomNeighbor = Topology.randomNeighbor(identity)
      }*/
      if (!randomNeighbor.isTerminated) {
        randomNeighbor ! "stop"
      }
      Thread.sleep(10)
      //println(Topology.identityString(identity) + " stopped?" + randomNeighbor.isTerminated)
      count += 1
    }
  }
}


package main

import topologies.FullNetwork
 
/* @author user
 */
object Test extends App{
  def main(args: Array[String]): Unit = {
    val network = new FullNetwork()
    network.start()
    
  }
  
}
package topologies

import actors.BaseActor
import akka.actor._
import scala.math
import scala.util.Random
import actors.GossipActor
import actors.PushSumActor
import actors.GossipActor
import actors.GossipV2
import actors.GossipActor

/**
 * @SrinivasNarne @SatbeerLamba
 */

object Topology {
	var network: Array[Array[Array[ActorRef]]] =  null
  var N : Int = 0
  var topologyType: String = ""

  def initialiseTopology(numberOfNodes: Int, topology: String, gossipOrPushsum: String) = {
   N = numberOfNodes
   topologyType = topology
   network = networkGenerator(numberOfNodes, topology, gossipOrPushsum)
   println("length of network" + network.length)
  }
	val rand = new Random()
	def networkGenerator(numberOfNodes: Int, topology: String, gossipOrPushsum: String): Array[Array[Array[ActorRef]]] = {
		var xDimension = 1
		var yDimension = 1
		var zDimension = 1

		if (topology == "grid" || topology == "imperfectgrid") {
			xDimension = (math.round(math.cbrt(numberOfNodes))).toInt
			yDimension = xDimension
			zDimension = xDimension
		}
		else {
			xDimension = numberOfNodes
		}

		val networkNodes =  Array.ofDim[ActorRef](xDimension.toInt, yDimension.toInt, zDimension.toInt)
		val networkSystem = ActorSystem("networkSystem")

		for (x <- 0 to xDimension - 1) {

			for (y <- 0 to yDimension - 1) {

				for (z <- 0 to zDimension - 1) {
					var identity = new Array[Int](3)
					identity(0) = x
					identity(1) = y
					identity(2) = z
					if (gossipOrPushsum == "gossip") {
						networkNodes(x)(y)(z) =  networkSystem.actorOf(Props(new GossipActor(identity)), name = x.toString + "," + y.toString + "," + z.toString)
					}
					else {
						networkNodes(x)(y)(z) = networkSystem.actorOf(Props(new PushSumActor(identity)), name = x.toString + "," + y.toString + "," + z.toString)
					}
				}
			}
		}
		return networkNodes
	}

  def identityString (identity: Array[Int]): String = {
    return "Identity:x=" + identity(0) + ",y=" + identity(1) + ",z=" + identity(2)
  }
	def randomNeighbor (identity: Array[Int]): ActorRef = {
		val neighbor = randomNeighborSelector(identity)
    return network(neighbor(0))(neighbor(1))(neighbor(2))

	}
	def randomNeighborSelector (identity: Array[Int]): Array[Int] = {
		var gridDimension = (math.round(math.cbrt(N))).toInt
    var returnVal : Array[Int]= null
		def randomDimension(i: Int): Int = {
			var coordinate = rand.nextInt(2)
			var y: Int = identity(i)
			if (coordinate == 0) {
				y = y-1
			}
			else {
				y = y+1
			}
			return y
		}

		if (topologyType == "fullnetwork") {
			var x = rand.nextInt(N)
			while (x == identity(0)) {
				 x = rand.nextInt(N)
			}
			identity(0) = x
			identity(1) = 0
			identity(2) = 0
			returnVal =  identity
		}

		else if (topologyType == "line") {
			if (identity(0) == 0) {
				identity(0) = 1
			}
			else if (identity(0) == N - 1) {
				identity(0) == identity(0) - 1
			}
			else {
				var x = rand.nextInt(2)
				if (x == 0) {
					identity(0) = identity(0) - 1
				}
				else identity(0) = identity(0) + 1
			}
			returnVal = identity
		}

		else if (topologyType == "grid") {
			var randomOption = rand.nextInt(3)
			var x = randomDimension(randomOption)
			while ((x < 0) || (x >= gridDimension)) {
				x = randomDimension(randomOption)
			}
			identity(randomOption) = x
			returnVal =  identity
		}

		else if(topologyType == "imperfectgrid") {
			var randomOption: Int = rand.nextInt(4)
			if (randomOption < 3) {
				var x: Int = (randomDimension(randomOption)).toInt
				while ((x < 0) || (x >= gridDimension)) {
					x = (randomDimension(randomOption)).toInt
				}
				identity(randomOption) = x
        returnVal =  identity
			}
			else {
				var x = rand.nextInt(gridDimension)
				for (i <- 0 to 2) {
					x = rand.nextInt(gridDimension)
					identity(i) = x
				}
			returnVal = identity
			}
		}
    if(returnVal == null){
      println("WARNING!!!!!! : neighbour's identity is null")
    }else{
      //print("found neighbor = ")
      //printIdentity(returnVal)
    }
    return returnVal
	}


}

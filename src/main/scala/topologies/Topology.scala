package topologies

import actors.BaseActor
import akka.actor._
import scala.math
import scala.util.Random

/**
 * @SrinivasNarne @SatbeerLamba
 */

object Topology {
	val network = null
	val rand = new Random()
	def networkGenerator(numberOfNodes: Int, topology: String, gossipOrPushsum: String): Array [ActorRef] = {
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

		val networkNodes = new Array.ofDim[ActorRef](xDimension.toInt, yDimension.toInt, zDimension.toInt)
		val networkSystem = ActorSystem("networkSystem")

		for (x <- 0 to xDimension - 1) {

			for (y <- 0 to yDimension - 1) {

				for (z <- 0 to zDimension - 1) {
					var identity = new Array[Int](3)
					identity(0) = x
					identity(1) = y
					identity(2) = z

					if (gossipOrPushsum == "gossip") {
						networkNodes(x)(y)(z) =  networkSystem.actorOf(Props(new GossipActor(topology, identity, numberOfNodes)), name = x.toString + "," + y.toString + "," + z.toString)
					}
					else {
						networkNodes(x)(y)(z) = networkSystem.actorOf(Props(new PushSumActor(this,identity)), name = x.toString + "," + y.toString + "," + z.toString)
					}
				}
			}
		}
		network = networkNodes
		return networkNodes
	}
	def randomNeighbor (numberOfNodes: Int, topology: String, gossipOrPushsum: String): ActorRef = {
		return network(neighbor(0))(neighbor(1))(neighbor(2))

	}
	def randomNeighborSelector (topology: String, identity: Array[Int], numberOfNodes: Int): Array[Int] = {
		var gridDimension = (math.round(math.cbrt(numberOfNodes))).toInt
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

		if (topology == "fullnetwork") {
			var x = rand.nextInt(numberOfNodes)
			while (x == identity(0)) {
				 x = rand.nextInt(numberOfNodes)
			}
			identity(0) = x
			identity(1) = 0
			identity(2) = 0
			return identity
		}

		else if (topology == "line") {
			if (identity(0) == 0) {
				identity(0) = 1
			}
			else if (identity(0) == numberOfNodes - 1) {
				identity(0) == identity(0) - 1
			}
			else {
				var x = rand.nextInt(2)
				if (x == 0) {
					identity(0) = identity(0) - 1
				}
				else identity(0) = identity(0) + 1
			}
			return identity
		}

		else if (topology == "grid") {
			var randomOption = rand.nextInt(3)
			var x = randomDimension(randomOption)
			while ((x < 0) || (x > gridDimension)) {
				x = randomDimension(randomOption)
			}
			identity(randomOption) = x
			return identity
		}

		else if (topology == "imperfectgrid") {
			var randomOption: Int = rand.nextInt(4)
			if (randomOption < 3) {
				var x: Int = (randomDimension(randomOption)).toInt
				while ((x < 0) || (x > gridDimension)) {
					x = (randomDimension(randomOption)).toInt
				}
				identity(randomOption) = x
			}
			else {
				var x = rand.nextInt(gridDimension)
				for (i <- 0 to 2) {
					x = rand.nextInt(gridDimension)
					identity(i) = x
				}
			return identity
			}
		}
	}


}

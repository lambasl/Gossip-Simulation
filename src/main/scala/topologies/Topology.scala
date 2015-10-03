package topologies

import actors.BaseActor
import akka.actor._
import scala.math

/**
 * @SrinivasNarne @SatbeerLamba
 */

case class Rumour(topology: Int, numberOfNodes: Int, gossipOrPushsum: String) extends Messages
class Topology() extends Actor {

	val rand = new Random()
	def networkGenerator(numberOfNodes: Int, topology: String, gossipOrPushsum: String): Array [ActorRef] = {
		var xDimension = 1
		var yDimension = 1
		var zDimension = 1

		if (topology = "grid" || "imperfectgrid") {
			xDimension = math.round(math.cbrt(numberOfNodes))
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
						networkNodes(x)(y)(z) =  networkSystem.actorOf(Props(new GossipActor(this,identity)), name = x.toString + "," + y.toString + "," + z.toString)
					}
					else {
						networkNodes(x)(y)(z) = networkSystem.actorOf(Props(new PushSumActor(this,identity)), name = x.toString + "," + y.toString + "," + z.toString)
					}
				}
			}
		}
		return networkNodes
	}

	def randomNeighborSelector (topology: String, identity: Array[Int], numberOfNodes: Int): Array[Int] = {
		var gridDimension = math.round(math.cbrt(numberOfNodes))
		def randomDimension(i: Int): Int = {
			var coordinate = rand.nextInt(2)
			var y = identity(i)
			if (coordinate == 0) {
				y--
			}
			else {
				y++
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
			var randomOption = rand.nextInt(4)
			if (randomOption < 3) {
				var x = randomDimension(randomOption)
				while ((x < 0) || (x > gridDimension)) {
					x = randomDimension(randomOption)
				}
				identity(randomOption) = x
			}
			else {
				var x = rand.nextInt(gridDimension)
				for (i = 0; i < 3; i++) {
					x = rand.nextInt(gridDimension)
					identity(i) = x
				}
			return identity
			}
		}
	}

	def receive = {

		case Rumour(topology: Int, numberOfNodes: Int, gossipOrPushsum: String) => {
			val network = networkGenerator(numberOfNodes, topology, gossipOrPushsum)
			network(1)(0)(0) ! "gossip"

		}
	}

}

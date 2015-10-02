package topologies

import actors.BaseActor

/**
 * @author user
 */
trait Topology {
  def getRandonNeighbor(actor: BaseActor): BaseActor
}
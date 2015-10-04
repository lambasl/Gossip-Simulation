package actors

/**
 * @author user
 */
class PushSumActor(val identity: Array[Int]) extends BaseActor{
  
  def receive = {
    case "Hello" =>{
      println("PushSumActor")
    }
  }
}
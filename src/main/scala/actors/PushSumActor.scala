package actors

/**
 * @author user
 */
class PushSumActor extends BaseActor{
  
  def receive = {
    case "Hello" =>{
      println("PushSumActor")
    }
  }
}
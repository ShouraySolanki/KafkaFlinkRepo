
class Total{
  private var a: Int = 0
  private var b: Int = 0
  private var sum: Int = a + b

  def getA(): Int ={
    a
  }

  def setA(a : Int){
    this.a = a
  }


  def getB(): Int ={
    b
  }

  def setB(b : Int){
    this.b = b
  }
  def getSum(): Int ={
    sum
  }

  def setSum(sum : Int){
    this.sum = sum
  }


  override def toString: String = "Total{" + "a=" + a + ", b=" + b + ", sum=" + sum + '}'
}



object TestScala  {
  def main(args: Array[String]) = {
   val list = List(1, 2, 3, 4, 5)
    println(list.foldLeft(List[Int]())((a, b) =>  b :: a))

    println(balancing("{[()]}"))
    println(balancing("{[(]]}"))
  }

  def balancing(str: String): Boolean = {

    def go(index: Int, list: List[Char]): Boolean = {
      if(index >= str.length){
        if(list.isEmpty) true else false
      }else{
        str(index) match {
          case '{' | '[' | '(' => go(index+1, str(index) :: list)

          case '}'  => list match {
            case Nil => false
            case h :: tail if(h=='{') => go(index+1, tail)
            case _ => go(index+1, list)
          }

          case ']' => list match {
            case Nil => false
            case h :: tail if(h=='[') => go(index+1, tail)
            case _ => go(index+1, list)
          }

          case ')' => list match {
            case Nil => false
            case h :: tail if(h=='(') => go(index+1, tail)
            case _ => go(index+1, list)
          }
        }
      }
    }

go(0, Nil)
  }
}

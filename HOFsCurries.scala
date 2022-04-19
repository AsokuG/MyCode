

object HOFsCurries extends App {

  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null

  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if (n <= 0) x
    else nTimes(f, n - 1, f(x))
    
    val plusOne= (x: Int) => x+1
    
    println(nTimes(plusOne, 10, 1))
  
  /*
   * n = 10 x = 1
   *   n = 9 x=2
   * 
   * */
  
  def nTimesBetter(f: Int => Int, n: Int): Int => Int =
    if(n <= 0) (x: Int) =>x
    else (x: Int) => nTimesBetter(f, n-1)(f(x))
    
   val pluse10 = nTimesBetter(plusOne, 10)
   
   println(pluse10(1))
   
    val superAdder: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
    
    val add3 = superAdder(3)
    
    println(add3(10))
    println(superAdder(10)(30))
   
   def mul(x: Int)=(y: Int)=> x*y
   
   def minus(x: Int)(y: Int) =x-y
   
   minus(5)(_)
   
   mul(4)
   
   val sum2 = (a: Int, b: Int) => a+b
   
   sum2(1, _: Int)
   
   def sum(a: Int, b: Int, c: Int) =a+b+c
   
   
  val a = (sum _).curried
  
  val re = a(4)
  
  val res = re(5)
  
  println(res(1))
  
  def add1(x: Int, y: Int) = x+y
  
  def add2(x: Int)(y: Int) = x+y
  
  def add23(x: Int) = (y: Int) => x+y
  
  val a1 = (add1 _).curried
  
  add2(3)(_)
  
  add23(3)
  
  def numAdder(num1: Int)(num2: Int): Int = 
    num1 + num2
    
  val p =  numAdder _
  p(4)
  
  println(sys.props("java.version"))
  
   def greeting() = (name: String) => {
    "hello" + " " + name
  }
   
   val greet = greeting()
   
   println(greet("Ashok"))
  
}
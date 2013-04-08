package scalaturkiye
import scala.util.continuations._
import scala.annotation.tailrec
import scala.concurrent._
import ExecutionContext.Implicits.global

object Cont extends App{

  @tailrec
  def fact0(n: Int)(cont: Int => Int): Int = {
    if (n <= 0)
      cont(1)
    else
      fact0(n-1){x => cont(n * x) }
  }

  @tailrec
  def fact2(n: Int)(cont: Int => Int): Int = {
    if (n <= 0)
      cont(1)
    else
      fact2(n-1){cont compose {x => n * x} }
  }
  
  @tailrec
  def fact3(n: Int)(cont: Int => Int): Int = {
    if (n <= 0)
      cont(1)
    else
      fact3(n-1)(cont compose (n * _) )
  }

  val k = reset{
    val x = shift(fact0(5))
    x 
  }

//  @tailrec
  def fibo(n: Int, cont: Int => Int): Int = {
    if(n <= 2)
      cont(1)
    else
      fibo(n-1, { x=> fibo(n-2, y => cont(x + y)) })
  }

  def fibo2(n: Int)(cont: Int => Int): Int = {
    if(n <= 2)
      cont(1)
    else
      fibo2(n-1)(cont compose ( _ + fibo2(n-2)(x => x) ))
  }

  def fiboPar(x: Int) (callback: Int => Unit ): Unit = {
    def fib(a:Int):Int = if (a <= 2) 1 else fib(a-2) + fib(a -1)
    val res = future(fib(x))
    res.onSuccess{case t => callback(t)}
  }
  
  reset{
    val a:Int = shift(fiboPar(6))
    println("result is " + a)
  }
  
  def transFormed = { 
    x0:Int => {
      x1:Int => {
	x2:Int => {
	  callback:(Int => Int) => {
	    (x0*(x1*(callback(x2))))
	}}}}}

 }

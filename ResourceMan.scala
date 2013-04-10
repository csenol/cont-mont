package scalaturkiye
import scala.util.continuations._
import scala.concurrent._
import java.io.File
import java.io.FileReader
import java.io.BufferedReader

object ResMan extends App {
  
  def withFile(path:String)(callback: BufferedReader => Unit):Unit = {
    val f = new File(path)
    val fr = new FileReader(f)
    val br = new BufferedReader(fr)
    callback(br)
    br.close
    fr.close
    println("file closed")
  }

/*  
  withFile("/tmp/hede"){ br =>
    val lines1 = br.readLine
    withFile("/tmp/hede2"){ br2 =>
      val lines2 = br2.readLine
      println(lines1 + " " + lines2) 			   
    }			
  }
  */
/*
  reset{
    val file1 = shift(withFile("/tmp/hede"))
    val file2 = shift(withFile("/tmp/hede2"))
    val lines1 = file1.readLine
    val lines2 = file2.readLine
    println(lines1 + " " + lines2)
  }
*/
  def withFile2(path:String):BufferedReader@cps[Unit] = {
    val f = new File(path)
    val fr = new FileReader(f)
    val br = new BufferedReader(fr)
    shift{k:(BufferedReader => Unit) => 
      k(br)
      br.close
      fr.close 
      println("file closed")	  
      }
    br
  }

  reset{
    val file1 = withFile2("/tmp/hede")
    val file2 = withFile2("/tmp/hede2")
    val lines1 = file1.readLine
    val lines2 = file2.readLine
    println(lines1 + " " + lines2)
  }

}

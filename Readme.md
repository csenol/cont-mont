# Continuation Nedir. Ne işe yarar

##  Dosya Açma-Kapamayı Otomatize Edelim

* Diyelim ki Scala da `BufferedReader` oluşturup kullandıktan sonra `close` işini otomatikleştirmek istiyorsunuz
* Higher-order-function lar ile bunu çözebiliriz. 
* Yani `callback`

## Örnek 

```scala
  def withFile(path:String)(callback: BufferedReader => Unit):Unit = {
    val f = new File(path)
    val fr = new FileReader(f)
    val br = new BufferedReader(fr)
    callback(br)
    br.close
    fr.close
    println("file closed")
  }
```

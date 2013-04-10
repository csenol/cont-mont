# Continuation Nedir? Ne işe yarar?

##  Dosya Açma-Kapamayı Otomatize Edelim

* Diyelim ki Scala da `BufferedReader` oluşturup kullandıktan sonra `close` işini otomatikleştirmek istiyorsunuz
* Higher-order-function lar ile bunu çözebiliriz. 
* Yani `callback`

## Dandik implementasyon

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

## Kullanımı
```scala  
  withFile("/tmp/hede"){ br =>
    val lines1 = br.readLine
    withFile("/tmp/hede2"){ br2 =>
      val lines2 = br2.readLine
      println(lines1 + " " + lines2) 			   
    }			
  }
```

## Sorunlar
* Nested callback.
* Çok fazla iç içe yapı olunca okumak zorlaşıyor
* State paylaşacakları için anonim fonksiyon yazıyoruz
* javascript, node.js :(

## Hayallerde yaşıyor bazı geliştiriciler!!!
* Keşke şöyle yazabilseydik

```scala
    val file1 = withFile("/tmp/hede")
    val file2 = withFile("/tmp/hede2")
    val lines1 = file1.readLine
    val lines2 = file2.readLine
    println(lines1 + " " + lines2)
```


## Shift/Reset ile rüyalar gerçek oluyor.

```scala
  reset{
    val file1 = shift(withFile("/tmp/hede"))
    val file2 = shift(withFile("/tmp/hede2"))
    val lines1 = file1.readLine
    val lines2 = file2.readLine
    println(lines1 + " " + lines2)
  }
```

## Peki ama nasıl ???

* Continuation Passing Style
* Dilde yazdığımız tüm fonksiyonlar callback alsa.
* Aslında javascript/ node.js tayfasının işi.
* Hesaplamanın devamını callback ile yapsak.
* Hesaplama bitince callback çalışsa.

## CPS - Factorial örneği
```scala
  @tailrec
  def fact0(n: Int)(cont: Int => Int): Int = {
    if (n <= 0)
      cont(1)
    else
      fact0(n-1){x => cont(n * x) }
  }
```

## Şuna çevriliyor gibi
* `fact0(3)`
* Bundan emin değilim!!!

```scala
  def transFormed = { 
    x0:Int => {
      x1:Int => {
        x2:Int => {
          callback:(Int => Int) => {
	    (x0*(x1*(callback(x2))))
	}}}}}
	transFormed(3)(2)(1)(x=>x)	
```

## Ozunde olay
* Stack kullanmaya gerek yok. Olaylar tailde. Function composition ve currying
* Daha guzel yazimi

```scala
  @tailrec
  def fact3(n: Int)(cont: Int => Int): Int = {
    if (n <= 0)
      cont(1)
    else
      fact3(n-1)(cont compose (n * _) )
  }
```

## Ee bu transform ne işimize yarıyor

* shift ettiğimiz curried function CPS transform oluyor
* Reset blogunun sonuna kadar
* Yani shift sonrasinda kalan Computation i yakalayip
* Bir CPS fonksiyona çeviriyor
* Shift ten donen deger ise Continuation in aldigi parametre

## Ornek
* Shift in sonrasi identity function
* Reset block'undan donen deger ise fact3 un cevabi.

```scala
  val k = reset{
    val x = shift(fact3(5))
    x 
  }
```
* `fact3` callback verelim diye kasmadik.

## Client in shift ten haberi olmasin
* Hem callback vermeyecegiz
* Hem de client shift ile ilgilenmeyecek

```scala
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
```

## Nasil cagiriyoruz

* Ayni sekilde

```scala
  reset{
    val file1 = withFile2("/tmp/hede")
    val file2 = withFile2("/tmp/hede2")
    val lines1 = file1.readLine
    val lines2 = file2.readLine
    println(lines1 + " " + lines2)
  }
```

## CPS annotation
* Fonksiyonumuzun icinde reset edilmemiş shift block u varsa
* CPS annotation ile işaretliyoruz
* Dönüş tipi contiunation in aldigi tip
* annotated tip te continuation in dondurdugu tip.




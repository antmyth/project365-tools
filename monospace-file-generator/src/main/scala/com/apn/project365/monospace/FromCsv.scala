package com.apn.project365.monospace

import java.io.{File, PrintWriter}

import com.typesafe.config.ConfigFactory
import purecsv.unsafe.CSVReader

import scala.io.Source

object FromCsv {
  def  main(args: Array[String]) {
    println("reading the file::"+args.head)
    Source.fromFile(args.head ).getLines()
      .drop(2)
      .filter(s => s.startsWith("pending") || s.startsWith("prep"))
      .foreach{ l =>
      val entries = CSVReader[Project365Entry].readCSVFromString(l).foreach { entry =>
        val content = MonospaceFileFormat.monofile(entry)
        println(s"Start generating ${Config.baseDir}project365_year1-day-${entry.fn}.txt")
//        println(content)
        val f = new File(s"${Config.baseDir}project365_year1-day-${entry.fn}.txt")
        f.createNewFile()
        f.setWritable(true)
        val writer = new PrintWriter(f)
        writer.write(content)
        writer.close()
        println(s"finished generating ${Config.baseDir}project365_year1-day-${entry.fn}.txt")
      }
    }
  }
}

case class Project365Entry(status:String, number: Int, fn:String, date:String, month: String,
                           title:String, location:String, hasImage:String, camera:String,
                           lens:String, filter1:String, filter2:String, accessory:String,
                           header:String, sublink:String, link:String)

object MonospaceFileFormat{
  import Config.baseTag
  def monofile(entry: Project365Entry) =
    s"""# ${entry.header}
       |
       |
       |-- Gear
       |${entry.camera}
       |${entry.lens}
       |${render(entry.filter1, entry.filter2)}${render(entry.accessory)}
       |-- Post
       |Lightroom :
       |Photoshop :
       |
       |
       |$baseTag${entry.status} #${entry.status}
       |""".stripMargin

  def render(f1:String, f2:String):String =
    if(f1.nonEmpty) s"Filters: $f1 + $f2\n" else ""
  def render(a:String):String =
    if(a.nonEmpty) s"$a\n" else ""
}

object Config{
  val config = ConfigFactory.load()
  val baseTag = config.getString("base.tag")
  val baseDir = config.getString("file.destination.dir")
}
package com.apn.project365.monospace


object FromCsv {
  def  main(args: Array[String]) {
    println("running")
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
       |-- Gear
       |${entry.camera}
       |${entry.lens}
       |${render(entry.filter1, entry.filter2)}${render(entry.accessory)}
       |-- Post
       |Lightroom :
       |Photoshop:
       |
       |
       |$baseTag${entry.status} #${entry.status}""".stripMargin

  def render(f1:String, f2:String):String =
    if(f1.nonEmpty) s"Filters: $f1 + $f2\n" else ""
  def render(a:String):String =
    if(a.nonEmpty) s"$a\n" else ""
}

object Config{
  val baseTag = "#apnphotographia#blog#project365_year1#"
  val baseDir = "/Users/antonio.nascimento/Dropbox/Apps/Monospace"
}
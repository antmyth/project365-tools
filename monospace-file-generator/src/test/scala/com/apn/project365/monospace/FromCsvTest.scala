package com.apn.project365.monospace

import org.scalatest.{FunSuite, ShouldMatchers}
import purecsv.unsafe._

class FromCsvTest extends FunSuite with ShouldMatchers{

  test("reading csv text") {
    case class Event(ts: Long, msg: String)
    val records = CSVReader[Event].readCSVFromString("1,foo\n2,bar")

    records.head.ts should be(1)
    records.head.msg should be("foo")
  }

  test("create project365 class from the 'real' csv line") {
    val csvLine = "pending,1,001,2016/06/01,Jun,\"Turnstiles at Chicago's \"\"L\"\"\",\"Chicago, IL, USA\",,Fujifilm X-T1,FUJINON XF18-135mm F3.5-5.6 R LM OIS WR,,,,\"#project365_year1 [day 001] Turnstiles at Chicago's \"\"L\"\"\",2016/6-1/project365_year1-day-001,http://www.apn-photographia.com/blog/2016/6-1/project365_year1-day-001"

    val records = CSVReader[Project365Entry].readCSVFromString(csvLine)

    records.size should be(1)
    records.head.status should be("pending")
    records.head.month should be("Jun")
  }

  test("should render entry from template") {
    val csvLine = "pending,1,001,2016/06/01,Jun,\"Turnstiles at Chicago's \"\"L\"\"\",\"Chicago, IL, USA\",,Fujifilm X-T1,FUJINON XF18-135mm F3.5-5.6 R LM OIS WR,crap,crap2,tripod,\"#project365_year1 [day 001] Turnstiles at Chicago's \"\"L\"\"\",2016/6-1/project365_year1-day-001,http://www.apn-photographia.com/blog/2016/6-1/project365_year1-day-001"

    val records = CSVReader[Project365Entry].readCSVFromString(csvLine)
    val result = MonospaceFileFormat.monofile(records.head)

    result should be(
      """# #project365_year1 [day 001] Turnstiles at Chicago's "L"
        |
        |-- Gear
        |Fujifilm X-T1
        |FUJINON XF18-135mm F3.5-5.6 R LM OIS WR
        |Filters: crap + crap2
        |tripod
        |
        |-- Post
        |Lightroom :
        |Photoshop:
        |
        |
        |#apnphotographia#blog#project365_year1#pending #pending""".stripMargin)

  }
}

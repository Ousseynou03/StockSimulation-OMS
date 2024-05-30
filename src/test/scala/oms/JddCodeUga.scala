package oms

import java.io.PrintWriter

object JddCodeUga {
  def main(args: Array[String]): Unit = {
    val startCode = 82010
    val endCode = 200000
    val codeRange = startCode to endCode

    val csvFilePath = "src/test/resources/data/jddCodeUga.csv"
    val writer = new PrintWriter(csvFilePath)
    writer.write("code-uga\n")
    codeRange.foreach(code => writer.write(s"DA10000024-$code\n"))
    writer.close()
    println("Le fichier est crée avec succes.")
  }
}

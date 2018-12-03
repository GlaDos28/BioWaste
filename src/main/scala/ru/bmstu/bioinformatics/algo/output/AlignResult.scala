package main.scala.ru.bmstu.bioinformatics.algo.output

case class AlignResult(score: Int)

object AlignResult {
    def fromStripAligns(aligns: List[Int]): AlignResult = AlignResult(aligns.max)
}

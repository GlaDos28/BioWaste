package main.scala.ru.bmstu.bioinformatics.algo

import main.scala.ru.bmstu.bioinformatics.algo.util.{DiagSum, Strip}
import main.scala.ru.bmstu.bioinformatics.algo.input.{DotMatrix, ScoreTable, SeqPair}
import main.scala.ru.bmstu.bioinformatics.algo.output.AlignResult

object cli {
    def main(args: Array[String]): Unit = {

        /* Input */

        val scoreTable = new ScoreTable("ATGC", Array(
            Array( 5, -4, -4, -4),
            Array(-4,  5, -4, -4),
            Array(-4, -4,  5, -4),
            Array(-4, -4, -4,  5)
        ))

        val dm = new DotMatrix(Array(
            Array(false, false, false, false, false, true,  false, false, false, false, false, false),
            Array(false, true,  false, false, false, false, false, true,  false, false, false, false),
            Array(false, false, true,  false, false, false, false, false, true,  false, false, false),
            Array(false, false, false, true,  false, false, false, false, false, true,  false, false),
            Array(false, false, false, false, true,  false, false, false, false, false, true,  false),
            Array(false, false, false, false, false, false, false, false, false, false, false, false),
            Array(false, false, false, false, false, true,  false, false, false, false, false, false),
            Array(false, false, false, false, false, false, false, false, false, false, false, false),
        ))

        val seqPair = SeqPair(
            "GCATCGGC",
            "CCATCGCCATCG"
        )

        /* Process */

        val diagSum       = DiagSum.fromDotMatrix(dm, 2)
        val bestOffsets   = diagSum.pickMax(3)
        val bestDiags     = bestOffsets.map(seqPair.getDiagonalSeqs)
        //val bestTrimDiags = bestDiags.map(_.trimmedToMaxLocal(scoreTable))
        val strips        = Strip.scanlineDiagsFitStrip(6)(bestOffsets)
        val stripAligns   = strips.map(_.smithWatermanScore(-1)(seqPair, scoreTable))
        val alignRes      = AlignResult.fromStripAligns(stripAligns)

        println(alignRes)
    }
}

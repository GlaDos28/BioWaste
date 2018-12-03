package main.scala.ru.bmstu.bioinformatics.algo.util

import main.scala.ru.bmstu.bioinformatics.algo.input.{ScoreTable, SeqPair}

import scala.collection.mutable.ListBuffer

class Strip private(val diags: List[Diagonal]) {
    def leftOffset:  Int = diags.head
    def rightOffset: Int = diags.last

    def smithWatermanScore(gapPenalty: Int)(implicit seqPair: SeqPair, scoreTable: ScoreTable): Int = {
        val topBound    = leftOffset
        val bottomBound = rightOffset

        var cnt            = 0
        var i              = math.max( topBound    + 1, 1)
        var curFirstColInd = math.max(-bottomBound + 1, 1)
        var curLastColInd  = math.max(-topBound    + 1, 1)

        var maxScore = 0

        /* First row elements */

        var rowScore = ListBuffer[Int]()
        rowScore += 0  // First row first element

        for (_ <- curFirstColInd to curLastColInd) {
            rowScore += 0
        }

        /* --- */

        while (i < seqPair.s1.length && curFirstColInd < seqPair.s2.length) {
            var newRowScore = ListBuffer[Int]()
            newRowScore += 0  // Row first element

            for (j <- curFirstColInd to curLastColInd) {
                val stScore      = scoreTable.get(seqPair.s1(i - 1), seqPair.s2(j - 1))
                val leftScore    = newRowScore.last
                val topScore     = rowScore(j - curFirstColInd + 1)
                val topLeftScore = rowScore(j - curFirstColInd)

                val score = math.max(math.max(
                    leftScore    + gapPenalty,
                    topScore     + gapPenalty),
                    topLeftScore + stScore)

                maxScore = math.max(score, maxScore)

                newRowScore += score
            }

            newRowScore += 0
            rowScore = newRowScore

            cnt += 1
            i   += 1
            curFirstColInd =          math.max(-bottomBound + 1 + cnt, 1)
            curLastColInd  = math.min(math.max(-topBound    + 1 + cnt, 1), seqPair.s2.length - 1)
        }

        maxScore
    }
}

object Strip {
    def scanlineDiagsFitStrip(w: Int)(diags: List[Diagonal]): List[Strip] = {
        val sortedDiags = diags.sortWith(_.offset < _.offset)
        val resBuffer   = ListBuffer[Strip]()

        var strip  = Vector[Diagonal](sortedDiags.head)
        var follow = 1

        /* Util functions */

        val fillUntilPossible = () => {
            while (follow < sortedDiags.size && sortedDiags(follow) - strip.head + 1 <= w) {
                strip :+= sortedDiags(follow)
                follow += 1
            }
        }

        val removeUntilEnough = () => strip = strip.dropWhile(strip.last - _ + 1 > w)

        /* --- */

        fillUntilPossible()
        resBuffer += new Strip(strip.toList)

        while (follow < sortedDiags.size) {
            strip :+= sortedDiags(follow)
            follow += 1

            removeUntilEnough()
            fillUntilPossible()

            resBuffer += new Strip(strip.toList)
        }

        resBuffer.toList
    }
}

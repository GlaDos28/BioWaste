package main.scala.ru.bmstu.bioinformatics.algo

object Preamble {
    implicit class ExtraTraversableOnceOps[T](seq: TraversableOnce[T]) {
        def maxElemLen: Int = (0 /: seq.map(_.toString.length))(math.max)

        def mkTabbedString(maxElemLen: Int, separator: String = " "): String = {
            val builder = new StringBuilder()
            val it      = seq.toIterator

            if (!it.hasNext) {
                return ""
            }

            var elemStr = it.next.toString

            elemStr(0) match {
                case '-' => builder.append(elemStr).append(" ")
                case _   => builder.append(' ').append(elemStr)
            }

            while (it.hasNext) {
                builder.append(" " * (maxElemLen - elemStr.length))
                elemStr = it.next.toString

                elemStr(0) match {
                    case '-' => builder.append(elemStr).append(" ")
                    case _   => builder.append(' ').append(elemStr)
                }
            }

            builder.toString
        }
    }
}

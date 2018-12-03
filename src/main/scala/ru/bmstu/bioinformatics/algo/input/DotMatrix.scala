package main.scala.ru.bmstu.bioinformatics.algo.input

class DotMatrix(val m: Array[Array[Boolean]]) {
    def rowNum: Int = m.length
    def colNum: Int = m(0).length
    def get(rowInd: Int, colInd: Int): Boolean = m(rowInd)(colInd)
}

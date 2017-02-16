package com.example

import java.io.File
import java.util.*


fun main(args: Array<String>) {
    run("D:\\projets\\hashcode-pizza\\src\\main\\resources\\small.in", "D:\\projets\\hashcode-pizza\\src\\main\\resources\\small.out")
    run("D:\\projets\\hashcode-pizza\\src\\main\\resources\\example.in", "D:\\projets\\hashcode-pizza\\src\\main\\resources\\example.out")
}


fun run(path:String, out:String) {

    val lines = File(path).readLines()

    val first = lines.first()
    val rest = lines.drop(1);

    val (rowCount, columnCount, eltsPerSlice, maxCellsPerSlice) = first.split(" ")

    println("rows : $rowCount")
    println("columns : $columnCount")
    println("elements per slice : $eltsPerSlice")
    println("max cells per slice : $maxCellsPerSlice")

    val pizza = Pizza(rowCount.toInt(), columnCount.toInt())
    pizza.load(rest)

    println(pizza)

    val ingredient = pizza[1,1]
    println(ingredient)

    val slices = arrayListOf<Slice>();

    for(r in 0 until pizza.rowCount){
        for(c in 0 until pizza.columnCount) {

            val last = pizza[r, c-1] ?: continue;

            val current = pizza[r, c];

            if(last.type != current!!.type && !isInSlices(slices, current) && !isInSlices(slices, last)) {
                println("part possible")
                val slice = Slice(last, current)
                slices.add(slice)
            }

        }
    }

    for(c in 0 until pizza.columnCount){
        for(r in 0 until pizza.rowCount) {

            val last = pizza[r-1, c] ?: continue;

            val current = pizza[r, c];

            if(last.type != current!!.type && !isInSlices(slices, current) && !isInSlices(slices, last)) {
                println("part possible")
                val slice = Slice(last, current)
                slices.add(slice)
            }

        }
    }

    File(out).printWriter().use { out ->
        out.println(slices.size)

        slices.forEach {
            slice -> out.println("${slice.first.row} ${slice.first.col} ${slice.last.row} ${slice.last.col}")
        }
    }

}

fun isInSlices(slices: ArrayList<Slice>, cell:Cell):Boolean {
    println("isInSlice ? $cell" )
    val result = slices.filter { slice -> slice.isIn(cell)}.size > 0
    println("result : $result")
    return result
}

data class Slice(val first:Cell, val last:Cell) {

    fun isIn(cell:Cell):Boolean {
        println("cell  :$cell")
        println("first :$first")
        println("last :$last")
        println("result :" + (first == cell || last == cell))
        return (first == cell || last == cell)
    }
}

data class Cell(val row:Int, val col:Int, val type:String) {

}


data class Pizza(val rowCount:Int, val columnCount:Int) {

    val ingredients: Board<Cell?>

    init {
        println("initializing Pizza")
        ingredients = Board(rowCount, columnCount)
    }

    fun load(rows: List<String>) {
        if (rows.size != rowCount) {
            throw Exception("Erreur : le nombre de ligne ne correspond pas à ce qui est attendu")
        }

        rows.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, value ->
                    ingredients[rowIndex, colIndex] = Cell(rowIndex, colIndex,"$value")
                }
        }
    }

    operator fun get(row: Int, col: Int): Cell?  {
        try {
            return ingredients[row][col]
        } catch(e:ArrayIndexOutOfBoundsException) {
            return null
        }
    }

    override fun toString(): String {
        val str = StringBuilder()
        str.append("Pizza [rows=$rowCount, cols=$columnCount]\n\n")
        for(i in 0 until rowCount) {
            str.append(ingredients[i].joinToString("")).append("\n")
        }
        return "$str"
    }
}








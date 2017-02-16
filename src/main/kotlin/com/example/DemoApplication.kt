package com.example

import java.io.File
import java.util.*


fun main(args: Array<String>) {
    run("C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\small.in", "C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\small.out")
    run("C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\example.in", "C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\example.out")
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
                val cells = ArrayList<Cell>()
                cells.add(last)
                cells.add(current)
                val slice = Slice(cells)
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
                val cells = ArrayList<Cell>()
                cells.add(last)
                cells.add(current)
                val slice = Slice(cells)
                slices.add(slice)
            }

        }
    }

    File(out).printWriter().use { out ->
        out.println(slices.size)

        slices.forEach {
            slice -> slice.cells.forEach {
                cell -> out.print("${cell.row} ${cell.col} ")
            }
            out.print("\n")
        }
    }

}

fun isInSlices(slices: ArrayList<Slice>, cell:Cell):Boolean {
    println("isInSlice ? $cell" )
    val result = slices.filter { slice -> slice.isIn(cell)}.size > 0
    println("result : $result")
    return result
}

data class Slice(val cells : ArrayList<Cell>) {

    fun isIn(cell:Cell):Boolean {
        println("cell  :$cell")
        println("cells :$cells")
        println("result :" + (cells.indexOf(cell) != -1))
        return (cells.indexOf(cell) != -1)
    }

    fun numberOfCellsOfElement(type:String):Int {
        var res = 0
        cells.forEach {
            cell -> if (cell.type == type) {
                res++
            }
        }
        return res
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








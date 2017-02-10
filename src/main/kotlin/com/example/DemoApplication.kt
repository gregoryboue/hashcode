package com.example

import java.io.File


fun main(args: Array<String>) {
    run("D:\\projets\\hashcode-pizza\\src\\main\\resources\\small.in")
}


fun run(path:String) {

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



//    pizza.ingredients.forEachIndexed { r, c, ingredient ->
//        println("$r, $c, $ingredient")
//    }

    for(r in 0 until pizza.rowCount){
        for(c in 0 until pizza.columnCount) {

            if()

        }
    }



}

data class Slice(val first:Cell, val last:Cell) {

    // nombre de cellules dans la part
    fun cellsCount() {
        first.row
    }
}

data class Cell(val row:Int, val col:Int)


data class Pizza(val rowCount:Int, val columnCount:Int) {

    val ingredients: Board<Ingredient?>

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
                    ingredients[rowIndex, colIndex] = Ingredient("$value")
                }
        }
    }

    operator fun get(row: Int, col: Int): Ingredient? = ingredients[row][col]

    override fun toString(): String {
        val str = StringBuilder()
        str.append("Pizza [rows=$rowCount, cols=$columnCount]\n\n")
        for(i in 0 until rowCount) {
            str.append(ingredients[i].joinToString("")).append("\n")
        }
        return "$str"
    }
}

// représente une cellule
data class Ingredient(val type:String) {
    override fun toString() = type
}







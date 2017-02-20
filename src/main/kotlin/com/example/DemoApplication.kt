package com.example

import java.io.File
import java.util.*


fun main(args: Array<String>) {

    run("C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\big.in", "C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\big.out")
    run("C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\medium.in", "C:\\Users\\Gregory\\IdeaProjects\\hashcode\\src\\main\\resources\\medium.out")
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
    val pizza2 = Pizza(rowCount.toInt(), columnCount.toInt())
    pizza2.load(rest)

    println(pizza)

    val ingredient = pizza[1,1]
    println(ingredient)
    val slices = firstWay(pizza, maxCellsPerSlice.toInt(),eltsPerSlice.toInt())

    val slices2 = secondWay(pizza2, maxCellsPerSlice.toInt(),eltsPerSlice.toInt())

    val numberOfCellFirstWay = pizza.getnumberOfCellsInSlice(slices)
    val numberOfCellSecondWay = pizza.getnumberOfCellsInSlice(slices2)

    var finalSlices = arrayListOf<Slice>()
    if (numberOfCellFirstWay > numberOfCellSecondWay) {
        finalSlices = slices
    } else {
        finalSlices = slices2
    }
    File(out).printWriter().use { out ->
        out.println(finalSlices.size)
        out.println("1: ${slices.size}, 2: ${slices2.size} ")
        out.println("firstWay: $numberOfCellFirstWay, secondWay: $numberOfCellSecondWay ")

        slices.forEach {
            slice -> out.println("${slice.cells.first().row} ${slice.cells.first().col} ${slice.cells.last().row} ${slice.cells.last().col}")
        }
    }

}

fun firstWay(pizza:Pizza, maxCellsPerSlice:Int,eltsPerSlice:Int):ArrayList<Slice> {
    val slices = arrayListOf<Slice>();
    var cells = ArrayList<Cell>()
    var slice = Slice(cells)
    for(r in 0 until pizza.rowCount){
        cells = ArrayList<Cell>()
        slice = Slice(cells)
        for(c in 0 until pizza.columnCount) {

            val current = pizza[r, c] ?: continue;

            // Si la part n'a pas assez de cellules et que la cellule n'est pas deja dans une part on l'ajoute a la part
            if(slice.cells.size < maxCellsPerSlice && !current.isInSlice) {
                println("part possible")

                cells.add(current)
                //si la part est la derniere de la rangee et qu'elle est valide on l'ajoute
                if (c == pizza.columnCount -1 && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                        && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                    slices.add(slice)
                    slice.setCellUnavailable(pizza)
                    println(slice)
                    cells = ArrayList<Cell>()
                    slice = Slice(cells)

                }
                // Sinon si la part a le nombre limite de cellule on ajoute la part et on reinitialise
            } else if (slice.cells.size == maxCellsPerSlice && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                    && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                // Sinon si on a une part a une cellule et que l'on est a la fin de la ligne on l'ajoute
                // TODO : verifier qu'on a bien assez d'ingredients de chaque type
            } else if (cells.size >= 1 && r == pizza.columnCount - 1  && slice.cells.size < maxCellsPerSlice && !current.isInSlice) {
                cells.add(current)
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                //Sinon on cree une nouvelle part
            } else {
                cells = ArrayList<Cell>()
                slice = Slice(cells)
            }

        }
    }
    cells = ArrayList<Cell>()
    slice = Slice(cells)
    for(c in 0 until pizza.columnCount){
        cells = ArrayList<Cell>()
        slice = Slice(cells)
        for(r in 0 until pizza.rowCount) {

            //val last = pizza[r-1, c] ?: continue;

            val current = pizza[r, c] ?: continue;
            // Si la part n'a pas assez de cellules et que la cellule n'est pas deja dans une part on l'ajoute a la part
            if(slice.cells.size < maxCellsPerSlice &&  !current.isInSlice) {
                println("part possible")

                cells.add(current)

                //si la part est la derniere de la colonne et qu'elle est valide on l'ajoute
                if (c == pizza.rowCount -1 && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                        && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                    slices.add(slice)
                    slice.setCellUnavailable(pizza)
                    println(slice)
                    cells = ArrayList<Cell>()
                    slice = Slice(cells)

                }
                // Sinon si la part a le nombre limite de cellule on ajoute la part et on reinitialise
            } else if (slice.cells.size == maxCellsPerSlice && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                    && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                // Sinon si on a une part a une cellule et que l'on est a la fin de la colonne on l'ajoute
                // TODO : verifier qu'on a bien assez d'ingredients de chaque type
            } else if (cells.size >= 1 && r == pizza.rowCount - 1 && slice.cells.size < maxCellsPerSlice && !current.isInSlice) {
                cells.add(current)
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
            } else {
                cells = ArrayList<Cell>()
                slice = Slice(cells)
            }

        }
    }
    return slices;
}

fun secondWay(pizza:Pizza, maxCellsPerSlice:Int,eltsPerSlice:Int):ArrayList<Slice> {
    val slices = arrayListOf<Slice>();
    var cells = ArrayList<Cell>()
    var slice = Slice(cells)
    cells = ArrayList<Cell>()
    slice = Slice(cells)
    for(c in 0 until pizza.columnCount){
        cells = ArrayList<Cell>()
        slice = Slice(cells)
        for(r in 0 until pizza.rowCount) {

            //val last = pizza[r-1, c] ?: continue;

            val current = pizza[r, c] ?: continue;
            // Si la part n'a pas assez de cellules et que la cellule n'est pas deja dans une part on l'ajoute a la part
            if(slice.cells.size < maxCellsPerSlice &&  !current.isInSlice) {
                println("part possible")

                cells.add(current)

                //si la part est la derniere de la colonne et qu'elle est valide on l'ajoute
                if (c == pizza.rowCount -1 && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                        && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                    slices.add(slice)
                    slice.setCellUnavailable(pizza)
                    println(slice)
                    cells = ArrayList<Cell>()
                    slice = Slice(cells)

                }
                // Sinon si la part a le nombre limite de cellule on ajoute la part et on reinitialise
            } else if (slice.cells.size == maxCellsPerSlice && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                    && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                // Sinon si on a une part a une cellule et que l'on est a la fin de la colonne on l'ajoute
                // TODO : verifier qu'on a bien assez d'ingredients de chaque type
            } else if (cells.size >= 1 && r == pizza.rowCount - 1 && slice.cells.size < maxCellsPerSlice && !current.isInSlice) {
                cells.add(current)
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
            } else {
                cells = ArrayList<Cell>()
                slice = Slice(cells)
            }

        }
    }
    for(r in 0 until pizza.rowCount){
        cells = ArrayList<Cell>()
        slice = Slice(cells)
        for(c in 0 until pizza.columnCount) {

            val current = pizza[r, c] ?: continue;

            // Si la part n'a pas assez de cellules et que la cellule n'est pas deja dans une part on l'ajoute a la part
            if(slice.cells.size < maxCellsPerSlice && !current.isInSlice) {
                println("part possible")

                cells.add(current)
                //si la part est la derniere de la rangee et qu'elle est valide on l'ajoute
                if (c == pizza.columnCount -1 && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                        && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                    slices.add(slice)
                    slice.setCellUnavailable(pizza)
                    println(slice)
                    cells = ArrayList<Cell>()
                    slice = Slice(cells)

                }
                // Sinon si la part a le nombre limite de cellule on ajoute la part et on reinitialise
            } else if (slice.cells.size == maxCellsPerSlice && slice.numberOfCellsOfElement("T") >= eltsPerSlice
                    && slice.numberOfCellsOfElement("M") >= eltsPerSlice) {
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                // Sinon si on a une part a une cellule et que l'on est a la fin de la ligne on l'ajoute
                // TODO : verifier qu'on a bien assez d'ingredients de chaque type
            } else if (cells.size >= 1 && r == pizza.columnCount - 1  && slice.cells.size < maxCellsPerSlice && !current.isInSlice) {
                cells.add(current)
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                //Sinon on cree une nouvelle part
            } else {
                cells = ArrayList<Cell>()
                slice = Slice(cells)
            }

        }
    }

    return slices;
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

    fun setCellUnavailable(pizza:Pizza) {
        cells.forEach {
            cell -> pizza.ingredients[cell.row][cell.col]!!.setInSlice()
        }
    }

}

data class Cell(val row:Int, val col:Int, val type:String) {
    var isInSlice: Boolean
    init {
        isInSlice = false
    }
    fun setInSlice() {
        isInSlice = true
    }
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

    fun getnumberOfCellsInSlice(slices:ArrayList<Slice>):Int {
        var sum =0
        slices.forEach {
            slice -> slice.cells.forEach {
                cell -> sum++
            }
        }
        return sum
    }
}

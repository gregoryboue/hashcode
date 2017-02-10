package com.example


class Board<T> (val rowCount: Int, val columnCount: Int, val array: Array<Array<T>>) {

    companion object {
        inline operator fun <reified T> invoke(rowCount: Int, columnCount: Int) =
            Board(rowCount, columnCount, Array(rowCount, { arrayOfNulls<T>(columnCount) }))
    }

    operator fun get(row: Int, col: Int): T {
        return array[row][col]
    }

    operator fun set(row: Int, col: Int, t: T) {
        array[row][col] = t
    }

    operator fun get(row: Int): Array<T> {
        return array[row]
    }

    inline fun forEach(operation: (T) -> Unit) {
        array.forEach { it.forEach { operation.invoke(it) } }
    }

    inline fun forEachIndexed(operation: (row: Int, col: Int, T) -> Unit) {
        array.forEachIndexed { row, p -> p.forEachIndexed { col, t -> operation.invoke(row, col, t) } }
    }

    // il faudrait la distance entre deux cellules
}
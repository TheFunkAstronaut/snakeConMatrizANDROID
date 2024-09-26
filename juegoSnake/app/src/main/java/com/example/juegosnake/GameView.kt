package com.example.juegosnake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.os.Handler
import kotlin.random.Random

class GameView(context: Context) : View(context) {
    /**
     * Inicio la snake y la comida
     */
    private val snake = mutableListOf<Pair<Int, Int>>()
    private var food: Pair<Int, Int> = Pair(0, 0)

    /**
     * Size de la matriz que se ocupara en la pantalla manualmente
     */
    private var rows = 44
    private var cols = 20
    private var cellSize = 0

    private var handler = Handler()
    private var running = false
    private var direction = Direction.RIGHT
    private var grow = false

    private val paintSnake = Paint().apply { color = Color.GREEN }
    private val paintFood = Paint().apply { color = Color.RED }
    private val paintGrid = Paint().apply { color = Color.LTGRAY }

    init {
        resetGame()
    }

    private val updateTask = object : Runnable {
        override fun run() {
            if (running) {
                moveSnake()
                invalidate()
                handler.postDelayed(this, 500)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        cellSize = width / cols
        drawGrid(canvas)
        drawSnake(canvas)
        drawFood(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        for (i in 0..cols) {
            canvas.drawLine(i * cellSize.toFloat(), 0f, i * cellSize.toFloat(), height.toFloat(), paintGrid)
        }
        for (i in 0..rows) {
            canvas.drawLine(0f, i * cellSize.toFloat(), width.toFloat(), i * cellSize.toFloat(), paintGrid)
        }
    }

    private fun drawSnake(canvas: Canvas) {
        for (pos in snake) {
            canvas.drawRect(
                pos.first * cellSize.toFloat(),
                pos.second * cellSize.toFloat(),
                (pos.first + 1) * cellSize.toFloat(),
                (pos.second + 1) * cellSize.toFloat(),
                paintSnake
            )
        }
    }

    private fun drawFood(canvas: Canvas) {
        canvas.drawRect(
            food.first * cellSize.toFloat(),
            food.second * cellSize.toFloat(),
            (food.first + 1) * cellSize.toFloat(),
            (food.second + 1) * cellSize.toFloat(),
            paintFood
        )
    }

    private fun resetGame() {
        snake.clear()
        snake.add(Pair(cols / 2, rows / 2))
        spawnFood()
        direction = Direction.RIGHT
        running = true
        handler.post(updateTask)
    }

    private fun spawnFood() {
        var newFood: Pair<Int, Int>
        do {
            newFood = Pair(Random.nextInt(cols), Random.nextInt(rows))
        } while (newFood in snake)
        food = newFood
    }

    private fun moveSnake() {
        val head = snake.first()
        var newHead = head

        when (direction) {
            Direction.UP -> newHead = Pair(head.first, (head.second - 1 + rows) % rows)
            Direction.DOWN -> newHead = Pair(head.first, (head.second + 1) % rows)
            Direction.LEFT -> newHead = Pair((head.first - 1 + cols) % cols, head.second)
            Direction.RIGHT -> newHead = Pair((head.first + 1) % cols, head.second)
        }

        if (newHead in snake) {
            /**
             * Game Over, se vuelvev a iniciar el juego de 0
             */
            resetGame()
        } else {
            snake.add(0, newHead)
            if (newHead == food) {
                spawnFood()
            } else {
                /**
                 * Mueve la snake en la direccion que tenia
                 */
                snake.removeLast()
            }
        }
    }

    /**
     * Este metodo lo hago para que no se altere el manejo de la cabeza de la snake, y haya
     * mejor consistencia con esta
     */

    fun setDirection(newDirection: Direction) {
        if ((direction == Direction.UP && newDirection != Direction.DOWN) ||
            (direction == Direction.DOWN && newDirection != Direction.UP) ||
            (direction == Direction.LEFT && newDirection != Direction.RIGHT) ||
            (direction == Direction.RIGHT && newDirection != Direction.LEFT)
        ) {
            direction = newDirection
        }
    }

    fun startGame() {
        resetGame()
        handler.post(updateTask)
    }
}

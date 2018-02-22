import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * PercolationImage
 *
 * Программа представляет собой решение задачи протекания методом Монте-Карло (задача узлов) для двумерной области
 * решений и ее визуализация. Имеются два вида перколяционных решеток: с одинаковой степенью занятости узлов и с разной
 * степенью занятости узлов, которая увеличивается от верхней границе к нижней. Результатом программы является
 * изображение перколяционной решетки, где черными пикселями обозначены заполненные узлы.
 *
 * В закоментированных частях программы содержатся решение для решетки с одинаковой степенью занятости узлов и функция
 * протекания из центральной точки.
 *
 * Из-за рекурсии в среде разработки был увеличен размер стека до 64 МБ.
 *
 * @author Artur Manukian
 */

public class PercolationImage {

    public static void main(String[] args) throws IOException {

        /* Переменные */
        int i, j; // x и y узлов решетки.
        int heightGrid = 500; // Высота.
        int widthGrid = 500; // Ширина.
        double p = 0.45; // Вероятность занятости узла.

        /* Препятствия на границах */
        // Инициализация перколяционной решетки.
        int[][] grid = new int[heightGrid + 2][widthGrid + 2];
        for (i = 0; i < widthGrid + 2; i++) {
            // Заполнение всех строк
            // нулевыми значениями.
            Arrays.fill(grid[heightGrid], 0);
        }

        /* Задание решетки с одинаковой степенью занятости узлов */
        /*for (i = 1; i < heightGrid + 1; i++) {
            for (j = 1; j < width + 1; j++) {
                // С помощью рандомайзера разыгрывается состояние узла.
                // 0 - если узел занят.
                // 1 - если узел свободен.
                grid[i][j] = Math.random() <= p ? 0 : 1;
            }
        }*/

        /* Задание решетки с разной степенью занятости узлов */
        for (i = 1; i < heightGrid + 1; i++) {
            for (j = 1; j < widthGrid + 1; j++) {
                if (i < 100) {
                    grid[i][j] = Math.random() <= p - 0.16 ? 0 : 1;
                } else if (i >= heightGrid / 5 & i < heightGrid / 5 * 2) {
                    grid[i][j] = Math.random() <= p - 0.08 ? 0 : 1;
                } else if (i >= heightGrid / 5 * 2 & i < heightGrid / 5 * 3) {
                    grid[i][j] = Math.random() <= p - 0.04 ? 0 : 1;
                } else if (i >= heightGrid / 5 * 3 & i < heightGrid / 5 * 4) {
                    grid[i][j] = Math.random() <= p - 0.02 ? 0 : 1;
                } else if (i >= heightGrid / 5 * 4 & i <= heightGrid) {
                    grid[i][j] = Math.random() <= p ? 0 : 1;
                }
            }
        }

        /* Перколяция */
        // Протекание начинается из центрального
        // узла верхней границы решетки.
        /*int x = 1;
        int y = width / 2; // Условный центр.
        if (grid[x][y] != 1) {
            grid[x][y] = 1;
            PercolationImage floodFill = new PercolationImage();
            floodFill.fillGrid(grid, x, y);
        } else {
            PercolationImage floodFill = new PercolationImage();
            floodFill.fillGrid(grid, x, y);
        }*/

        // Протекание начинается из всех свободных
        // узлов верхней границы решетки.
        int x, y;
        for (x = 1; x < 2; x++) {
            for (y = 1; y < widthGrid + 1; y++){
                if (grid[x][y] == 1) {
                    PercolationImage floodFill = new PercolationImage();
                    floodFill.fillGrid(grid, x, y);
                }
            }
        }

        /* Отрисовки и сохранения изображения */
        BufferedImage outputToImage = new BufferedImage(widthGrid, heightGrid, BufferedImage.TYPE_INT_RGB);
        for (y = 0; y < heightGrid; y++) {
            for (x = 0; x < widthGrid; x++) {
                if (grid[y][x] == 2) {
                    // Для всех заполненных узлов
                    // присваивается черный цвет пикселя.
                    outputToImage.setRGB(x, y, 0x000000);
                } else {
                    // Для всех остальных узлов
                    // присваивается белый цвет пикселя.
                    outputToImage.setRGB(x, y, 0xffffff);
                }
            }
        }
        ImageIO.write(outputToImage, "png", new File("C:\\Users\\Artur\\Desktop\\percolation.png"));
    }

    /* Рекурсивный алгоритм заливки */
    private void fillGrid(int[][] grid, int x, int y) {

        if (grid[x][y] == 1) {
            // Если узле свободен, присваивается
            // значение 2 и узел становится заполненным.
            grid[x][y] = 2;

            fillGrid(grid, x, y + 1);
            fillGrid(grid, x, y - 1);
            fillGrid(grid, x + 1, y);
            fillGrid(grid, x - 1, y);
        }
    }
}
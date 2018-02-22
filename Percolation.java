import java.util.Arrays;
import java.util.Locale;

/**
 * Percolation
 *
 * Программа представляет собой решение задачи протекания методом Монте-Карло (задача узлов) для двумерной области
 * решений. Результатом программы является вероятность образования стягивающего кластера для каждого значения p
 * (вероятность занятости узла) с шагом 0.01 по заданному диапазону.
 *
 * @author Artur Manukian
 */

public class Percolation {

    public static void main(String[] args) {

        /* Переменные */
        int i, j; // x и y узлов решетки.
        int heightGrid = 100; // Размер
        int widthGrid = 100; // сетки.
        int counterOfClusters = 0; // Счетчик стягивающих кластеров.
        int n = 1000; // Количество итераций.
        // Вероятность образования
        // стягивающего кластера.
        double probability;
        // Вероятность занятости узла,
        // его начальное и конечное значения.
        double p;
        double pStart = 0.3;
        double pEnd = 0.5;
        double step = 0.01;

        /* Вывод общих сведений */
        System.out.println("Алгоритм рекурсивной заливки для перколяционной решетки");
        System.out.println("\nПерколяция методом Монте-Карло");
        System.out.println("Решетка: " + heightGrid + " х " + widthGrid);
        System.out.println("Количество итераций: " + n);

        /* Цикл для расчета заданного диапазона p */
        for (p = pStart; p < pEnd + step; p += step) {
            // Цикл итераций для лучшей
            // точности вычислений.
            for (int counter = 0; counter < n; counter++) {

                /* Препятствия на границах */
                // Инициализация перколяционной решетки.
                int[][] grid = new int[heightGrid + 2][widthGrid + 2];
                for (i = 0; i < heightGrid + 2; i++) {
                    // Заполнение всех i-тых строк
                    // нулевыми значениями.
                    Arrays.fill(grid[i], 0);
                }

                /* Заполнение решетки */
                for (i = 1; i < heightGrid + 1; i++) {
                    for (j = 1; j < widthGrid + 1; j++) {
                        // С помощью рандомайзера разыгрывается состояние узла.
                        // 0 - если узел занят.
                        // 1 - если узел свободен.
                        grid[i][j] = Math.random() <= p ? 0 : 1;
                    }
                }

                /* Перколяция */
                // Протекание начинается из всех свободных
                // узлов верхней границы решетки.
                for (i = 1; i < 2; i++) {
                    for (j = 1; j < widthGrid + 1; j++){
                        // Если узел свободен, то вызывается
                        // рекурсивный метод fillGrid.
                        if (grid[i][j] == 1) {
                            Percolation floodFill = new Percolation();
                            floodFill.fillGrid(grid, i, j);
                        }
                    }
                }

                /* Проверка наличия стягивающего кластера */
                // Проверка всех элементов последней строки.
                for (i = heightGrid; i < heightGrid + 1; i++) {
                    for (j = 1; j < widthGrid + 1; j++) {
                        // 2 - заполненный узел.
                        // Если один из узлов последней строки является заполненным,
                        // значит протекание дошло до нижней границы и, соответственно,
                        // образовался стягивающий кластер.
                        if (grid[i][j] == 2) {
                            counterOfClusters++;
                            // Прерывание цикла, поскольку важен сам факт образования
                            // стягивающего кластера и нет необходимости в дальнейшем
                            // приросте счетчика на текущей итерации.
                            break;
                        }
                    }
                }
            }
            // Расчет вероятности образования
            // стягивающего кластера.
            probability = (double) counterOfClusters / n;

            System.out.println("\nВероятность занятости узла: p = " + String.format(Locale.ENGLISH, "%(.2f", p));
            System.out.println("Стягивающих кластеров: " + counterOfClusters);
            System.out.println("Вероятность образования стягивающего кластера: Pc = " + probability);
            // Обнуление счетчика кластеров для текущего
            // значения p после его вывода в консоль.
            counterOfClusters = 0;
        }
    }

    /* Рекурсивный алгоритм заливки */
    private void fillGrid(int[][] grid, int i, int j) {

        if (grid[i][j] == 1) {
            // Если узле свободен, присваивается
            // значение 2 и узел становится заполненным.
            grid[i][j] = 2;

            fillGrid(grid, i, j + 1);
            fillGrid(grid, i, j - 1);
            fillGrid(grid, i + 1, j);
            fillGrid(grid, i - 1, j);
        }
    }
}
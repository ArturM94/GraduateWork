import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Diffusion from the point source
 *
 * Программа представляет собой решение задачи диффузии загрязнений в атмосфере методом конечных разностей (явная схема)
 * для двумерной области решений, используя уравнение теплопроводности c добавлением слогаемого Q (источник загрязнения).
 * Результатом программы является .txt файл со значениями концентраций загрязнений в точках двумерного массива.
 *
 * В закоментированной части программы содержатся переменные и решение для двух источников Q. Принципиально ничем
 * не отличается от решения с одним источником.
 *
 * @author Artur Manukian
 */

public class Diffusion {

    public static void main(String[] args) throws FileNotFoundException {

        /* Переменные */
        int i, j; // x и y сетки.
        int size = 40; // Размер сетки.
        int n = 100; // Количество итераций.
        double t = 0; // Граничное условие первого рода (температура).
        double [][] T = new double[size][size]; // Временной слой.
        double [][] newT = new double[size][size]; // Новый временной слой.

        // Значения
        // lambda - теплопроводность,
        // ro - плотность,
        // c - теплоемкость,
        // tau - шаг по времени
        // заданы для температуры 30 градусов Цельсия.
        double lambda = 0.0267;
        double ro = 1.165;
        double c = 1005;
        double tau = 0.01;
        double h; // Шаг по сетке.
        double stability; // Условие устойчивости схемы.

        // Концентрация и координаты
        // для одного источника.
        double conc1, conc2, conc3;
        int xChimney = 20;
        int yChimney = 20;
        double Q = 2;

        // Концентрация и координаты
        // для двух источников.
        /*double conc;
        int xChimney1 = 15;
        int yChimney1 = 20;
        int xChimney2 = 25;
        int yChimney2 = 20;
        double Q1 = 1;
        double Q2 = 0.01;*/

        /* Выполнение условия устойчивости */
        // Подбор значения h таким образом, чтобы условие устойчивости
        // stability было больше tau с условно минимальной разницей.
        for (stability = 0, h = 0; tau >= stability; h += 0.0005) {
            stability = (ro * c * Math.pow(h, 2)) / (2 * lambda);
        }

        /* Заполнение сетки нулевыми значениями */
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                T[i][j] = 0;
            }
        }

        /* Граничные условия */
        // Для левой и правой границы.
        for (i = 0; i < size; i++) {
            T[i][0] = t; // Все i-тые элементы для нулевого j-того.
            T[i][size - 1] = t; // Все i-тые элементы для максимального j-того.
        }
        // Для верхней и нижней границы.
        for (j = 0; j < size; j++) {
            T[0][j] = t; // Все j-тые элементы для нулевого i-того.
            T[size - 1][j] = t; // Все j-тые элементы для максимального i-того.
        }

        /* Сетка с граничными условиями */
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                // Дублирование начальных значений для нового временного слоя.
                newT[i][j] = T[i][j];
            }
        }

        /* Уравнение диффузии для одного источника */
        for (int counter = 0; counter < n; counter++) {
            for (i = 1; i < size - 1; i++) {
                for (j = 1; j < size - 1; j++) {
                    // Источник загрязнения.
                    if (i == yChimney && j == xChimney){
                        T[i][j] = newT[i][j] + ((lambda * tau) / (ro * c) * ((newT[i+1][j] + newT[i-1][j] + newT[i][j+1] + newT[i][j-1] - 4 * newT[i][j]) / Math.pow(h, 2))) + Q;
                    } else {
                        T[i][j] = newT[i][j] + ((lambda * tau) / (ro * c) * ((newT[i+1][j] + newT[i-1][j] + newT[i][j+1] + newT[i][j-1] - 4 * newT[i][j]) / Math.pow(h, 2)));
                    }
                }
            }
            // Перезапись на новый временной слой.
            for (i = 1; i < size - 1; i++) {
                for (j = 1; j < size - 1; j++) {
                    newT[i][j] = T[i][j];
                }
            }
        }

        // В закоментированной части программы содержится решение для двух источников Q.
        // Принципиально ничем не отличается от решения с одним источником.

        /* Уравнение диффузии для двух источников */
        /*for (int counter = 0; counter < n; counter++) {
            for (i = 1; i < size - 1; i++) {
                for (j = 1; j < size - 1; j++) {
                    // Источники загрязнения.
                    if (i == yChimney1 && j == xChimney1){
                        T[i][j] = newT[i][j] + ((lambda * tau) / (ro * c) * ((newT[i+1][j] + newT[i-1][j] + newT[i][j+1] + newT[i][j-1] - 4 * newT[i][j]) / Math.pow(h, 2))) + Q1;
                    } else if (i == yChimney2 && j == xChimney2) {
                        T[i][j] = newT[i][j] + ((lambda * tau) / (ro * c) * ((newT[i+1][j] + newT[i-1][j] + newT[i][j+1] + newT[i][j-1] - 4 * newT[i][j]) / Math.pow(h, 2))) + Q2;
                    } else {
                        T[i][j] = newT[i][j] + ((lambda * tau) / (ro * c) * ((newT[i+1][j] + newT[i-1][j] + newT[i][j+1] + newT[i][j-1] - 4 * newT[i][j]) / Math.pow(h, 2)));
                    }
                }
            }
            // Перезапись на новый временной слой.
            for (i = 1; i < size - 1; i++) {
                for (j = 1; j < size - 1; j++) {
                    newT[i][j] = T[i][j];
                }
            }
        }*/

        /* Вывод сетки в консоль */
        // Необязательная функция.
        System.out.println("Сетка с источником загрязнения:");
        for (i = 1; i < size - 1; i++) {
            for (j = 1; j < size - 1; j++) {
                System.out.print(String.format(Locale.ENGLISH, "%(.2f", newT[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println();

        /* Вывод данных в файл */
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Artur\\Desktop\\DiffusionData.txt")), true);
        for (i = 1; i < size - 1; i++) {
            for (j = 1; j < size - 1; j++) {
                out.printf(String.format("%(.2f", newT[i][j])+" ");
            }
            out.println();
        }
        out.println();

        /* Снятие концентрации в точках */
        conc1 = T[yChimney][xChimney - 3];
        conc2 = T[yChimney][xChimney - 6];
        conc3 = T[yChimney][xChimney - 9];

        System.out.println("Концентрация в точке 1: " + String.format("%(.3f", conc1));
        System.out.println("Концентрация в точке 2: " + String.format("%(.3f", conc2));
        System.out.println("Концентрация в точке 3: " + String.format("%(.3f", conc3));

        System.out.println("\nУсловие устойчивости схемы: " + String.format(Locale.ENGLISH, "%(.5f", stability));
        System.out.println("lambda: " + lambda);
        System.out.println("ro: " + ro);
        System.out.println("c: " + c);
        System.out.println("tau: " + tau);
        System.out.println("h: " + h);
    }
}
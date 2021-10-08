package com.pashovpa.ex5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Integration {
    final double A;
    final double B;
    final double eps;
    final int bisN;
    final int N;
    double[][] legendre;
    double[][] nodes;
    double[][] coefficients;
    double a;
    double b;

    public Integration() {
        this.A = -1;
        this.B = 1;
        this.eps = 10e-17;
        this.bisN = 10000;

        Scanner input = new Scanner(System.in);
        System.out.print("Введите максимальное значение параметра N( >= 1), которое вы будете использовать N = ");
        int N = input.nextInt();
        while (N <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            N = input.nextInt();
        }
        this.N = N;

        double[][] legendre = new double[this.N + 1][this.N + 1];
        for (int i = 0; i <= this.N; i++) {
            Arrays.fill(legendre[i], 0);
        }
        legendre[0][0] = 1;
        legendre[1][1] = 1;
        for (int i = 2; i <= this.N; i++) {
            for (int j = 1; j <= this.N; j++) {
                legendre[i][j] = (2 * i - 1) * legendre[i - 1][j - 1] / i - (i - 1) * legendre[i - 2][j] / i;
            }
            legendre[i][0] = legendre[i - 2][0] * (1 - i) / i;
        }
        this.legendre = legendre;

        double[][] nodes = new double[this.N + 1][this.N];
        Arrays.fill(nodes[0], 0);
        for (int i = 1; i <= this.N; i++) {
            nodes[i] = bisection(separation(bisN, i), i);
        }
        this.nodes = nodes;

        double[][] coefficients = new double[this.N + 1][this.N];
        Arrays.fill(coefficients[0], 0);
        for (int i = 1; i <= this.N; i++) {
            for (int j = 0; j < this.N; j++)
                coefficients[i][j] = 2 * (1 - nodes[i][j] * nodes[i][j]) / (i * i * legendre(nodes[i][j], i - 1) * legendre(nodes[i][j], i - 1));
        }
        this.coefficients = coefficients;

        System.out.println("------------------------------------------------------------");
        for (int i = 1; i < this.N + 1; i++) {
            System.out.println("Для N = " + i);
            System.out.print("Узлы: ");
            for (int j = 0; j < i; j++) {
                System.out.print(" t" + (j + 1) + " = " + nodes[i][j]);
            }
            System.out.println();
            System.out.print("Коэффициенты: ");
            for (int j = 0; j < i; j++) {
                System.out.print(" С" + (j + 1) + " = " + coefficients[i][j]);
            }
            System.out.println();
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("############################################################");
    }

    public double legendre(double X, int degree) {
        if (degree == 0) {
            return 1;
        } else if (degree == 1) {
            return X;
        } else {
            double value = 0;
            for (int i = 0; i < legendre.length; i++) {
                value += legendre[degree][i] * Math.pow(X, i);
            }
            return value;
        }
    }

    public ArrayList<Double> separation(int N, int degree) {
        ArrayList<Double> points = new ArrayList<>();
        double step = (this.B - this.A) / N;
        double X1 = this.A;
        double X2 = X1 + step;
        double Y1 = legendre(X1, degree);
        double Y2;

        while (X2 <= this.B) {
            Y2 = legendre(X2, degree);
            if (Y1 * Y2 <= 0) {
                points.add(X1);
                points.add(X2);
            }
            X1 = X2;
            X2 = X1 + step;
            Y1 = Y2;
        }

        return points;
    }

    public double[] bisection(ArrayList<Double> segments, int degree) {
        double[] nodes = new double[this.N];
        int size = segments.size();
        int count = 0;
        for (int i = 0; i < size; i += 2) {
            double a = segments.get(i);
            double b = segments.get(i + 1);
            double c, fa, fc, X;

            while (b - a > 2 * this.eps) {
                c = (a + b) / 2;
                fa = legendre(a, degree);
                fc = legendre(c, degree);

                if (fa * fc <= 0) {
                    b = c;
                } else {
                    a = c;
                }
            }
            X = (a + b) / 2;
            nodes[count] = X;
            count++;
        }
        return nodes;
    }

    public double fP7(double X) {
        return Math.pow(X, 7) - Math.pow(X, 4) + 3 * X;
    }

    public double FP7(double X) {
        return Math.pow(X, 8) / 8 - Math.pow(X, 5) / 5 + 3 * X * X / 2;
    }

    public void check() {
        System.out.println("Проверка на многочлене x^7 - x^4 + 3x для N = 4");
        int N = 4;
        double J = FP7(this.B) - FP7(this.A);
        System.out.println("Точное значение интеграла на [-1; 1]: " + J);
        double apprJ = 0;
        for (int i = 0; i < N; i++) {
            apprJ += this.coefficients[N - 1][i] * fP7(nodes[N - 1][i]);
        }
        System.out.println("Приближенное значение интеграла на [-1; 1]: " + apprJ);
        System.out.println("Абсолютная фактическая погрешность: " + Math.abs(J - apprJ));
        System.out.println("############################################################");
    }

    public void find() {
        System.out.println("Вычисление интеграла функции: f(x) = sqrt(x) * cos(x^2)");
        Scanner input = new Scanner(System.in);
        System.out.println("Введите пределы интегрирования:");
        System.out.print("a = ");
        double a = input.nextDouble();
        this.a = a;
        System.out.print("b = ");
        double b = input.nextDouble();
        this.b = b;

        System.out.println("------------------------------------------------------------");
        for (int i = 3; i <= this.N; i++) {
            System.out.println("Для N = " + i);
            System.out.print("Узлы: ");
            double[] newNodes = new double[this.N];
            Arrays.fill(newNodes, 0);
            for (int j = 0; j < i; j++) {
                newNodes[j] = (b - a) * nodes[i][j] / 2 + (b + a) / 2;
                System.out.print(" t" + (j + 1) + " = " + newNodes[j]);
            }
            System.out.println();
            System.out.print("Коэффициенты: ");
            double[] newCoefficients = new double[this.N];
            Arrays.fill(newCoefficients, 0);
            for (int j = 0; j < i; j++) {
                newCoefficients[j] = (b - a) * coefficients[i][j] / 2;
                System.out.print(" С" + (j + 1) + " = " + newCoefficients[j]);
            }
            System.out.println();
            double apprJ = 0;
            for (int j = 0; j < N; j++) {
                apprJ += newCoefficients[j] * f(newNodes[j]);
            }
            System.out.println("Приближенное значение интеграла на [" + a + "; " + b + "]: " + apprJ);
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("############################################################");
    }

    public double f(double X) {
        return Math.sqrt(X) * Math.cos(X * X);
    }

    public static void main(String[] args) {
        System.out.println("Задание №5: ВЫЧИСЛЕНИЕ ИНТЕГРАЛА ПРИ ПОМОЩИ КФ ГАУССА");
        Integration test = new Integration();
        test.check();
        while (true) {
            test.find();
        }
    }
}



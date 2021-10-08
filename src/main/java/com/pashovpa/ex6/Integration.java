package com.pashovpa.ex6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Integration {
    final double A = -1;
    final double B = 1;
    final double eps = 10e-12;
    double a;
    double b;
    double h;
    double gaussValue;
    double typeGaussValue;
    double[][] legendre;
    double[] nodes;
    double[] coefficients;
    final int bisN = 10000;
    int N;
    int m;

    public double p(double X) {
        return Math.cos(X);
    }

    public double f(double X) {
        return Math.sin(X);
    }

    public double g(double X) {
        return p(X) * f(X);
    }

    public Integration() {
        Scanner input = new Scanner(System.in);
        System.out.println("Введите пределы интегрирования:");
        System.out.print("a = ");
        this.a = input.nextDouble();
        System.out.print("b = ");
        this.b = input.nextDouble();
    }

    public double gauss() {
        Scanner input = new Scanner(System.in);
        System.out.print("Введите количество узлов: N = ");
        int N = input.nextInt();
        while (N <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            N = input.nextInt();
        }
        this.N = N;

        System.out.print("Введите число промежутков деления отрезка [" + this.a + ", " + this.b + "]: m = ");
        int m = input.nextInt();
        while (m <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            m = input.nextInt();
        }
        this.m = m;
        this.h = (this.b - this.a) / m;

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

        double[] nodes = new double[this.N];
        Arrays.fill(nodes, 0);
        nodes = bisection(separation(bisN, this.N), this.N);
        this.nodes = nodes;

        double[] coefficients = new double[this.N];
        Arrays.fill(coefficients, 0);
        for (int j = 0; j < this.N; j++) {
            coefficients[j] = 2 * (1 - nodes[j] * nodes[j]) / (N * N * legendre(nodes[j], N - 1) * legendre(nodes[j], N - 1));
        }
        this.coefficients = coefficients;

        double value = 0;
        for (int j = 0; j < this.m; j++) {
            for (int k = 0; k < this.N; k++) {
                value += coefficients[k] * g(this.h / 2 * nodes[k] + this.a + j * this.h + this.h / 2);
            }
        }
        value *= this.h / 2;

//		System.out.println("------------------------------------------------------------");
        System.out.println("Исходные параметры: N = " + this.N + ", m = " + this.m);
        System.out.print("Узлы: ");
        for (int j = 0; j < this.N; j++) {
            System.out.print(" t" + (j + 1) + " = " + nodes[j]);
        }
        System.out.println();
        System.out.print("Коэффициенты: ");
        for (int j = 0; j < this.N; j++) {
            System.out.print(" С" + (j + 1) + " = " + coefficients[j]);
        }
        System.out.println();
        System.out.println("Полученное значение интеграла: " + value);
        System.out.println("#############################################################");

        this.gaussValue = value;
        return value;
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

    public double typeGauss() {
        double m0 = Fm0(this.b) - Fm0(this.a);
        double m1 = Fm1(this.b) - Fm1(this.a);
        double m2 = Fm2(this.b) - Fm2(this.a);
        double m3 = Fm3(this.b) - Fm3(this.a);

        double a1 = (m0 * m3 - m2 * m1) / (m1 * m1 - m2 * m0);
        double a2 = (m2 * m2 - m3 * m1) / (m1 * m1 - m2 * m0);

        double x1 = (-1 * a1 + Math.sqrt(a1 * a1 - 4 * a2)) / 2;
        double x2 = (-1 * a1 - Math.sqrt(a1 * a1 - 4 * a2)) / 2;

        double A1 = (m1 - x2 * m0) / (x1 - x2);
        double A2 = (m1 - x1 * m0) / (x2 - x1);

        double typeGaussValue = A1 * f(x1) + A2 * f(x2);

//		System.out.println("------------------------------------------------------------");
        System.out.print("Моменты весовой функции: ");
        System.out.print(" m0 = " + m0);
        System.out.print(" m1 = " + m1);
        System.out.print(" m2 = " + m2);
        System.out.print(" m3 = " + m3);
        System.out.println();

        System.out.print("Ортогональный многочлен: x^2 + (" + a1 + ") * x + " + a2);
        System.out.println();

        System.out.print("Узлы: ");
        System.out.print(" x1 = " + x1);
        System.out.print(" x2 = " + x2);
        System.out.println();

        System.out.print("Коэффициенты: ");
        System.out.print(" A1 = " + A1);
        System.out.print(" A2 = " + A2);
        System.out.println();

        System.out.println("Проверка на коэффициенты: ");
        if (A1 > 0 && A2 > 0) {
            System.out.println("A1 > 0 AND A2 > 0 -- TRUE");
        } else {
            System.out.println("A1 > 0 AND A2 > 0 -- FALSE");
        }
        System.out.println("(A1 + A2) - m0 = " + (A1 + A2 - m0));

        System.out.println("Полученное значение интеграла по КФ типа Гаусса с 2-мя узлами: " + typeGaussValue);
        System.out.println("Разница с интегралом полученным по составной КФ Гаусса: " + Math.abs(this.gaussValue - typeGaussValue));
        System.out.println("#############################################################");

        this.typeGaussValue = typeGaussValue;
        return typeGaussValue;
    }

    public double Fm0(double X) {
        return Math.sin(X);
    }

    public double Fm1(double X) {
        return X * Math.sin(X) + Math.cos(X);
    }

    public double Fm2(double X) {
        return (X * X - 2) * Math.sin(X) + 2 * X * Math.cos(X);
    }

    public double Fm3(double X) {
        return (X * X - 6) * X * Math.sin(X) + 3 * (X * X - 2) * Math.cos(X);
    }

    public void mehler() {
        Scanner input = new Scanner(System.in);
        System.out.print("Введите количество узлов для КФ Мелера: ");
        System.out.println();
        System.out.print("N1 = ");
        int N1 = input.nextInt();
        while (N1 <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            N1 = input.nextInt();
        }
        System.out.print("N2 = ");
        int N2 = input.nextInt();
        while (N2 <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            N2 = input.nextInt();
        }
        System.out.print("N3 = ");
        int N3 = input.nextInt();
        while (N3 <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            N3 = input.nextInt();
        }

        double[] nodesMehler1 = new double[N1 + 1];
        for (int i = 1; i <= N1; i++) {
            nodesMehler1[i] = Math.cos((2 * i - 1) * Math.PI / (2 * N1));
        }
        double[] nodesMehler2 = new double[N2 + 1];
        for (int i = 1; i <= N2; i++) {
            nodesMehler2[i] = Math.cos((2 * i - 1) * Math.PI / (2 * N2));
        }
        double[] nodesMehler3 = new double[N3 + 1];
        for (int i = 1; i <= N3; i++) {
            nodesMehler3[i] = Math.cos((2 * i - 1) * Math.PI / (2 * N3));
        }

        double coefficient1 = Math.PI / N1;
        double coefficient2 = Math.PI / N2;
        double coefficient3 = Math.PI / N3;

        double value1 = 0;
        for (int i = 1; i <= N1; i++) {
            value1 += fM(nodesMehler1[i]);
        }
        value1 *= coefficient1;

        double value2 = 0;
        for (int i = 1; i <= N2; i++) {
            value2 += fM(nodesMehler2[i]);
        }
        value2 *= coefficient2;

        double value3 = 0;
        for (int i = 1; i <= N3; i++) {
            value3 += fM(nodesMehler3[i]);
        }
        value3 *= coefficient3;

        System.out.println("Значение параметра N1 = " + N1);
        System.out.print("Узлы: ");
        for (int j = 1; j <= N1; j++) {
            System.out.print(" x" + j + " = " + nodesMehler1[j]);
        }
        System.out.println();
        System.out.print("Коэффициенты: ");
        for (int j = 1; j <= N1; j++) {
            System.out.print(" С" + j + " = " + coefficient1);
        }
        System.out.println();
        System.out.println("Приближённое значение интеграла: " + value1);
        System.out.println("------------------------------------------------------------");

        System.out.println("Значение параметра N2 = " + N2);
        System.out.print("Узлы: ");
        for (int j = 1; j <= N2; j++) {
            System.out.print(" x" + j + " = " + nodesMehler2[j]);
        }
        System.out.println();
        System.out.print("Коэффициенты: ");
        for (int j = 1; j <= N2; j++) {
            System.out.print(" С" + j + " = " + coefficient2);
        }
        System.out.println();
        System.out.println("Приближённое значение интеграла: " + value2);
        System.out.println("------------------------------------------------------------");

        System.out.println("Значение параметра N3 = " + N3);
        System.out.print("Узлы: ");
        for (int j = 1; j <= N3; j++) {
            System.out.print(" x" + j + " = " + nodesMehler3[j]);
        }
        System.out.println();
        System.out.print("Коэффициенты: ");
        for (int j = 1; j <= N3; j++) {
            System.out.print(" С" + j + " = " + coefficient3);
        }
        System.out.println();
        System.out.println("Приближённое значение интеграла: " + value3);
        System.out.println("------------------------------------------------------------");
    }

    public double fM(double X) {
        return Math.cos(3 * X) / (0.3 + X * X);
    }

    public static void main(String[] args) {
        System.out.println("Задание №6: ПРИБЛИЖЁННОЕ ВЫЧИСЛЕНИЕ ИНТЕГРАЛА ПРИ ПОМОЩИ КФ НАСТ");
        Integration test = new Integration();
        System.out.println("Практический блок:");
        System.out.println("------------------------------------------------------------");
        test.gauss();
        test.typeGauss();
        test.mehler();
    }

}


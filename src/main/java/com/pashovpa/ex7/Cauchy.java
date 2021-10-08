package com.pashovpa.ex7;

import java.util.Scanner;

public class Cauchy {
    final double x0 = 0;
    final double y0 = 1;
    double h;
    int N;
    double[] points;
    double[] values;
    double[] eulerOne;
    double[] eulerTwo;
    double[] eulerThree;
    double[] rengue;
    double[] adams;

    public Cauchy() {
        Scanner input = new Scanner(System.in);
        System.out.println("Введите параметры задачи:");
        System.out.print("h = ");
        this.h = input.nextDouble();
        System.out.print("N = ");
        this.N = input.nextInt();

        this.points = new double[this.N + 1];
        this.points[0] = x0;
        for (int i = 1; i <= this.N; i++) {
            this.points[i] = this.x0 + i * this.h;
        }

        this.values = new double[this.N + 1];
        this.values[0] = y0;
        for (int i = 1; i <= this.N; i++) {
            this.values[i] = y(this.points[i]);
        }
    }

    public void solve() {
        getEulerOne();
        getEulerTwo();
        getEulerThree();
        getRengue();
        getAdams();

        System.out.println("------------------------------------------------------------------------");
        System.out.println("Метод Эйлера 1:");
        for (int i = 0; i < this.N + 1; i++) {
            System.out.println("x" + i + " = " + points[i] + ", y" + i + " = " + eulerOne[i] + ", |yn - y(xn)| = " + Math.abs(eulerOne[i] - values[i]));
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Метод Эйлера 2:");
        for (int i = 0; i < this.N + 1; i++) {
            System.out.println("x" + i + " = " + points[i] + ", y" + i + " = " + eulerTwo[i] + ", |yn - y(xn)| = " + Math.abs(eulerTwo[i] - values[i]));
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Метод Эйлера 3:");
        for (int i = 0; i < this.N + 1; i++) {
            System.out.println("x" + i + " = " + points[i] + ", y" + i + " = " + eulerThree[i] + ", |yn - y(xn)| = " + Math.abs(eulerThree[i] - values[i]));
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Метод Рунге-Кутта:");
        for (int i = 0; i < this.N + 1; i++) {
            System.out.println("x" + i + " = " + points[i] + ", y" + i + " = " + rengue[i] + ", |yn - y(xn)| = " + Math.abs(rengue[i] - values[i]));
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Метод Адамса:");
        for (int i = 0; i < this.N + 1; i++) {
            System.out.println("x" + i + " = " + points[i] + ", y" + i + " = " + adams[i] + ", |yn - y(xn)| = " + Math.abs(adams[i] - values[i]));
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Свойдная таблица результатов для всех пяти методов:");
        System.out.println("    МЕТОД                  y" + this.N + "             АБСОЛЮТНАЯ ПОГРЕШНОСТЬ");
        System.out.println("Метод Эйлера 1     " + eulerOne[this.N] + "       " + Math.abs(eulerOne[this.N] - values[this.N]));
        System.out.println("Метод Эйлера 2     " + eulerTwo[this.N] + "        " + Math.abs(eulerTwo[this.N] - values[this.N]));
        System.out.println("Метод Эйлера 3     " + eulerThree[this.N] + "       " + Math.abs(eulerThree[this.N] - values[this.N]));
        System.out.println("Метод Рунге-Кутта  " + rengue[this.N] + "       " + Math.abs(rengue[this.N] - values[this.N]));
        System.out.println("Метод Адамса       " + adams[this.N] + "       " + Math.abs(adams[this.N] - values[this.N]));
        System.out.println("------------------------------------------------------------------------");
    }

    public void getEulerOne() {
        this.eulerOne = new double[this.N + 1];
        this.eulerOne[0] = this.y0;
        for (int i = 1; i <= this.N; i++) {
            this.eulerOne[i] = this.eulerOne[i - 1] + this.h * f(this.points[i - 1], this.eulerOne[i - 1]);
        }
    }

    public void getEulerTwo() {
        this.eulerTwo = new double[this.N + 1];
        this.eulerTwo[0] = this.y0;
        for (int i = 1; i <= this.N; i++) {
            this.eulerTwo[i] = this.eulerTwo[i - 1] + this.h * f(this.points[i - 1] + this.h / 2, this.eulerTwo[i - 1] + this.h / 2 * f(this.points[i - 1], this.eulerTwo[i - 1]));
        }
    }

    public void getEulerThree() {
        this.eulerThree = new double[this.N + 1];
        this.eulerThree[0] = this.y0;
        for (int i = 1; i <= this.N; i++) {
            this.eulerThree[i] = this.eulerThree[i - 1] + this.h / 2 * (f(this.points[i - 1], this.eulerThree[i - 1]) + f(this.points[i], this.eulerThree[i - 1] + this.h * f(this.points[i - 1], this.eulerThree[i - 1])));
        }
    }

    public void getRengue() {
        this.rengue = new double[this.N + 1];
        this.rengue[0] = this.y0;
        double k1, k2, k3, k4;
        for (int i = 1; i <= this.N; i++) {
            k1 = this.h * f(this.points[i - 1], this.rengue[i - 1]);
            k2 = this.h * f(this.points[i - 1] + this.h / 2, this.rengue[i - 1] + k1 / 2);
            k3 = this.h * f(this.points[i - 1] + this.h / 2, this.rengue[i - 1] + k2 / 2);
            k4 = this.h * f(this.points[i - 1] + this.h, this.rengue[i - 1] + k3);
            this.rengue[i] = this.rengue[i - 1] + (k1 + 2 * k2 + 2 * k3 + k4) / 6;
        }
    }

    public void getAdams() {
        this.adams = new double[this.N + 1];
        this.adams[0] = this.y0;

        double[] q = new double[this.N + 1];
        double[] diff1 = new double[this.N];
        double[] diff2 = new double[this.N - 1];
        double[] diff3 = new double[this.N - 2];
        double[] diff4 = new double[this.N - 3];

        for (int i = 1; i <= 4; i++) {
            this.adams[i] = y(this.points[i]);
        }
        for (int i = 0; i < 5; i++) {
            q[i] = this.h * f(this.points[i], this.adams[i]);
        }
        for (int i = 0; i < 4; i++) {
            diff1[i] = q[i + 1] - q[i];
        }
        for (int i = 0; i < 3; i++) {
            diff2[i] = diff1[i + 1] - diff1[i];
        }
        for (int i = 0; i < 2; i++) {
            diff3[i] = diff2[i + 1] - diff2[i];
        }
        for (int i = 0; i < 1; i++) {
            diff4[i] = diff3[i + 1] - diff3[i];
        }
        this.adams[5] = this.adams[4] + q[4] + diff1[3] / 2 + 5 * diff2[2] / 12 + 3 * diff3[1] / 8 + 251 * diff4[0] / 720;

        for (int i = 5; i < this.N; i++) {
            q[i] = this.h * f(this.points[i], this.adams[i]);
            diff1[i - 1] = q[i] - q[i - 1];
            diff2[i - 2] = diff1[i - 1] - diff1[i - 2];
            diff3[i - 3] = diff2[i - 2] - diff2[i - 3];
            diff3[i - 4] = diff2[i - 3] - diff2[i - 4];
            this.adams[i + 1] = this.adams[i] + q[i] + diff1[i - 1] / 2 + 5 * diff2[i - 2] / 12 + 3 * diff3[i - 3] / 8 + 251 * diff4[i - 4] / 720;
        }
    }

    double f(double X, double Y) {
        return -1 * Y * Y;
    }

    double y(double X) {
        return 1 / (1 + X);
    }

    public static void main(String[] args) {
        System.out.println("Задание 7. Численное решение Задачи Коши для обыкновенного дифференциального уравнения первого порядка.");
        System.out.println("Исходное задача Коши: y'(x) = -y(x)^2, y(0) = 1");
        System.out.println("Точное решение задачи Коши: y(x) = 1 / (1 + x)");
        while (true) {
            Cauchy test = new Cauchy();
            test.solve();
        }
    }
}

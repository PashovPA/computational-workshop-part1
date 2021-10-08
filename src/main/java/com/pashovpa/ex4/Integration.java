package com.pashovpa.ex4;

import java.util.Scanner;

public class Integration {
    double a;
    double b;
    double h;
    double w;
    double q;
    int N;

    public double f(double X) {
//	  return 11;
//		return 11 * X - 10;
//		return 11 * X * X - 10 * X  + 9;
        return Math.exp(X);
    }

    public double F(double X) {
//		return 11 * X;
//		return 11 * X * X / 2 - 10 * X;
//		return 11 * X * X * X / 3 - 10 * X * X / 2 + 9 * X;
        return Math.exp(X);

    }

    public double p(double X) {
        return 1;
    }

    public Integration() {
        Scanner input = new Scanner(System.in);

        System.out.print("Введите начало отрезка интегрирования: a = ");
        this.a = input.nextDouble();

        System.out.print("Введите конец отрезка интегрирования: b = ");
        this.b = input.nextDouble();

        System.out.print("Введите число промежутков деления [" + this.a + ", " + this.b + "]: N = ");
        int N = input.nextInt();
        while (N <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            N = input.nextInt();
        }
        this.N = N;
        this.h = (this.b - this.a) / this.N;
        System.out.println("Шаг деления: h = " + this.h);

        double w = 0;
        double q = 0;
        for (int k = 1; k < N; k++) {
            w += f(this.a + k * this.h);
            q += f((this.a + k * this.h) + this.h / 2);
        }
        this.w = w;
        this.q = q + f(this.a + this.h / 2);
    }

    public void findIntegral() {
        double J = F(this.b) - F(this.a);
        System.out.println("Точное значение интеграла: J = " + J);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: ЛЕВЫХ ПРЯМОУГОЛЬНИКОВ");
        double leftJ = leftRectangle();
        System.out.println("Значение: J(h) = " + leftJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - leftJ));
        double leftError = 0.5 * Math.exp(this.b) * (this.b - this.a) * this.h;
        System.out.println("Теоретическая погрешность: " + leftError);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СРЕДНИХ ПРЯМОУГОЛЬНИКОВ");
        double middleJ = middleRectangle();
        System.out.println("Значение: J(h) = " + middleJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - middleJ));
        double middleError = Math.exp(this.b) * (this.b - this.a) * this.h * this.h / 24;
        System.out.println("Теоретическая погрешность: " + middleError);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: ПРАВЫХ ПРЯМОУГОЛЬНИКОВ");
        double rightJ = rightRectangle();
        System.out.println("Значение: J(h) = " + rightJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - rightJ));
        double rightError = 0.5 * Math.exp(this.b) * (this.b - this.a) * this.h;
        System.out.println("Теоретическая погрешность: " + rightError);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: ТРАПЕЦИЙ");
        double trapezeJ = trapeze();
        System.out.println("Значение: J(h) = " + trapezeJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - trapezeJ));
        double trapezeError = Math.exp(this.b) * (this.b - this.a) * this.h * this.h / 12;
        System.out.println("Теоретическая погрешность: " + trapezeError);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СИМПСОНА");
        double simpsonJ = simpson();
        System.out.println("Значение: J(h) = " + simpsonJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - simpsonJ));
        double simpsonError = Math.exp(this.b) * (this.b - this.a) * this.h * this.h * this.h * this.h / 2880;
        System.out.println("Теоретическая погрешность: " + simpsonError);

        System.out.println("#########################################################################");
    }

    public double leftRectangle() {
        return this.h * (f(this.a) + this.w);
    }

    public double middleRectangle() {
        return this.h * this.q;
    }

    public double rightRectangle() {
        return this.h * (this.w + f(this.b));
    }

    public double trapeze() {
        return (this.h / 2) * (f(this.a) + 2 * this.w + f(this.b));
    }

    public double simpson() {
        return (this.h / 6) * (f(this.a) + 4 * this.q + 2 * this.w + f(this.b));
    }

    public static void main(String[] args) {
        System.out.println("Задание №4: Приближённое вычисление интеграла по составным квадратурным формулам");
        while (true) {
            Integration test = new Integration();
            test.findIntegral();
        }
    }
}

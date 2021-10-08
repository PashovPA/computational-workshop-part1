package com.pashovpa.ex4;

import java.util.Scanner;

public class ExactIntegration {
    double a;
    double b;
    double h;
    double w;
    double q;
    int N;
    int l;

    public double f(double X) {
        return Math.exp(X);
    }

    public double F(double X) {
        return Math.exp(X);
    }

    public double p(double X) {
        return 1;
    }

    public ExactIntegration() {
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

    public double[] findIntegral() {
        double J = F(this.b) - F(this.a);
        System.out.println("Точное значение интеграла: J = " + J);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СРЕДНИХ ПРЯМОУГОЛЬНИКОВ");
        double middleJ = middleRectangle();
        System.out.println("Значение: J(h) = " + middleJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - middleJ));

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: ТРАПЕЦИЙ");
        double trapezeJ = trapeze();
        System.out.println("Значение: J(h) = " + trapezeJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - trapezeJ));

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СИМПСОНА");
        double simpsonJ = simpson();
        System.out.println("Значение: J(h) = " + simpsonJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - simpsonJ));

        System.out.println("#########################################################################");

        double[] values = new double[3];
        values[0] = middleJ;
        values[1] = trapezeJ;
        values[2] = simpsonJ;

        return values;
    }

    public double[] findExactIntegral() {
        Scanner input = new Scanner(System.in);
        System.out.print("Введите коэффициент, на который необходимо домножить N = " + this.N + ": l = ");
        int l = input.nextInt();
        while (l <= 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            l = input.nextInt();
        }
        this.l = l;
        this.h = this.h / this.l;
        double w = 0;
        double q = 0;
        for (int k = 1; k < this.N * this.l; k++) {
            w += f(this.a + k * this.h);
            q += f((this.a + k * this.h) + this.h / 2);
        }
        this.w = w;
        this.q = q + f(this.a + this.h / 2);

        double J = F(this.b) - F(this.a);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СРЕДНИХ ПРЯМОУГОЛЬНИКОВ");
        double middleJ = middleRectangle();
        System.out.println("Значение: J(h/l) = " + middleJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - middleJ));

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: ТРАПЕЦИЙ");
        double trapezeJ = trapeze();
        System.out.println("Значение: J(h/l) = " + trapezeJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - trapezeJ));

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СИМПСОНА");
        double simpsonJ = simpson();
        System.out.println("Значение: J(h/l) = " + simpsonJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J(h)| = " + Math.abs(J - simpsonJ));

        System.out.println("#########################################################################");

        double[] values = new double[3];
        values[0] = middleJ;
        values[1] = trapezeJ;
        values[2] = simpsonJ;

        return values;
    }

    public void runge(double[] Jh, double[] Jhl) {
        double J = F(this.b) - F(this.a);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СРЕДНИХ ПРЯМОУГОЛЬНИКОВ");
        double middleJ = (this.l * this.l * Jhl[0] - Jh[0]) / (this.l * this.l - 1);
        System.out.println("Уточнённое по принципу Рунге значение J' = " + middleJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J'| = " + Math.abs(J - middleJ));

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: ТРАПЕЦИЙ");
        double trapezeJ = (this.l * this.l * Jhl[1] - Jh[1]) / (this.l * this.l - 1);
        System.out.println("Уточнённое по принципу Рунге значение J' = " + trapezeJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J'| = " + Math.abs(J - trapezeJ));

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Квадратурная формула: СИМПСОНА");
        double simpsonJ = (this.l * this.l * this.l * this.l * Jhl[2] - Jh[2]) / (this.l * this.l * this.l * this.l - 1);
        System.out.println("Уточнённое по принципу Рунге значение J' = " + simpsonJ);
        System.out.println("Абсолютная фактическая погрешность: |J - J'| = " + Math.abs(J - simpsonJ));

        System.out.println("#########################################################################");
    }

    public double middleRectangle() {
        return this.h * this.q;
    }

    public double trapeze() {
        return (this.h / 2) * (f(this.a) + 2 * this.w + f(this.b));
    }

    public double simpson() {
        return (this.h / 6) * (f(this.a) + 4 * this.q + 2 * this.w + f(this.b));
    }

    public static void main(String[] args) {
        System.out.println("Задание №4.2: УТОЧНЕНИЕ ПРИБЛИЖЁННОГО ЗНАЧЕНИЯ ИНТЕГРАЛА ПО ПРИНЦИПУ РУНГЕ");
        while (true) {
            ExactIntegration test = new ExactIntegration();
            test.runge(test.findIntegral(), test.findExactIntegral());
        }
    }
}

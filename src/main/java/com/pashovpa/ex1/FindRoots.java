package com.pashovpa.ex1;

import java.util.ArrayList;

public class FindRoots {
    double A;
    double B;
    double eps;
    String EXPRESSION;

    public FindRoots() {
        this.A = -3;
        this.B = 7;
        this.eps = 0.00001;
        this.EXPRESSION = "2 * x^2 - 2^x - 5";
    }

    public double f(double X) {
        return 2 * X * X - Math.pow(2, X) - 5;
    }

    public ArrayList<Double> separation(int N) {
        ArrayList<Double> points = new ArrayList<>();
        double step = (this.B - this.A) / N;
        double X1 = this.A;
        double X2 = X1 + step;
        double Y1 = f(X1);
        double Y2;

        while (X2 <= this.B) {
            Y2 = f(X2);
            if (Y1 * Y2 <= 0) {
                points.add(X1);
                points.add(X2);
                System.out.println("[" + X1 + "," + X2 + "]");
            }
            X1 = X2;
            X2 = X1 + step;
            Y1 = Y2;
        }

        System.out.println("Количество отрезков: " + points.size() / 2);
        System.out.println("\n");

        return points;
    }

    public void bisection(ArrayList<Double> segments) {
        int size = segments.size();
        for (int i = 0; i < size; i += 2) {
            double a = segments.get(i);
            double b = segments.get(i + 1);
            double c, fa, fc, X, delta;
            int count = 0;

            System.out.println("Название метода: метод бисекции");
            System.out.println("Начальные приближения к корню: " + a + "," + b);

            while (b - a > 2 * this.eps) {
                c = (a + b) / 2;
                fa = f(a);
                fc = f(c);
                count++;

                if (fa * fc <= 0) {
                    b = c;
                } else {
                    a = c;
                }
            }
            X = (a + b) / 2;
            delta = (b - a) / 2;

            System.out.println("Количество шагов: " + count);
            System.out.println("Приближенное решение: " + X);
            System.out.println("Длина последнего отрезка: " + delta);
            System.out.println("Абсолютная величина невязки: " + Math.abs(f(X) - 0));
            System.out.println("\n");
        }
        System.out.println("\n\n");
    }

    public double df(double X) {
        return 4 * X - Math.pow(2, X) * Math.log(2);
    }

    public void newton(ArrayList<Double> segments) {
        int size = segments.size();
        for (int i = 0; i < size; i += 2) {
            double a = segments.get(i);
            double b = segments.get(i + 1);
            double x0 = (a + b) / 2;
            double prev = x0;
            double next = x0 - f(x0) / df(x0);
            int count = 1;

            System.out.println("Название метода: метод Ньютона");
            System.out.println("Начальные приближения к корню: " + a + "," + b);

            while (Math.abs(next - prev) > this.eps) {
                prev = next;
                next = prev - f(prev) / df(prev);
                count++;
            }

            System.out.println("Количество шагов: " + count);
            System.out.println("Приближенное решение: " + next);
            System.out.println("Абсолютная величина невязки: " + Math.abs(f(next) - 0));
            System.out.println("\n");
        }
        System.out.println("\n\n");
    }

    public void modnewton(ArrayList<Double> segments) {
        int size = segments.size();
        for (int i = 0; i < size; i += 2) {
            double a = segments.get(i);
            double b = segments.get(i + 1);
            double x0 = (a + b) / 2;
            double prev = x0;
            double next = x0 - f(x0) / df(x0);
            double dfx0 = df(x0);
            int count = 1;

            System.out.println("Название метода: модифицированный метод Ньютона");
            System.out.println("Начальные приближения к корню: " + a + "," + b);

            while (Math.abs(next - prev) > this.eps) {
                prev = next;
                next = prev - f(prev) / dfx0;
                count++;
            }

            System.out.println("Количество шагов: " + count);
            System.out.println("Приближенное решение: " + next);
            System.out.println("Абсолютная величина невязки: " + Math.abs(f(next) - 0));
            System.out.println("\n");
        }
        System.out.println("\n\n");
    }

    public void cross(ArrayList<Double> segments) {
        int size = segments.size();
        for (int i = 0; i < size; i += 2) {
            double a = segments.get(i);
            double b = segments.get(i + 1);
            double x0 = (a + b) / 2;
            double x1 = x0 - f(x0) / df(x0), x2 = x1 - f(x1) / df(x1);
            int count = 2;

            System.out.println("Название метода: метод секущих");
            System.out.println("Начальные приближения к корню: " + a + "," + b);

            while (Math.abs(x2 - x1) > this.eps) {
                x0 = x1;
                x1 = x2;
                x2 = x1 - f(x1) * (x1 - x0) / (f(x1) - f(x0));
                count++;
            }

            System.out.println("Количество шагов: " + count);
            System.out.println("Приближенное решение: " + x2);
            System.out.println("Абсолютная величина невязки: " + Math.abs(f(x2) - 0));
            System.out.println("\n");
        }
        System.out.println("\n\n");
    }

    public static void main(String[] args) {
        FindRoots test = new FindRoots();
        System.out.println("\nЗадание №1: ЧИСЛЕННЫЕ МЕТОДЫ РЕШЕНИЯ НЕЛИНЕЙНЫХ УРАВНЕНИЙ");
        System.out.println("Тестовая задача:\nf(x) = " + test.EXPRESSION);
        System.out.println("A = " + test.A + ", B = " + test.B + ", eps = " + test.eps + "\n");

        System.out.println("Отделение корней способом табулирования: ");
        ArrayList<Double> segments = test.separation(100);

        System.out.println("###### Метод бисекции ######\n");
        test.bisection(segments);

        System.out.println("###### Метод Ньютона ######\n");
        test.newton(segments);

        System.out.println("###### Модифицированный метод Ньютона ######\n");
        test.modnewton(segments);

        System.out.println("###### Метод секущих ######\n");
        test.cross(segments);
    }
}

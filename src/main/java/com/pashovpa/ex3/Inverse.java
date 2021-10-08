package com.pashovpa.ex3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Inverse {
    double a;
    double b;
    int number;
    ArrayList<Double> arguments;
    ArrayList<Double> values;
    ArrayList<Double> inverseArguments;
    ArrayList<Double> inverseValues;
    String EXPRESSION;
    double[][] newtonOdds;
    double valueOfPoint;
    double eps;

    public double f(double x) {
        return Math.sin(x) + x * x / 2;
    }

    public Inverse(double a, double b) {
        this.a = a;
        this.b = b;
        this.EXPRESSION = "f(x) = sin(x) + x^2/2";

        ArrayList<Double> inputArguments = new ArrayList<>();
        ArrayList<Double> inputValues = new ArrayList<>();

        Scanner input = new Scanner(System.in);
        System.out.print("Введите число значений в таблице(m+1): ");
        int number = input.nextInt();
        while (number < 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            number = input.nextInt();
        }
        this.number = number;

        System.out.println("Исходная таблично-заданная функция f: ");
        System.out.println("-----------------------------------------------------");
        double step = (this.b - this.a) / (number - 1);
        for (int i = 0; i < number; i++) {
            inputArguments.add(this.a + step * i);
            inputValues.add(f(inputArguments.get(i)));
            System.out.println("x" + i + " = " + inputArguments.get(i) + " : f(x" + i + ") = " + inputValues.get(i));
        }
        System.out.println("-----------------------------------------------------");

        this.arguments = inputArguments;
        this.values = inputValues;
        this.inverseArguments = inputValues;
        this.inverseValues = inputArguments;
    }

    public void findArgument() {
        Scanner input = new Scanner(System.in);

        System.out.print("Введите значение функции, для которого ищем значение аргумента: ");
        this.valueOfPoint = input.nextDouble();

        ArrayList<Double> sortInverseArguments = sort();
        ArrayList<Double> sortInverseValues = this.inverseValues;
        System.out.println("Отсортированная по близости к " + this.valueOfPoint + " таблица обратной функции f^(-1): ");
        System.out.println("-----------------------------------------------------");
        for (int i = 0; i < sortInverseArguments.size(); i++) {
            System.out.println("x" + i + " = " + sortInverseArguments.get(i) + " : f(x" + i + ") = " + sortInverseValues.get(i));
        }
        System.out.println("-----------------------------------------------------");

        fistMethod();
        secondMethod();
    }

    public ArrayList<Double> sort() {
        double tempArgument, tempValue;
        for (int i = 0; i < this.inverseArguments.size(); i++) {
            for (int j = 0; j < this.inverseArguments.size(); j++) {
                if (Math.abs(this.valueOfPoint - this.inverseArguments.get(j)) > Math.abs(this.valueOfPoint - this.inverseArguments.get(i))) {
                    tempArgument = this.inverseArguments.get(j);
                    this.inverseArguments.set(j, this.inverseArguments.get(i));
                    this.inverseArguments.set(i, tempArgument);

                    tempValue = this.inverseValues.get(j);
                    this.inverseValues.set(j, this.inverseValues.get(i));
                    this.inverseValues.set(i, tempValue);
                }
            }
        }
        return this.inverseArguments;
    }

    public void fistMethod() {
        System.out.print("Введите степень интерполяционного многочлена для 1 способа (не выше " + (this.number - 1) + "): ");
        Scanner input = new Scanner(System.in);
        int firstDegree = input.nextInt();
        while (firstDegree < 0 || firstDegree > this.number - 1) {
            System.out.print("Недопустимое значение! Повторите ввод: ");
            firstDegree = input.nextInt();
        }

        System.out.println("-----------------------------------------------------");
        System.out.println("Решение задачи обратного интерполирования 1 СПОСОБОМ:");
        double firstSolution = lagrange(firstDegree);
        System.out.println("Параметры: значение функции(F) = " + this.valueOfPoint + ", степень интерполяционного многочлена(n1) = " + firstDegree);
        System.out.println("Решение: X = " + firstSolution);
        System.out.println("Модуль невязки: " + Math.abs(f(firstSolution) - this.valueOfPoint));
        System.out.println("-----------------------------------------------------");
    }

    public double lagrange(int degree) {
        double value = 0;
        double l1, l2;
        for (int i = 0; i <= degree; i++) {
            l1 = 1;
            l2 = 1;
            for (int j = 0; j <= degree; j++) {
                if (i != j) {
                    l1 *= (this.valueOfPoint - this.inverseArguments.get(j));
                    l2 *= (this.inverseArguments.get(i) - this.inverseArguments.get(j));
                }
            }
            value += (this.inverseValues.get(i) * l1 / l2);
        }
        return value;
    }

    public void secondMethod() {
        System.out.print("Введите степень интерполяционного многочлена для 2 способа (не выше " + (this.number - 1) + "): ");
        Scanner input = new Scanner(System.in);
        int secondDegree = input.nextInt();
        while (secondDegree < 0 || secondDegree > this.number - 1) {
            System.out.print("Недопустимое значение! Повторите ввод: ");
            secondDegree = input.nextInt();
        }
        System.out.print("Введите точность(eps), с которой находится корень алгебраического уравнения во 2 способе: ");
        this.eps = input.nextDouble();
        while (this.eps < 0) {
            System.out.print("Недопустимое значение! Повторите ввод: ");
            this.eps = input.nextDouble();
        }

        System.out.println("-----------------------------------------------------");
        System.out.println("Решение задачи обратного интерполирования 2 СПОСОБОМ:");
        ArrayList<Double> interRoots = interRoots(secondDegree);
        System.out.println("Параметры: значение функции(F) = " + this.valueOfPoint + ", степень интерполяционного многочлена(n2) = " + secondDegree + ", точность(eps) = " + eps);
        for (int i = 0; i < interRoots.size(); i++) {
            System.out.println("Решение " + (i + 1) + ": X" + (i + 1) + "= " + interRoots.get(i));
            System.out.println("Модуль невязки: " + Math.abs(f(interRoots.get(i)) - this.valueOfPoint));
        }

        System.out.println("-----------------------------------------------------");
        System.out.println("#####################################################");
    }

    public ArrayList<Double> interRoots(int degree) {
        newtonOdds(degree);
        return bisection(separation(10));
    }

    public ArrayList<Double> separation(int N) {
        ArrayList<Double> points = new ArrayList<>();
        double step = (this.b - this.a) / N;
        double X1 = this.a;
        double X2 = X1 + step;
        double Y1 = fPoly(X1);
        double Y2;

        while (X2 <= this.b) {
            Y2 = fPoly(X2);
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

    public ArrayList<Double> bisection(ArrayList<Double> segments) {
        int size = segments.size();
        ArrayList<Double> roots = new ArrayList<>();
        for (int i = 0; i < size; i += 2) {
            double a = segments.get(i);
            double b = segments.get(i + 1);
            double c, fa, fc;

            while (b - a > 2 * this.eps) {
                c = (a + b) / 2;
                fa = fPoly(a);
                fc = fPoly(c);

                if (fa * fc <= 0) {
                    b = c;
                } else {
                    a = c;
                }
            }
            roots.add((a + b) / 2);
        }
        return roots;
    }

    public void newtonOdds(int degree) {
        double[][] separateDiff = new double[degree + 1][degree + 1];
        for (int i = 0; i <= degree; i++) {
            Arrays.fill(separateDiff[i], 0);
        }

        for (int i = 0; i <= degree; i++) {
            separateDiff[i][0] = this.values.get(i);
        }

        for (int i = 1; i <= degree; i++) {
            for (int j = 0; j <= degree - i; j++) {
                separateDiff[j][i] = (separateDiff[j + 1][i - 1] - separateDiff[j][i - 1]) / (this.arguments.get(j + i) - this.arguments.get(j));
            }
        }

        this.newtonOdds = separateDiff;
    }

    public double fPoly(double X) {
        double value = this.newtonOdds[0][0];
        double difference = 1;
        for (int i = 1; i < this.newtonOdds[0].length; i++) {
            difference *= (X - this.arguments.get(i - 1));
            value += this.newtonOdds[0][i] * difference;
        }
        return value - this.valueOfPoint;
    }

    public static void main(String[] args) {
        System.out.println("Задание №3.1: ЗАДАЧА ОБРАТНОГО ИНТЕРПОЛИРОВАНИЯ");
        Scanner mainInput = new Scanner(System.in);
        double a, b;
        System.out.print("Введите значение левой границы отрезка а = ");
        a = mainInput.nextDouble();
        System.out.print("Введите значение правой границы отрезка b = ");
        b = mainInput.nextDouble();
        Inverse test = new Inverse(a, b);
        while (true) {
            test.findArgument();
        }
    }
}

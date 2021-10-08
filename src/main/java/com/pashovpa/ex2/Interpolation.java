package com.pashovpa.ex2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Interpolation {
    double a;
    double b;
    int number;
    ArrayList<Double> arguments;
    ArrayList<Double> values;
    String EXPRESSION;

    public Interpolation(double a, double b) {
        this.a = a;
        this.b = b;
        this.EXPRESSION = "f(x) = sin(x) + x^2/2";

        ArrayList<Double> inputArguments = new ArrayList<>();
        ArrayList<Double> inputValues = new ArrayList<>();

        Scanner input = new Scanner(System.in);
        System.out.print("Введите число значений в таблице: ");
        int number = input.nextInt();
        while (number < 0) {
            System.out.println("Недопустимое значение! Повторите ввод: ");
            number = input.nextInt();
        }
        this.number = number;

        System.out.println("Исходная таблично-заданная функция: ");
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
    }

    public double f(double x) {
        return Math.sin(x) + x * x / 2;
//    return Math.pow(x, 4) - 6 * x * x + 25;
    }


    public void findValue() {
        Scanner input = new Scanner(System.in);

        System.out.print("Введите точку интерполирования: ");
        double point = input.nextDouble();

        ArrayList<Double> sortArguments = sort(point);
        ArrayList<Double> sortValues = this.values;
        System.out.println("Отсортированная по близости к " + point + " таблица функции: ");
        System.out.println("-----------------------------------------------------");
        for (int i = 0; i < sortArguments.size(); i++) {
            System.out.println("x" + i + " = " + sortArguments.get(i) + " : f(x" + i + ") = " + sortValues.get(i));
        }
        System.out.println("-----------------------------------------------------");

        System.out.print("Введите степень интерполяционного многочлена (не выше " + (this.number - 1) + "): ");
        int degree = input.nextInt();
        while (degree < 0 || degree > this.number - 1) {
            System.out.print("Недопустимое значение! Повторите ввод: ");
            degree = input.nextInt();
        }

        System.out.println("-----------------------------------------------------");
        double lagrangeValue = lagrange(point, degree);
        System.out.println("Значение Pn(x), найденное при помощи представления в форме Лагранжа: " + lagrangeValue);
        System.out.println("Значение абсолютной фактической погрешности для формы Ньютона: " + Math.abs(f(point) - lagrangeValue));

        System.out.println("-----------------------------------------------------");
        double newtonValue = newton(point, degree);
        System.out.println("Значение Pn(x), найденное при помощи представления в форме Ньютона: " + newtonValue);
        System.out.println("Значение абсолютной фактической погрешности для формы Ньютона: " + Math.abs(f(point) - newtonValue));
        System.out.println("-----------------------------------------------------");

        System.out.println("#####################################################");
    }

    public double lagrange(double point, double degree) {
        double value = 0;
        double l1, l2;
        for (int i = 0; i <= degree; i++) {
            l1 = 1;
            l2 = 1;
            for (int j = 0; j <= degree; j++) {
                if (i != j) {
                    l1 *= (point - this.arguments.get(j));
                    l2 *= (this.arguments.get(i) - this.arguments.get(j));
                }
            }
            value += (this.values.get(i) * l1 / l2);
        }
        return value;
    }

    public double newton(double point, int degree) {
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

        double value = separateDiff[0][0];
        double difference = 1;
        for (int i = 1; i <= degree; i++) {
            difference *= (point - this.arguments.get(i - 1));
            value += separateDiff[0][i] * difference;
        }
        return value;
    }

    public ArrayList<Double> sort(double point) {
        double tempArgument, tempValue;
        for (int i = 0; i < this.arguments.size(); i++) {
            for (int j = 0; j < this.arguments.size(); j++) {
                if (Math.abs(point - this.arguments.get(j)) > Math.abs(point - this.arguments.get(i))) {
                    tempArgument = this.arguments.get(j);
                    this.arguments.set(j, this.arguments.get(i));
                    this.arguments.set(i, tempArgument);

                    tempValue = this.values.get(j);
                    this.values.set(j, this.values.get(i));
                    this.values.set(i, tempValue);
                }
            }
        }
        return this.arguments;
    }

    public static void main(String[] args) {
        System.out.println("Задание №2: ЗАДАЧА АЛГЕБРАИЧЕСКОГО ИНТЕРПОЛИРОВАНИЯ");
        System.out.println("Тестовая задача: Вариант 11");
        System.out.println("a = 0.4, b = 0.9, x = 0.75, n = 7, m = 15\n");
        Interpolation test = new Interpolation(0.4, 0.9);
        while (true) {
            test.findValue();
        }
    }
}



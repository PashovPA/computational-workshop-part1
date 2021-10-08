package com.pashovpa.ex3;

import java.util.ArrayList;
import java.util.Scanner;

public class Differentiation {
    double a;
    double b;
    double step;
    int number;
    ArrayList<Double> arguments;
    ArrayList<Double> values;
    String EXPRESSION;

    public Differentiation() {
        ArrayList<Double> inputArguments = new ArrayList<>();
        ArrayList<Double> inputValues = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        System.out.print("Введите первый узел разбиения a = ");
        this.a = input.nextDouble();

        System.out.print("Введите шаг разбиения h = ");
        this.step = input.nextDouble();
        while (this.step <= 0) {
            System.out.print("Недопустимое значение! Повторите ввод: ");
            this.step = input.nextDouble();
        }

        System.out.print("Введите число значений в таблице (m + 1) = ");
        this.number = input.nextInt();
        while (this.number < 3) {
            System.out.print("Недопустимое значение! Повторите ввод: ");
            this.number = input.nextInt();
        }

        System.out.println("Исходная таблично-заданная функция f: ");
        System.out.println("-----------------------------------------------------");
        for (int i = 0; i < this.number; i++) {
            inputArguments.add(this.a + this.step * i);
            inputValues.add(f(inputArguments.get(i)));
            System.out.println("x" + i + " = " + inputArguments.get(i) + " : f(x" + i + ") = " + inputValues.get(i));
        }
        System.out.println("-----------------------------------------------------");

        this.arguments = inputArguments;
        this.values = inputValues;
        this.b = inputArguments.get(number - 1);
        this.EXPRESSION = "f(x) = exp(3 * x)";
    }

    public double f(double X) {
        return Math.exp(3 * X);
    }

    public void findDerivatives() {
        System.out.println("        xi                     f(xi)                 f'(xi)ЧД         | f'(xi)Т - f'(xi)ЧД |         f''(xi)ЧД        | f''(xi)Т - f''(xi)ЧД |");
        for (int i = 0; i < this.arguments.size(); i++) {
            double df = df(i);
            double ddf = ddf(i);
            System.out.printf("%18.16f      %18.16f      %18.16f      %18.16f      %18.16f      %18.16f", this.arguments.get(i), this.values.get(i), df, Math.abs(3 * this.values.get(i) - df), ddf, Math.abs(9 * this.values.get(i) - ddf));
            System.out.println();
        }
    }

    public double df(int index) {
        if (index == 0) {
            return (-3 * this.values.get(index) + 4 * this.values.get(index + 1) - this.values.get(index + 2)) / (2 * this.step);
        } else if (index == this.number - 1) {
            return (3 * this.values.get(index) - 4 * this.values.get(index - 1) + this.values.get(index - 2)) / (2 * this.step);
        }
        return (this.values.get(index + 1) - this.values.get(index - 1)) / (2 * this.step);
    }

    public double ddf(int index) {
        if (index == 0 || index == this.number - 1) {
            return 0.0 / 0.0;
        }
        return (this.values.get(index + 1) - 2 * this.values.get(index) + this.values.get(index - 1)) / (this.step * this.step);
    }

    public static void main(String[] args) {
        System.out.println("Задание №3.2: НАХОЖДЕНИЕ ПРОИЗВОДНЫХ ТАБЛИЧНО-ЗАДАННОЙ ФУНКЦИИ ПО ФОРМУЛАМ ЧИСЛЕННОГО ДИФФЕРЕНЦИРОВАНИЯ");
        System.out.println("Рассматриваемая функция: f(x) = exp(3x)");
        Differentiation test = new Differentiation();
        test.findDerivatives();
    }
}


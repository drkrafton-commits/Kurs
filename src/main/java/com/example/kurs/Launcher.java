package com.example.kurs;

public class Launcher {
    public static void main(String[] args) {
        // Добавляем опции для Java 25
        System.setProperty("prism.verbose", "true");

        // Запускаем основное приложение
        HelloApplication.main(args);
    }
}
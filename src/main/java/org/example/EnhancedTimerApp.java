package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class EnhancedTimerApp {

    private static Timer timer;
    private static TimerTask task;
    private static long remainingTime;
    private static boolean isPaused = false;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Таймер уведомлений");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridLayout(5, 1));

        JPanel inputPanel = new JPanel();
        JLabel hoursLabel = new JLabel("Часы:");
        JTextField hoursField = new JTextField(2);
        JLabel minutesLabel = new JLabel("Минуты:");
        JTextField minutesField = new JTextField(2);
        JLabel secondsLabel = new JLabel("Секунды:");
        JTextField secondsField = new JTextField(2);

        inputPanel.add(hoursLabel);
        inputPanel.add(hoursField);
        inputPanel.add(minutesLabel);
        inputPanel.add(minutesField);
        inputPanel.add(secondsLabel);
        inputPanel.add(secondsField);

        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Запустить");
        JButton pauseButton = new JButton("Пауза");
        JButton resetButton = new JButton("Сброс");

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resetButton);

        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);

        startButton.addActionListener(e -> {
            try {
                int hours = Integer.parseInt(hoursField.getText().isEmpty() ? "0" : hoursField.getText());
                int minutes = Integer.parseInt(minutesField.getText().isEmpty() ? "0" : minutesField.getText());
                int seconds = Integer.parseInt(secondsField.getText().isEmpty() ? "0" : secondsField.getText());

                long totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000L;

                if (totalMilliseconds <= 0) {
                    JOptionPane.showMessageDialog(frame, "Введите положительное время.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (timer != null) timer.cancel();
                startTimer(totalMilliseconds, statusLabel, frame);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите корректное число.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        pauseButton.addActionListener(e -> {
            if (isPaused) {
                isPaused = false;
                startTimer(remainingTime, statusLabel, frame);
                statusLabel.setText("Таймер возобновлен.");
            } else {
                isPaused = true;
                if (timer != null) timer.cancel();
                statusLabel.setText("Таймер на паузе.");
            }
        });

        resetButton.addActionListener(e -> {
            if (timer != null) timer.cancel();
            remainingTime = 0;
            isPaused = false;
            statusLabel.setText("Таймер сброшен.");
        });

        frame.add(inputPanel);
        frame.add(buttonPanel);
        frame.add(statusLabel);

        frame.setVisible(true);
    }

    private static void startTimer(long milliseconds, JLabel statusLabel, JFrame frame) {
        remainingTime = milliseconds;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime -= 1000;
                    long hours = (remainingTime / 3600000);
                    long minutes = (remainingTime / 60000) % 60;
                    long seconds = (remainingTime / 1000) % 60;
                    statusLabel.setText(String.format("Оставшееся время: %02d:%02d:%02d", hours, minutes, seconds));
                } else {
                    timer.cancel();
                    JOptionPane.showMessageDialog(frame, "Время вышло!", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("Таймер завершён.");
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}

package com.mycompany.clientapp;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

class ClockPane extends Pane {
        private int hour;
        private int minute;

        private double w = 350, h = 350;

        public ClockPane() {
            this.hour = 9;
            this.minute = 0;
            paintClock();
        }

        public ClockPane(int h, int m){
            this.hour = h;
            this.minute = m;
            paintClock();
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
            paintClock();
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
            paintClock();
        }

        public double getW() {
            return w;
        }

        public void setW(double w) {
            this.w = w;
            paintClock();
        }

        public double getH() {
            return h;
        }

        public void setH(double h) {
            this.h = h;
            paintClock();
        }

        private void paintClock() {
            double clockRadius = Math.min(w, h) * 0.8 * 0.5;
            double centerX = w / 2;
            double centerY = h / 2;

            Circle circle = new Circle(centerX, centerY, clockRadius);
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);
            Text t1 = new Text(centerX - 5, centerY - clockRadius + 12, "12");
            Text t3 = new Text(centerX + clockRadius - 10, centerY + 3, "3");
            Text t6 = new Text(centerX - 3, centerY + clockRadius - 3, "6");
            Text t9 = new Text(centerX - clockRadius + 3, centerY + 5, "9");

            double mLength = clockRadius * 0.65;
            double xMinute = centerX + mLength
                * Math.sin(minute * (2 * Math.PI / 60));
            double minuteY = centerY - mLength
                * Math.cos(minute * (2 * Math.PI / 60));
            Line mLine = new Line(centerX, centerY, xMinute, minuteY);
            mLine.setStroke(Color.BLUE);

            double hLength = clockRadius * 0.5;
            double hourX = centerX + hLength
                * Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
            double hourY = centerY - hLength
                * Math.cos((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
            Line hLine = new Line(centerX, centerY, hourX, hourY);
            hLine.setStroke(Color.GREEN);

            getChildren().clear();
            getChildren().addAll(circle, t1,t3, t6, t9, mLine, hLine);
        }
    }

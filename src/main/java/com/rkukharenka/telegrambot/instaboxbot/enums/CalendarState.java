package com.rkukharenka.telegrambot.instaboxbot.enums;

public enum CalendarState {

    JANUARY("Январь", 1),
    FEBRUARY("Февраль", 2),
    MARCH("Март", 3),
    APRIL("Апрель", 4),
    MAY("Май", 5),
    JUNE("Июнь", 6),
    JULY("Июль", 7),
    AUGUST("Август", 8),
    SEPTEMBER("Сентябрь", 9),
    OCTOBER("Октябрь", 10),
    NOVEMBER("Ноябрь", 11),
    DECEMBER("Декабрь", 12);

    private final String russianName;
    private final int number;

    CalendarState(String russianName, int number) {
        this.russianName = russianName;
        this.number = number;
    }

    public String getRussianName() {
        return russianName;
    }

    public int getNumber() {
        return number;
    }

}

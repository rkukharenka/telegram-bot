package com.rkukharenka.telegrambot.instaboxbot.bot.helper;

import com.rkukharenka.telegrambot.instaboxbot.common.constant.CallbackDataConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.CalendarState;
import com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeyboardFactory {

    private static final String EMPTY_STRING = " ";

    public static ReplyKeyboard getCalendar(YearMonth yearMonth, Set<LocalDate> availableDays) {
        InlineKeyboardMarkup monthMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();

        fillControlCalendarButtons(buttonRows, yearMonth);

        fillDaysOfWeekButtons(buttonRows);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, yearMonth.getMonthValue() - 1);
        calendar.set(Calendar.YEAR, yearMonth.getYear());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int weeksInMonth = (int) Math.ceil((firstDayOfWeek - 1 + daysInMonth) / 7.0);

        int[][] calendarArray = new int[weeksInMonth][7];

        int day = 1;
        createCalendarMatrix(yearMonth, firstDayOfWeek, daysInMonth, weeksInMonth, calendarArray, day);

        fillCalendarButtons(yearMonth, buttonRows, calendarArray, availableDays);
        monthMarkup.setKeyboard(buttonRows);

        return monthMarkup;
    }

    private static void createCalendarMatrix(YearMonth yearMonth, int firstDayOfWeek, int daysInMonth, int weeksInMonth, int[][] calendarArray, int day) {
        for (int week = 0; week < weeksInMonth; week++) {
            for (int weekday = 0; weekday < 7; weekday++) {
                if ((week == 0 && weekday < firstDayOfWeek - 2)
                    || (day > daysInMonth)) {
                    calendarArray[week][weekday] = 0;
                } else {
                    if (isCurrentMonth(yearMonth) && LocalDate.now().getDayOfMonth() + 1 > day) {
                        calendarArray[week][weekday] = 0;
                        day++;
                    } else {
                        calendarArray[week][weekday] = day++;
                    }
                }
            }
        }
    }

    private static void fillCalendarButtons(YearMonth yearMonth, List<List<InlineKeyboardButton>> buttonRows,
                                            int[][] calendarArray, Set<LocalDate> availableDays) {
        for (int[] weekArray : calendarArray) {
            List<InlineKeyboardButton> week = new ArrayList<>();
            for (int dayOfMonth : weekArray) {
                if (dayOfMonth == 0) {
                    week.add(createInlineButton(EMPTY_STRING, EMPTY_STRING));
                } else {
                    LocalDate localDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), dayOfMonth);
                    if (availableDays.contains(localDate)) {
                        week.add(createInlineButton(String.valueOf(dayOfMonth), DateTimeUtils.formatDateToString(localDate)));
                    } else {
                        week.add(createInlineButton(EMPTY_STRING, EMPTY_STRING));
                    }
                }
            }
            buttonRows.add(week);
        }
    }

    private static void fillDaysOfWeekButtons(List<List<InlineKeyboardButton>> buttonRows) {
        buttonRows.add(List.of(
                createInlineButton("Пн", EMPTY_STRING),
                createInlineButton("Вт", EMPTY_STRING),
                createInlineButton("Ср", EMPTY_STRING),
                createInlineButton("Чт", EMPTY_STRING),
                createInlineButton("Пт", EMPTY_STRING),
                createInlineButton("Сб", EMPTY_STRING),
                createInlineButton("Вс", EMPTY_STRING)
        ));
    }

    private static void fillControlCalendarButtons(List<List<InlineKeyboardButton>> buttonRows, YearMonth yearMonth) {
        String currentMonth = yearMonth.getMonth().toString();

        buttonRows.add(List.of(
                createButtonPreviousMonth(yearMonth),
                createInlineButton(CalendarState.valueOf(currentMonth).getRussianName() + EMPTY_STRING + yearMonth.getYear(), currentMonth),
                createInlineButton(">>>", CallbackDataConstant.NEXT_MONTH)
        ));
    }

    private static boolean isCurrentMonth(YearMonth yearMonth) {
        LocalDate now = LocalDate.now();
        return yearMonth.getMonth() == now.getMonth()
               && yearMonth.getYear() == now.getYear();
    }

    private static InlineKeyboardButton createInlineButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private static InlineKeyboardButton createButtonPreviousMonth(YearMonth yearMonth) {
        boolean isCurrentMonth = isCurrentMonth(yearMonth);
        String text = isCurrentMonth ? EMPTY_STRING : "<<<";
        String callbackData = isCurrentMonth ? EMPTY_STRING : CallbackDataConstant.PREVIOUS_MONTH;
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public static ReplyKeyboard getContinueOrRepickDateTime() {
        InlineKeyboardMarkup confirmation = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(
                createInlineButton("Продолжить", CallbackDataConstant.ACCEPT_DATE_TIME),
                createInlineButton("Выбрать другую дату/время", CallbackDataConstant.REPICK_DATE_TIME)));

        confirmation.setKeyboard(buttons);
        return confirmation;
    }

    public static ReplyKeyboard getSkipComment() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(createInlineButton("Пропустить", CallbackDataConstant.PRE_CREATE_ORDER)));

        keyboard.setKeyboard(buttons);
        return keyboard;
    }

    public static ReplyKeyboard getCreateOrderOrCancel() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(createInlineButton("Забронировать", CallbackDataConstant.CREATE_ORDER),
                createInlineButton("Отменить", CallbackDataConstant.CANCEL_ORDER)));
        keyboard.setKeyboard(buttons);
        return keyboard;
    }

}

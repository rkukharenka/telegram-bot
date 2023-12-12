package com.rkukharenka.telegrambot.instaboxbot.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstant {

    public static final String GREETING_MSG_FORMAT = """
            Здравствуйте, %s! 👋
            Вас приветствует команда "Instabox Mogilev"!
                        
            ❗ Для правильной работы бота нажимайте на всплывающие кнопки. Не пишите ничего самостоятельно, пока бот не запросит этого, иначе Вы не получите ответа.
            """;

    public static final String CHOOSE_DATE_MSG = """
            🗓 Выберите желаемую дату из календаря
                        
            ❗ Цифрами отмечены доступные дни для бронирования:
            """;

    public static final String SHOW_PICKED_DATE_MSG_FORMAT = "⚠ %s, Вы выбрали дату <b>%s</b> <i>(день-месяц-год)</i>.";

    public static final String CHOOSE_TIME_MSG = """
            ⏰ Введите время бронирования в формате <i>ЧЧ:MM-ЧЧ:MM</i>
            Например: 10:00-14:00
                        
            ❗ Минимальное время бронирования 1 час.
            """;

    public static final String SHOW_PICKED_TIME_MSG_FORMAT = "⚠ %s, Вы выбрали время с <b>%s</b> до <b>%s</b>.";

    public static final String REPICK_TIME_MSG = """
            ❌ Введенное время не соответствует шаблону <i>ЧЧ:MM-ЧЧ:MM</i>.
                        
            🙏 Пожалуйста, повторите ввод.
            """;

    public static final String REVIEW_DATE_AND_TIME_PREORDER_MSG = """
            ❗❗❗ Проверьте дату и время бронирования:
                        
                          🗓 - %s,
                          ⏰ - с %s по %s.
            """;

    public static final String ADD_EVENT_LOCATION_MSG = """
            ⛳ Напишите место проведение мероприятия.
                        
            <i>Например: город Могилев, ул. Якубовского, 71 - Royal Club.</i>
            """;

    public static final String ADD_MOBILE_PHONE_FOR_ORDER_MSG = """
            ❗❗❗Для подтверждения заказа введите 9 цифр номера телефона в формате:
                25ххххххх
                29ххххххх
                33ххххххх
                44ххххххх
            """;

    public static final String ADD_COMMENT_OPTIONAL_MSG_FORMAT = """
            %s, при желании вы можете оставить комментарий. 
                        
            Желаете ли добавить что-то еще?
            """;

    public static final String CONFIRM_CREATE_ORDER_MSG_FORMAT = """
            <b>Информация о Вашем заказе:</b>
                 <i> ☎ %s </i>
                 <i> 📅 %s </i>
                 <i> ⏰ - с %s по %s </i>
                 <i> ⛳ - %s </i>
            """;

    public static final String CREATE_ORDER_MSG_FORMAT = """
            ✅ %s, Ваш заказ создан и ожидает обработки администратором.
                        
            🙏 Спасибо и хорошего дня!
            """;

    public static final String CANCEL_ORDER_MSG_FORMAT = """
            Oops к сожалению Вы отменили заказ.
                        
            До скорых встреч!
            """;

    public static final String CHECK_NUMBER_PHONE_INPUT_MSG = """
            ⚠ Номер телефона не соответствует запрошенному формату.
            Повторите ввод.
            """;
}

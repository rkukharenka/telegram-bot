package com.rkukharenka.telegrambot.instaboxbot.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstant {

    private static final String ORDER_INFO_MSG_PART = """
                 <i> ☎ - %s </i>
                 <i> 📅 - %s (день-месяц-год) </i>
                 <i> ⏰ - с %s по %s </i>
                 <i> ⛳ - %s </i>
                 <i> 💬 - %s </i>
            """;

    public static final String GREETING_MSG_FORMAT = """
            Привет, %s! 👋
            Рады видеть Вас в "Instabox Mogilev"!

            ❗ Пожалуйста, используйте кнопки для взаимодействия с ботом. Не вводите текст самостоятельно, пока бот не попросит.
            """;

    public static final String CHOOSE_DATE_MSG = """
            🗓 Выберите дату из календаря
            
            ❗ Доступные для бронирования дни отмечены цифрами:
            """;

    public static final String SHOW_PICKED_DATE_MSG_FORMAT = "⚠ %s, Вы выбрали дату <b>%s</b> <i>(день-месяц-год)</i>.";

    public static final String CHOOSE_TIME_MSG_FORMAT = """
            ⏰ Введите время бронирования в формате <i>ЧЧ:MM-ЧЧ:MM</i>
            Например: 10:00-14:00
            
            Доступные слоты для записи:
            %s
                        
            ❗ Минимальное время бронирования 1 час.
            """;

    public static final String SHOW_PICKED_TIME_MSG_FORMAT = "⚠ %s, Вы выбрали время с <b>%s</b> до <b>%s</b>.";

    public static final String REPICK_TIME_MSG = """
            ❌ Введенное время не соответствует шаблону <i>ЧЧ:MM-ЧЧ:MM</i>.
                        
            🙏 Пожалуйста, повторите ввод.
            """;

    public static final String CHOSEN_TIME_BUSY_MSG_FORMAT = """
            ❗ Введенное время недоступно для бронирования.
            
            Убедитесь, что ваше время бронирования входит в доступные слоты:
            %s
                        
            🙏 Пожалуйста, повторите ввод.
            """;

    public static final String REVIEW_DATE_AND_TIME_PREORDER_MSG = """
            ❗❗❗ Проверьте дату и время бронирования:
                        
                 🗓 - %s,
                 ⏰ - с %s по %s.
            """;

    public static final String ADD_EVENT_LOCATION_MSG = """
            ⛳ Укажите место проведение мероприятия.
                        
            <i>Например: город Могилев, ул. Якубовского, 71 - Royal Club.</i>
            """;

    public static final String ADD_MOBILE_PHONE_FOR_ORDER_MSG = """
            ❗❗❗Для подтверждения заказа необходимо поделиться номером телефона.
            p.s. используйте кнопку "Поделиться номером телефона".
            """;

    public static final String ADD_COMMENT_OPTIONAL_MSG_FORMAT = """
            💬 %s, при желании оставьте комментарий.
                        
            Желаете ли добавить что-то еще?
            Если да, опишите это в сообщении и отправьте.
            """;

    public static final String CONFIRM_CREATE_ORDER_MSG_FORMAT = """
            <b>Информация о Вашем заказе:</b>
            """ + ORDER_INFO_MSG_PART;

    public static final String CREATE_ORDER_MSG_FORMAT = """
            ✅ %s, Ваш заказ создан и ожидает обработки администратором.
            После подтверждения заказа вы получите сообщение.
                        
            🙏 Спасибо и хорошего дня!
            """;

    public static final String CANCEL_ORDER_MSG_FORMAT = """
            Oops к сожалению Вы отменили заказ.
                        
            До скорых встреч!
            """;

    public static final String CREATE_ORDER_NOTIFICATION_MSG_FORMAT = """
            Создан заказ:
                 <i> 👤 - %s </i>
            """ + ORDER_INFO_MSG_PART;

    public static final String NOT_APPLICABLE_INPUT_MSG = """
            ❌ Ваше действие не соответствует контексту чата.
            ☝ Пожалуйста, обратите внимание на предыдущее сообщение.
            """;

    public static final String ORDER_ACCEPTED_MSG_FORMAT = """
            %s, статус заказа - <b>ПОДТВЕРЖДЕН</b>!
            Информация о заказе:
                 🗓 - %s,
                 ⏰ - с %s по %s.
            С нетерпением ждем встречи!
            
            В случае возникновения каких-то вопросов - %s.
            
            До встречи!
            """;

    public static final String ORDER_DECLINED_MSG = """
            ❌ К сожалению Ваш заказ отклонен администратором.
            """;

}

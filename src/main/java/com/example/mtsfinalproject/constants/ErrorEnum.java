package com.example.mtsfinalproject.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    TARIFF_NOT_FOUND("Тариф не найден"),
    LOAN_CONSIDERATION("Заявка в процессе обработки"),
    LOAN_ALREADY_APPROVED("Кредит уже одобрен"),
    TRY_LATER("Попробуйте позже"),
    ORDER_NOT_FOUND("Заявка не найдена"),
    ORDER_IMPOSSIBLE_TO_DELETE("Невозможно удалить заявку"),
    TIMEOUT("Слишком долгое время выполнения запроса");

    private final String message;

}

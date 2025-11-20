package com.bortnik.expensetracker.controller.validator;

import com.bortnik.expensetracker.exceptions.BadRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class DateValidator {

    public void validateDateIsFuture(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BadRequest("Date cannot be before today");
        }
    }

    public void validateEndDatePastStartDate(
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        if (endDate.isBefore(startDate)) {
            throw new BadRequest("End date cannot be before start date");
        }
    }
}

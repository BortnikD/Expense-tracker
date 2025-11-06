package com.bortnik.expensetracker.scheduler;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.service.BudgetPlanService;
import com.bortnik.expensetracker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpenseReportScheduler {

    private final BudgetPlanService budgetPlanService;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendNotification() {
        int page = 0;
        final int pageSize = 200;
        Page<BudgetPlanDTO> result;

        do {
            result = budgetPlanService.getAllBySpentAmountExceedsLimit(Pageable.ofSize(pageSize).withPage(page));
            result.forEach(notificationService::notifyUserAboutExceededLimit);
            page++;
        } while (result.hasNext());

        log.info("Sending expense reports is completed");
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void deleteNotActualNotifications() {
        notificationService.deleteNotActualNotifications();
    }
}

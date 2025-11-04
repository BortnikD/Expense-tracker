ALTER TABLE budget_plans
DROP CONSTRAINT budget_plans_spent_amount_check;

ALTER TABLE budget_plans
ADD CONSTRAINT budget_plans_spent_amount_check CHECK (spent_amount >= 0);
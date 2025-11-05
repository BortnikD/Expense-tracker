CREATE TABLE exceeded_budget_notification_log (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    budget_month VARCHAR(7) NOT NULL, -- YYYY-MM
    category_id UUID REFERENCES categories(id),
    notified_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uq_exceeded_notification UNIQUE (user_id, budget_month, category_id)
);

CREATE INDEX idx_exceeded_notification_user_id ON exceeded_budget_notification_log (user_id);
CREATE INDEX idx_exceeded_notification_budget_month ON exceeded_budget_notification_log (budget_month);
CREATE INDEX idx_exceeded_notification_category_id ON exceeded_budget_notification_log (category_id);

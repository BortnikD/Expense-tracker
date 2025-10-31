CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    username   VARCHAR(128) UNIQUE NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE categories
(
    id      UUID PRIMARY KEY,
    user_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    name    VARCHAR(255)                                 NOT NULL,
    UNIQUE (user_id, name)
);

CREATE TABLE expenses
(
    id          UUID PRIMARY KEY,
    user_id     UUID REFERENCES users (id) ON DELETE CASCADE      NOT NULL,
    category_id UUID REFERENCES categories (id) ON DELETE CASCADE NOT NULL,
    amount      NUMERIC(15, 2)                                    NOT NULL CHECK (amount > 0),
    date        DATE                                              NOT NULL,
    description TEXT
);

CREATE TABLE budget_plans
(
    id           UUID PRIMARY KEY,
    user_id      UUID REFERENCES users (id) ON DELETE CASCADE      NOT NULL,
    category_id  UUID REFERENCES categories (id) ON DELETE CASCADE NOT NULL,
    limit_amount NUMERIC(15, 2)                                    NOT NULL CHECK (limit_amount > 0),
    spent_amount NUMERIC(15, 2)                                    NOT NULL CHECK (spent_amount > 0),
    month        DATE                                              NOT NULL,
    UNIQUE (user_id, category_id, month)
);

CREATE TABLE notifications
(
    id         UUID PRIMARY KEY,
    user_id    UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    message    TEXT                                         NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    read       BOOLEAN     DEFAULT FALSE
);

CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);

CREATE INDEX idx_categories_user_id ON categories (user_id);
CREATE INDEX idx_categories_name ON categories (name);

CREATE INDEX idx_expenses_user_id ON expenses (user_id);
CREATE INDEX idx_expenses_category_id ON expenses (category_id);
CREATE INDEX idx_expenses_date ON expenses (date);

CREATE INDEX idx_budget_plans_user_id ON budget_plans (user_id);
CREATE INDEX idx_budget_plans_category_id ON budget_plans (category_id);
CREATE INDEX idx_budget_plans_month ON budget_plans (month);

CREATE INDEX idx_notifications_user_id ON notifications (user_id);
CREATE INDEX idx_notifications_created_at ON notifications (created_at);

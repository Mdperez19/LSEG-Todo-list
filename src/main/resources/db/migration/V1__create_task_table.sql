CREATE TABLE IF NOT EXISTS task (
    id UUID NOT NULL PRIMARY KEY,
    description VARCHAR(255),
    due_date DATE,
    status VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    CONSTRAINT task_status_check CHECK (
        status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')
    )
);
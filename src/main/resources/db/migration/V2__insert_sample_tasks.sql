INSERT INTO task (id, title, description, due_date, status)
VALUES
    (gen_random_uuid(), 'Tarea 1', 'Descripción de la tarea 1', '2025-06-01', 'PENDING'),
    (gen_random_uuid(), 'Tarea 2', 'Descripción de la tarea 2', '2025-06-02', 'IN_PROGRESS'),
    (gen_random_uuid(), 'Tarea 3', 'Descripción de la tarea 3', '2025-06-03', 'COMPLETED'),
    (gen_random_uuid(), 'Tarea 4', 'Descripción de la tarea 4', '2025-06-04', 'PENDING'),
    (gen_random_uuid(), 'Tarea 5', 'Descripción de la tarea 5', '2025-06-05', 'IN_PROGRESS');

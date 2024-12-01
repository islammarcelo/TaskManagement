--liquibase formatted sql
--changeset mk:1
INSERT INTO "users" ("id", "email", "username", "password", "role") VALUES (1, 'eslamadel2000@yahoo.com', 'eslamadel','$2a$10$HWEsI.HmZL8nSrkAM0pjj.5IwczpH1OqBDXXyZl9Wl/hpX3V42Vwy' ,'ADMIN');
INSERT INTO "users" ("id", "email", "username", "password", "role") VALUES (2, 'islamadel676@gmail.com', 'eslamadel676','$2a$10$HWEsI.HmZL8nSrkAM0pjj.5IwczpH1OqBDXXyZl9Wl/hpX3V42Vwy', 'REQULAR_USER');

INSERT INTO "task" ("id", "description", "due_date", "priority", "task_status", "title") VALUES (1, 'first task for task management', '2024-12-01','low', 'TODO', 'first task');
INSERT INTO "task" ("id", "description", "due_date", "priority", "task_status", "title") VALUES (2, 'second task for task management', '2024-12-02','medium', 'IN_PROGRESS', 'second task');
INSERT INTO "task" ("id", "description", "due_date", "priority", "task_status", "title") VALUES (3, 'third task for task management', '2024-12-03','high', 'DONE', 'third task');





DELETE FROM vote;
DELETE FROM dish;
DELETE FROM restaurant;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User1', 'user1@email.com', '{noop}password'),  --100000
       ('User2', 'user2@email.com', '{noop}password'),  --100001
       ('Admin', 'admin@email.com', '{noop}password');  --100002

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('ADMIN', 100002);

INSERT INTO restaurant (name)
VALUES ('Random Pizza'),            --100003
       ('Random Sushi'),            --100004
       ('Random Stuff');            --100005

INSERT INTO dish (RESTAURANT_ID, NAME, PRICE, DATE)
VALUES (100003, 'Chefburger', 199.00, '2020-01-30'),
       (100003, 'Longer', 99.00, '2020-01-30'),
       (100003, 'Twister', 66.00, '2020-01-30'),
       (100004, 'Ice coffee', 99.00, '2020-01-30'),
       (100004, 'Donat', 99.00, '2020-01-30'),
       (100004, 'Big Mak', 149.00, '2020-01-30'),
       (100005, 'Whopper', 136.00, '2020-01-30'),
       (100005, 'Big King', 159.00, '2020-01-30'),
       (100005, 'Pepsi', 89.00, '2020-01-30');

INSERT INTO vote (USER_ID, RESTAURANT_ID, DATE)
VALUES (100000, 100003, '2020-01-30');
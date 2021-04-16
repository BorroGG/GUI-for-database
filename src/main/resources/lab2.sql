CREATE DATABASE lab2;

CREATE TABLE Priority /* Приоритет задания */
(
    PriorityID CHAR(1) NOT NULL PRIMARY KEY,
    Name CHAR(7) NOT NULL
);

INSERT INTO Priority VALUES /* степени: 1 - высокий, 2 - средний, 3 - низкий */
(1, 'Высокий'),
(2, 'Средний'),
(3, 'Низкий');

CREATE TABLE Position /* Должность */
(
    PositionID SMALLINT NOT NULL PRIMARY KEY,
    Title VARCHAR(20) NOT NULL
);

INSERT INTO Position VALUES
(1, 'Менеджер'),
(2, 'Рядовой сотрудник');

CREATE TABLE Employee /* Работник */
(
    ServiceNumber INTEGER NOT NULL PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    MiddleName VARCHAR(50),
    Phone CHAR(11) NOT NULL,
    Email VARCHAR(40) NOT NULL,
    Fax CHAR(5) NOT NULL,
    Login VARCHAR(30) NOT NULL,
    Password VARCHAR(15) NOT NULL,
    PositionID SMALLINT REFERENCES Position (PositionID)
);
/*1 = 01sweet01 = $2a$10$xkMPYJ04Ze7O7TF9vOfvKusPYEMgBtU7XXDihq6m9XkyFqHWoDTrC
2 = Qwerty1010 = $2a$10$OvGi2rXtvmp5/9.daYiEZerK/QsCLlXTcTNgmeSAiY6f3T96Dal4q
3 = 1234PoP4321 = $2a$10$PrlenXGKYkGd5kZk8RmWzeRRPp4LM4/bDI8NhVnH3i21UCWCrNRg6
4 = 08MmaG01 = $2a$10$xToDZxwuaIzeTvT2ZW/qEubEiYhvuT3.kLvYJtxSb0pD/DUOj/h7m
5 = A01n02n3a4 = $2a$10$bUNpg0ecbMiZTMNfNtYQeOBhDuN9HOYwtgUH00uD4ABamaQtkdWcu*/
INSERT INTO Employee VALUES
(1, 'Ольга', 'Кудряшова', 'Валерьевна', '79691005263', 'olga_kudr01@email.com', '96901', 'kudryashova.olga', '$2a$10$xkMPYJ04Ze7O7TF9vOfvKusPYEMgBtU7XXDihq6m9XkyFqHWoDTrC', 1),
(2, 'Кирилл', 'Цветков', 'Михайлович', '79268965412', 'kirill_cvet@email.com', '96902', 'cvetkov.kirill', '$2a$10$OvGi2rXtvmp5/9.daYiEZerK/QsCLlXTcTNgmeSAiY6f3T96Dal4q', 1),
(3, 'Cергей', 'Попов', 'Семенович', '79153212326', 'sergey_pop@email.com', '96903', 'popov.sergey', '$2a$10$PrlenXGKYkGd5kZk8RmWzeRRPp4LM4/bDI8NhVnH3i21UCWCrNRg6', 2),
(4, 'Вильгейм', 'Магнуссон', null, '79996541203', 'vilgeim_magnus@email.com', '96904', 'magnusov.vilgeim', '$2a$10$xToDZxwuaIzeTvT2ZW/qEubEiYhvuT3.kLvYJtxSb0pD/DUOj/h7m', 2),
(5, 'Анна', 'Осипова', 'Игоревна', '79161052123', 'anna_osip@email.com', '96905', 'osipova.anna', '$2a$10$bUNpg0ecbMiZTMNfNtYQeOBhDuN9HOYwtgUH00uD4ABamaQtkdWcu', 2);


CREATE TABLE Customer /* Клиент */
(
    CustomerID INTEGER NOT NULL PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    MiddleName VARCHAR(50),
    Phone CHAR(11) NOT NULL,
    Email VARCHAR(40) NOT NULL,
    INN VARCHAR(12) NOT NULL /*Физ.лицо = 10 симв., Юр.лицо = 12 с.*/
);

INSERT INTO Customer VALUES
(1, 'Надежда', 'Королева', 'Сергеевна', '79267842610', 'korol_n@ya.ru', '4568120954'),
(2, 'Юлия', 'Беляева', 'Алексеевна', '79165129685', 'belya_ul@gmail.com', '102564812053'),
(3, 'Геннадий', 'Горин', 'Анатольевич', '79268254163', 'goringorin@ya.ru', '8012453651'),
(4, 'Адам', 'Воропаев', 'Феликсович', '79162304512', 'adamvp@gmail.com', '1654782155'),
(5, 'Валентин', 'Поровозов', 'Александрович', '79251025423', 'porovoz_val@ya.ru', '235649910532'),
(6, 'Вячеслав', 'Алексеев', 'Дмитриевич', '79991047854', 'aleks_vyacheslav@mail.ru', '1205462345'),
(7, 'Мирослава', 'Мухина', 'Сергеевна', '79995218632', 'muhina_mira@mail.ru', '565552013499');

CREATE TABLE Contract /* Контракт */
(
    Number INTEGER NOT NULL PRIMARY KEY,
    DateConclusion DATE NOT NULL,
    Description VARCHAR(100) NOT NULL,
    CustomerID INTEGER REFERENCES Customer (CustomerID)
);

INSERT INTO Contract VALUES
(1, '2021-03-07', 'Поставка проектора (сер.ном. 999001) Воропаеву Адаму', 4),
(2, '2021-03-09',
 'Ремонт устройства (999002), т.к. вышел из строя во время эксплуатации, предусмотренной инструкцией', 5);

CREATE TABLE Equipment /* Оборудование */
(
    SerNumber CHAR(10) NOT NULL PRIMARY KEY,
    Type VARCHAR(20) NOT NULL,
    Name VARCHAR(50) NOT NULL,
    Options VARCHAR(100) NOT NULL,
    Number INTEGER REFERENCES Contract (Number)
);

INSERT INTO Equipment VALUES
('999001', 'Проектор', 'LG ProBeam лазерный 4K',
 'Проектор, используемый для презентаций во время бизнес-встреч в переговорной офиса клиента', 1),
('999002', 'Офисный ноутбук', 'Lenovo Yoga Slim7 14ITL05',
 'Ноутбук VIP-клиента для обеспечения безопасных переговоров', 2);

CREATE TABLE Mission /* Задание */
(
    MissionID INTEGER NOT NULL PRIMARY KEY,
    Name VARCHAR(30) NOT NULL,
    Description VARCHAR(70) NOT NULL,
    Begin_Date DATE NOT NULL,
    End_Date DATE,
    Complete TIMESTAMP,
    Author SMALLINT REFERENCES Employee (ServiceNumber) ,
    Executor SMALLINT REFERENCES Employee (ServiceNumber),
    PriorityID CHAR(1) REFERENCES Priority (PriorityID),
    CustomerID INTEGER REFERENCES Customer (CustomerID)
);

INSERT INTO Mission VALUES
(1, 'Проведение презентации', 'Провести презентацию для привлечения новых клиентов на фестивале',
 '2021-03-01', '2021-04-01', null, 2, 2, 2, 7),
(2, 'Телефонный звонок', 'Связаться с Беляевой Юлией (79165129685) в ближайшее время',
 '2021-03-03', '2021-03-05', '2021-03-04 10:14:21', 1, 5, 1, 2),
(3, 'Поставка оборудования', 'Поставить устройство с серийным номером 999001 Воропаеву Адаму',
 '2021-03-07', '2021-05-07', null, 1, 3, 3, 4),
(4, 'Гарантийный ремонт', 'Отправить устройств с серийным номером 999002 на гарантийный ремонт',
 '2021-03-09', '2021-08-09', null, 1, 3, 2, 5),
(5, 'Телефонный звонок', 'Связаться с Алексеевым Вячеславом (79991047854) по поводу договора',
 '2021-03-12', '2021-03-15', '2021-03-15 09:35:11', 2, 4, 1, 6),
(6, 'Электронное сообщение', 'Запросить отчет по электронной почте korol_n@ya.ru за 3 месяца',
 '2021-03-19', '2021-03-20', '2021-03-19 15:52:30', 2, 1, 1, 1),
(7, 'Отправка факса', 'Пригласить на совещание 01.04.21 в 10:00 Геннадия Горина',
 '2021-03-24', '2021-03-30', '2021-03-25 11:42:51', 2, 5, 3, 3);

CREATE ROLE admin WITH SUPERUSER createdb createrole replication bypassrls LOGIN PASSWORD 'root'; --создание админа

CREATE GROUP managers WITH USER kudryashova_olga, cvetkov_kirill; --создание группы менеджеров
CREATE GROUP ordinary WITH USER popov_sergey, magnusov_vilgeim, osipova_anna; --создание группы рядовых сотрудников

CREATE USER kudryashova_olga WITH PASSWORD '01sweet01'; --менеджер
CREATE USER cvetkov_kirill WITH PASSWORD 'Qwerty1010'; --менеджер
CREATE USER popov_sergey WITH PASSWORD '1234PoP4321'; --рядовой
CREATE USER magnusov_vilgeim WITH PASSWORD '08MmaG01'; --рядовой
CREATE USER osipova_anna WITH PASSWORD 'A01n02n3a4'; --рядовой

SELECT * FROM pg_group; --посмотреть группы

ALTER GROUP managers ADD USER cvetkov_kirill, kudryashova_olga;

GRANT ALL PRIVILEGES ON database lab2 TO admin;

GRANT ALL PRIVILEGES ON contract, customer, employee, equipment, position, priority TO managers;

GRANT ALL PRIVILEGES ON contract, customer, employee, equipment, position, priority TO ordinary;

GRANT ALL PRIVILEGES ON mission TO managers;

GRANT SELECT, INSERT, UPDATE (missionid, name, description, begin_date, end_date, complete, author, priorityid, customerid) ON mission TO ordinary; --Рядовые сотрудники не могут назначать задания.

GRANT SELECT (executor) ON mission TO ordinary;

ALTER TABLE Mission ENABLE ROW LEVEL SECURITY;

/*Просматривать задание, автором которого является менеджер, может либо автор, либо исполнитель задания.*/
/*Каждый менеджер может помимо своих задача просматривать задачи рядовых сотрудников. */
CREATE POLICY gives_mission ON Mission to managers
USING (
    ('Рядовой сотрудник' = (SELECT p.Title
FROM employee e
JOIN Position p on p.PositionID = e.PositionID
WHERE author = e.servicenumber)
        ) or (
            current_user = (SELECT e.login
FROM employee e
JOIN Position p on p.PositionID = e.PositionID
WHERE current_user = e.login and (Author = e.servicenumber or executor = e.servicenumber))
    ) or current_user = 'admin');

CREATE POLICY gives_mission_ordinary ON Mission to ordinary
USING (
current_user = (SELECT e.login
FROM employee e
JOIN Position p on p.PositionID = e.PositionID
WHERE current_user = e.login and (Author = e.servicenumber or executor = e.servicenumber))
     or current_user = 'admin');

/*Помечать задание как выполненное и указывать дату завершения может либо автор, либо исполнитель задания.*/
CREATE OR REPLACE FUNCTION checkRightsF1()
RETURNS trigger AS
$$
BEGIN
    IF current_user = 'admin' or OLD.author IN (SELECT e.ServiceNumber
FROM employee e
JOIN Position p on p.PositionID = e.PositionID
JOIN Mission M on e.ServiceNumber = M.Author
WHERE current_user = e.login and (Author = e.servicenumber or executor = e.servicenumber)) or OLD.executor IN (SELECT e.ServiceNumber
FROM employee e
JOIN Position p on p.PositionID = e.PositionID
JOIN Mission M on e.ServiceNumber = M.Author
WHERE current_user = e.login and (Author = e.servicenumber or executor = e.servicenumber)) THEN
        RETURN NEW;
ELSE
        RAISE EXCEPTION 'permission denied';
END IF;
END;
$$
LANGUAGE 'plpgsql';

CREATE TRIGGER checkRightsTrigger1 BEFORE UPDATE OF end_date, complete ON mission
    FOR EACH ROW EXECUTE FUNCTION checkRightsF1();

/*UPDATE mission SET end_date = '2021-03-19' WHERE missionid = 5;*/



/*Вносить какие-либо другие изменения в задание может только автор. */
CREATE OR REPLACE FUNCTION checkRightsF2()
RETURNS trigger AS
$$
BEGIN
    IF current_user = 'admin' or OLD.author IN (SELECT e.ServiceNumber
FROM employee e
JOIN Position p on p.PositionID = e.PositionID
JOIN Mission M on e.ServiceNumber = M.Author
WHERE current_user = e.login and (Author = e.servicenumber or executor = e.servicenumber)) THEN
        RETURN NEW;
ELSE
        RAISE EXCEPTION 'permission denied';
END IF;
END;
$$
LANGUAGE 'plpgsql';

CREATE TRIGGER checkRightsTrigger2 BEFORE UPDATE OF missionid, name, description, begin_date, author, executor, priorityid, customerid ON mission
    FOR EACH ROW EXECUTE FUNCTION checkRightsF2();

/*UPDATE mission SET name = 'lololllll' WHERE missionid = 4;*/

/*После завершения задания внесение в него изменений не допускается. */
CREATE OR REPLACE FUNCTION checkRightsF3()
RETURNS trigger AS
$$
BEGIN
    IF current_user = 'admin' or OLD.complete IS NULL THEN
        RETURN NEW;
ELSE
        RAISE EXCEPTION 'mission already complete';
END IF;
END;
$$
LANGUAGE 'plpgsql';

CREATE TRIGGER checkRightsTrigger3 BEFORE UPDATE ON mission
    FOR EACH ROW EXECUTE FUNCTION checkRightsF3();

/*UPDATE mission SET name = 'kokok6k' WHERE missionid = 1;*/

/*По прошествии 12 месяцев после даты завершения задания сведения о нем удаляются из системы.*/
CREATE OR REPLACE FUNCTION checkComplete12mouthF()
RETURNS trigger AS
$$
DECLARE
r mission%rowtype;
BEGIN
FOR r IN
SELECT * FROM mission WHERE End_Date + Interval '1 YEAR' < current_date
    LOOP
DELETE FROM Mission WHERE missionid = r.MissionID;
END LOOP;
RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

CREATE TRIGGER checkComplete12mouthT AFTER UPDATE ON mission
    FOR EACH ROW EXECUTE FUNCTION checkComplete12mouthF();

/*SELECT * FROM Mission;
UPDATE mission SET name = 'kokok6k' WHERE missionid = 1;
SELECT * FROM Mission;*/

COPY (SELECT * FROM mission WHERE Author = 2) TO 'D:/mission.csv' CSV;

ALTER TABLE employee ALTER COLUMN password TYPE varchar(70);

CREATE SEQUENCE ServiceNumber_seq START 6;

ALTER TABLE Employee ALTER COLUMN ServiceNumber SET DEFAULT NEXTVAL('ServiceNumber_seq');

INSERT INTO Position VALUES
(3, 'Админ');

CREATE SEQUENCE MissionID_seq START 10;

ALTER TABLE Mission ALTER COLUMN MissionID SET DEFAULT NEXTVAL('MissionID_seq');

CREATE SCHEMA kitty;
USE kitty;

CREATE TABLE Player (
                        id INT NOT NULL AUTO_INCREMENT,
                        username VARCHAR(30) NOT NULL,
                        date_of_birth DATE,
                        gender ENUM('M', 'F') NOT NULL,
                        CONSTRAINT p_PK PRIMARY KEY (id)
);

-- Made breed a table instead so it can be referencced
CREATE TABLE Breed (
                       name VARCHAR(20) PRIMARY KEY
);

-- Added breed foreign key
CREATE TABLE Cat (
                     id INT NOT NULL AUTO_INCREMENT,
                     name VARCHAR(20) NOT NULL,
                     gender ENUM('M', 'F') NOT NULL,
                     life_stage ENUM('kitten', 'young adult', 'mature adult', 'senior') NOT NULL,
                     breed VARCHAR(20) NOT NULL,
                     color VARCHAR(30) NOT NULL,
                     personality VARCHAR(15) NOT NULL,
                     description VARCHAR(50) NOT NULL,
                     CONSTRAINT c_PK PRIMARY KEY (id),
                     CONSTRAINT c_FK_Breed
                         FOREIGN KEY (breed)
                             REFERENCES Breed(name)
                             ON DELETE RESTRICT
                             ON UPDATE RESTRICT
);

CREATE TABLE Food (
                      name VARCHAR(15) NOT NULL,
                      price DECIMAL(7,2) NOT NULL,
                      description VARCHAR(50) NOT NULL,
                      saturation_value INT NOT NULL,
                      CONSTRAINT fo_PK PRIMARY KEY (name)
);

CREATE TABLE Furniture (
                           name VARCHAR(20) NOT NULL,
                           type VARCHAR(15) NOT NULL,
                           price DECIMAL(7,2) NOT NULL,
                           description VARCHAR(100) NOT NULL,
                           CONSTRAINT fu_PK PRIMARY KEY (name)
);

CREATE TABLE Player_Inventory (
                                  player_id INT,
                                  money DECIMAL(10,2) DEFAULT 200,
                                  CONSTRAINT pk_player_inventory
                                      PRIMARY KEY (player_id),
                                  CONSTRAINT fk_player_inventory_player
                                      FOREIGN KEY (player_id)
                                          REFERENCES Player(id)
                                          ON DELETE RESTRICT
                                          ON UPDATE CASCADE
);

-- added auto increment
CREATE TABLE Owned_cat (
                           id INT NOT NULL AUTO_INCREMENT,
                           date_arrived DATE NOT NULL,
                           affection_points INT
                               NOT NULL
                               DEFAULT 0,
                           hunger INT
                               NOT NULL
                               DEFAULT 50,
                           player_id INT NOT NULL,
                           cat_id INT NOT NULL,
                           CONSTRAINT oc_PK
                               PRIMARY KEY (id),
                           CONSTRAINT oc_FK_Player
                               FOREIGN KEY (player_id)
                                   REFERENCES Player(id)
                                   ON DELETE RESTRICT
                                   ON UPDATE CASCADE,
                           CONSTRAINT oc_FK_Cat
                               FOREIGN KEY (cat_id)
                                   REFERENCES Cat(id)
                                   ON DELETE RESTRICT
                                   ON UPDATE CASCADE,
                           CONSTRAINT Player_Cat_Duplicate
                               UNIQUE (player_id, cat_id)
);

CREATE TABLE Owned_Food (
                            player_id INT NOT NULL,
                            food_name VARCHAR(15) NOT NULL,
                            quantity INT,
                            CONSTRAINT ofo_PK
                                PRIMARY KEY(player_id, food_name),
                            CONSTRAINT ofo_FK_Player
                                FOREIGN KEY (player_id)
                                    REFERENCES Player(id)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE,
                            CONSTRAINT o_FK_Food
                                FOREIGN KEY (food_name)
                                    REFERENCES Food(name)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE
);

CREATE TABLE Food_Preference (
                                 cat_breed VARCHAR(20) NOT NULL,
                                 food_name VARCHAR(15) NOT NULL,
                                 CONSTRAINT fop_PK
                                     PRIMARY KEY(cat_breed, food_name),
                                 CONSTRAINT fop_FK_Breed
                                     FOREIGN KEY (cat_breed)
                                         REFERENCES Breed(name)
                                         ON DELETE RESTRICT
                                         ON UPDATE CASCADE,
                                 CONSTRAINT fop_FK_Food
                                     FOREIGN KEY (food_name)
                                         REFERENCES Food(name)
                                         ON DELETE RESTRICT
                                         ON UPDATE CASCADE
);

CREATE TABLE Furniture_Preference (
                                      cat_breed VARCHAR(20) NOT NULL,
                                      furniture_name VARCHAR(20) NOT NULL,
                                      CONSTRAINT fup_PK
                                          PRIMARY KEY(cat_breed, furniture_name),
                                      CONSTRAINT fup_FK_Breed
                                          FOREIGN KEY (cat_breed)
                                              REFERENCES Breed(name)
                                              ON DELETE RESTRICT
                                              ON UPDATE CASCADE,
                                      CONSTRAINT fop_FK_Furniture
                                          FOREIGN KEY (furniture_name)
                                              REFERENCES Furniture(name)
                                              ON DELETE RESTRICT
                                              ON UPDATE CASCADE
);

CREATE TABLE Owned_Furniture (
                                 player_id INT NOT NULL,
                                 furniture_name VARCHAR(20) NOT NULL,
                                 quantity int,
                                 CONSTRAINT ofu_PK
                                     PRIMARY KEY(player_id, furniture_name),
                                 CONSTRAINT ofu_FK_Player
                                     FOREIGN KEY (player_id)
                                         REFERENCES Player(id)
                                         ON DELETE RESTRICT
                                         ON UPDATE CASCADE,
                                 CONSTRAINT ofu_FK_Furniture
                                     FOREIGN KEY (furniture_name)
                                         REFERENCES Furniture(name)
                                         ON DELETE RESTRICT
                                         ON UPDATE CASCADE
);

CREATE TABLE Placed_Furniture (
                                  id INT NOT NULL AUTO_INCREMENT,
                                  position_placed INT
                                         NOT NULL
                                      CHECK (position_placed BETWEEN 1 AND 10),
                                  player_id INT NOT NULL,
                                  furniture_name VARCHAR(20) NOT NULL,
                                  CONSTRAINT pfu_PK
                                      PRIMARY KEY (id),
                                  CONSTRAINT pfu_FK_Player
                                      FOREIGN KEY (player_id)
                                          REFERENCES Player(id)
                                          ON DELETE RESTRICT
                                          ON UPDATE CASCADE,
                                  CONSTRAINT p_FK_Furniture
                                      FOREIGN KEY (furniture_name)
                                          REFERENCES Furniture(name)
                                          ON DELETE RESTRICT
                                          ON UPDATE CASCADE,
                                  CONSTRAINT Player_Furniture_Position
                                      UNIQUE(player_id, position_placed)
);

CREATE TABLE Food_eaten (
                            owned_cat_id INT NOT NULL,
                            food_name VARCHAR(15) NOT NULL,
                            time_fed DATETIME NOT NULL,
                            CONSTRAINT fe_PK
                                PRIMARY KEY (owned_cat_id, food_name, time_fed),
                            CONSTRAINT fe_FK_Owned_Cat
                                FOREIGN KEY (owned_cat_id)
                                    REFERENCES Owned_Cat(id)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE,
                            CONSTRAINT fe_FK_food_name
                                FOREIGN KEY (food_name)
                                    REFERENCES Food(name)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE
);

CREATE TABLE Furniture_Occupancy (
                                     placed_furniture_id INT NOT NULL,
                                     owned_cat_id INT NOT NULL,
                                     time_occupied DATE NOT NULL,
                                     CONSTRAINT fuo_PK
                                         PRIMARY KEY (placed_furniture_id, owned_cat_id, time_occupied),
                                     CONSTRAINT fuo_FK_Placed_Furniture
                                         FOREIGN KEY (placed_furniture_id)
                                             REFERENCES Placed_Furniture(id)
                                             ON DELETE RESTRICT
                                             ON UPDATE CASCADE,
                                     CONSTRAINT fuo_FK_Owned_Cat
                                         FOREIGN KEY (owned_cat_id)
                                             REFERENCES Owned_Cat(id)
                                             ON DELETE CASCADE
                                             ON UPDATE CASCADE
);

CREATE TABLE Furniture_Occupancy_Archive (
                                             player_id INT,
                                             furniture_name VARCHAR(20),
                                             owned_cat_id INT,
                                             time_occupied DATE
);

-- Populate database unto the Player records --
INSERT INTO Player (username, date_of_birth, gender)
VALUES
    ('Zai', '1999-07-01', 'F'),
    ('Wendy', '2008-05-01', 'M'),
    ('Seally', '1979-10-01', 'F'),
    ('Mumei', '1989-02-01', 'F'),
    ('Nerissa', '2007-10-01', 'F'),
    ('LeBron James', '1975-05-01', 'M'),
    ('Axel', '1987-11-01', 'M'),
    ('Xx_Hakos_xX', '1990-06-01', 'F'),
    ('Carl', '1967-11-01', 'M'),
    ('finx', '2010-06-01', 'M');

-- Populate database unto the breed records --
INSERT INTO Breed(name)
VALUES
    ('British Shorthair'),
    ('Bengal'),
    ('Siamese'),
    ('Persian'),
    ('Maine Coon'),
    ('Ragdoll'),
    ('Domestic Shorthair');

-- Populate database unto the cat records --
INSERT INTO Cat(name, gender, life_stage, breed, color, personality, description)
VALUES
    ('Thunder', 'M', 'kitten', 'British Shorthair', 'gray', 'reserved', 'Quiet yet playful'),
    ('Maple', 'F', 'senior', 'Bengal', 'orange', 'affectionate', 'Loves cuddles and naps'),
    ('Luna', 'F', 'young adult', 'Siamese', 'cream with spots', 'tame', 'Curious and social'),
    ('Pringles', 'M', 'mature adult', 'Persian', 'white', 'lazy', 'Enjoys resting on furnitures'),
    ('Brook', 'F', 'young adult', 'Maine Coon', 'brown tabby', 'playful', 'Always loves playing'),
    ('Sam', 'F', 'kitten', 'Ragdoll', 'Mitted', 'affectionate', 'Cares for your furniture'),
    ('Oscar', 'M', 'mature adult', 'Domestic Shorthair', 'black', 'independent', 'Explorer of high places'),
    ('Bella', 'F', 'young adult', 'Ragdoll', 'seal bicolor', 'gentle', 'Loves being carried'),
    ('Simba', 'M', 'kitten', 'Bengal', 'spotted rosette', 'energetic', 'Always on the move'),
    ('Chloe', 'F', 'senior', 'Persian', 'silver', 'calm', 'Queen of the furnitures'),
    ('Leo', 'M', 'young adult', 'Maine Coon', 'black smoke', 'friendly', 'Gentle giant with purrs'),
    ('Milo', 'M', 'kitten', 'Siamese', 'chocolate point', 'vocal', 'Chatty little companion'),
    ('Sophie', 'F', 'mature adult', 'British Shorthair', 'blue', 'dignified', 'Plush toy appearance'),
    ('Max', 'M', 'young adult', 'Maine Coon', 'pinkish gray', 'affectionate', 'Seeks warm laps always'),
    ('Lily', 'F', 'kitten', 'British Shorthair', 'calico', 'sweet', 'Adorable folded ears'),
    ('Charlie', 'M', 'mature adult', 'Siamese', 'chocolate-point', 'reserved', 'Emerald green eyes shine'),
    ('Zoe', 'F', 'senior', 'Domestic Shorthair', 'tortoiseshell', 'sassy', 'Personality in every spot'),
    ('Tiger', 'M', 'young adult', 'Bengal', 'marble pattern', 'playful', 'Water fascination enthusiast'),
    ('Coco', 'F', 'mature adult', 'Persian', 'sable brown', 'people-oriented', 'Shadow follows you everywhere'),
    ('Jasper', 'M', 'kitten', 'Ragdoll', 'blue mitted', 'docile', 'Floppy when held');



-- Populate database unto the Food records --
INSERT INTO Food (name, price, description, saturation_value)
VALUES
    ('Tuna_Bites', 35.00, 'Simple tuna snack', 10),
    ('Salmon_Jelly', 45.00, 'Soft salmon jelly', 12),
    ('Chicken_Shreds', 40.00, 'Shredded chicken meat', 14),
    ('Beef_Kibble', 30.00, 'Dry beef flavored kibble', 8),
    ('Mackerel_Stew', 55.00, 'Warm fish stew', 18),
    ('Ocean_Treats', 60.00, 'Mixed seafood treats', 16),
    ('Fish_Biscuits', 25.00, 'Crunchy fish biscuits', 6),
    ('Seafood_Deluxe', 80.00, 'Premium seafood set', 20),
    ('Milk_Pudding', 20.00, 'Sweet milk pudding', 5),
    ('Shrimp_Bowl', 50.00, 'Fresh shrimp bowl', 15);



-- Populate database unto the Furniture records --
INSERT INTO Furniture (name, type, price, description)
VALUES
    ("Sakura Pillow", "pillow", 220, "blush pink and delicate, like spring petals"),
    ("Flat Cardboard", "box", 90, "thin, plain, easy to stack"),
    ("Grass Cushion", "cushion", 200, "soft and green, like a patch of spring grass"),
    ("Small Shopping Box", "box", 70, "compact, handy, just enough"),
    ("Red Bucket", "bucket", 60, "bright and cheerful, ready for fun"),
    ("Sushi Cushion", "cushion", 320, "soft, squishy, and delightfully bite-sized"),
    ("Yellow Pillow", "pillow", 120, "soft and sunny, hugs you gently"),
    ("Large Shopping Box", "box", 100, "roomy, bulky, holds a lot"),
    ("Bamboo House", "house", 380, "light, airy, and full of natural charm"),
    ("Lucky Cushion", "cushion", 120, "bright and cheerful, brings good luck"),
    ("Luxurious Hamock", "hamock", 650, "soft, elegant, and made for ultimate relaxation"),
    ("Snowy Pillow", "pillow", 220, "crisp, cool, and fluffy as fresh snow"),
    ("Chocolate Tart Bed", "bed", 290, "plush and chocolaty, perfect for a cat nap"),
    ("FLuffy Bed", "bed", 160, "pillow-soft and perfect for long cat naps"),
    ("Treasure Box", "box", 400, "sturdy wooden box, with many space"),
    ("Cat Condo Complex", "tree", 500, "spacious and fun, a playground for cats"),
    ("Scratching Log", "scratcher", 350, "natural and rough, a paw-approved workout"),
    ("Burger Cushion", "cushion", 320, "plump, comfy, and full of layers to snuggle"),
    ("Xmas Bucket", "bucket", 75, "festive and jolly, full of holiday spirit"),
    ("Luxury Treasure Box", "box", 800, "sturdy wooden box, Ornate, gleaming, fit for a king/queen"),
    ("Blue Bucket", "bucket", 60, "simple and sturdy, perfect for anything"),
    ("Beige Cushion", "cushion", 100, "simple, calm, and easy to snuggle"),
    ("Stump House", "house", 400, "hollow and snug, a perfect little hideaway"),
    ("Maple Pillow", "pillow", 220, "warm, orange-toned, with autumn vibes"),
    ("FLuffy Cushion", "cushion", 170, "soft, puffy, perfect for a catnap"),
    ("Egg Bed", "bed", 550, "curvy and soft, like a cozy little nest"),
    ("Scratching Post", "scratcher", 200, "tall and sturdy, perfect for sharpening claws"),
    ("Two-tier Cat Tree", "tree", 240, "simple, sturdy, two comfy levels to climb"),
    ("Pumpkin House", "house", 250, "round, warm, and perfect for a snug cat nap"),
    ("Yellow Hamock", "hamock", 210, "bright and comfy, perfect for lazy stretches");

-- Populates Player inventory with money
INSERT INTO Player_Inventory(player_id, money)
VALUES
    (1, 100),
    (2, 300),
    (3, 200),
    (4, 400),
    (5, 150),
    (6, 500),
    (7, 370),
    (8, 420),
    (9, 210),
    (10, 480);

-- Populates owned cats, gives each player a cat
INSERT INTO Owned_Cat(date_arrived, player_id, cat_id)
VALUES
    ('2025-10-05', 1, 3),
    ('2025-10-18', 2, 12),
    ('2025-10-22', 3, 7),
    ('2025-10-09', 4, 19),
    ('2025-10-27', 5, 2),
    ('2025-11-03', 6, 14),
    ('2025-11-11', 7, 9),
    ('2025-11-06', 8, 3),
    ('2025-11-12', 9, 19),
    ('2025-11-27', 10, 8);

-- Populate Owned Food
INSERT INTO Owned_Food(player_id, food_name, quantity)
VALUES
    (1, 'Beef_Kibble', 2),
    (1, 'Tuna_Bites', 1),
    (2, 'Chicken_Shreds', 3),
    (3, 'Fish_Biscuits', 1),
    (3, 'Mackerel_Stew', 2),
    (4, 'Milk_Pudding', 4),
    (5, 'Ocean_Treats', 1),
    (5, 'Salmon_Jelly', 3),
    (6, 'Seafood_Deluxe', 2),
    (7, 'Shrimp_Bowl', 1),
    (7, 'Tuna_Bites', 4),
    (8, 'Beef_Kibble', 3),
    (9, 'Chicken_Shreds', 2),
    (10, 'Fish_Biscuits', 1),
    (10, 'Mackerel_Stew', 4);

-- Populate Food Preferences
INSERT INTO Food_Preference(cat_breed, food_name)
VALUES
    ('Bengal', 'Beef_Kibble'),
    ('Bengal', 'Chicken_Shreds'),
    ('Bengal', 'Fish_Biscuits'),

    ('British Shorthair', 'Mackerel_Stew'),
    ('British Shorthair', 'Milk_Pudding'),
    ('British Shorthair', 'Ocean_Treats'),

    ('Domestic Shorthair', 'Salmon_Jelly'),
    ('Domestic Shorthair', 'Seafood_Deluxe'),
    ('Domestic Shorthair', 'Shrimp_Bowl'),

    ('Maine Coon', 'Tuna_Bites'),
    ('Maine Coon', 'Beef_Kibble'),
    ('Maine Coon', 'Chicken_Shreds'),

    ('Persian', 'Fish_Biscuits'),
    ('Persian', 'Mackerel_Stew'),
    ('Persian', 'Milk_Pudding'),

    ('Ragdoll', 'Ocean_Treats'),
    ('Ragdoll', 'Salmon_Jelly'),
    ('Ragdoll', 'Seafood_Deluxe'),

    ('Siamese', 'Shrimp_Bowl'),
    ('Siamese', 'Tuna_Bites'),
    ('Siamese', 'Beef_Kibble');

SELECT name FROM Furniture;

INSERT INTO Owned_Furniture(player_id, furniture_name, quantity)
VALUES
    (1, 'Bamboo House', 1),
    (2, 'Luxury Treasure Box', 1),
    (3, 'Pumpkin House', 1),
    (4, 'Scratching Log', 1),
    (5, 'FLuffy Bed', 1),
    (6, 'Sakura Pillow', 1),
    (7, 'Cat Condo Complex', 1),
    (8, 'Egg Bed', 1),
    (9, 'Maple Pillow', 1),
    (10, 'Two-tier Cat Tree', 1);

SELECT * FROM Furniture_Preference;
INSERT INTO Furniture_Preference(cat_breed, furniture_name)
VALUES
    ('Bengal', 'Bamboo House'),
    ('Bengal', 'FLuffy Cushion'),
    ('Bengal', 'Cat Condo Complex'),

    ('British Shorthair', 'Luxury Treasure Box'),
    ('British Shorthair', 'Pumpkin House'),
    ('British Shorthair', 'Scratching Log'),

    ('Domestic Shorthair', 'Egg Bed'),
    ('Domestic Shorthair', 'Two-tier Cat Tree'),
    ('Domestic Shorthair', 'Maple Pillow'),

    ('Maine Coon', 'Sakura Pillow'),
    ('Maine Coon', 'Blue Bucket'),
    ('Maine Coon', 'Treasure Box'),

    ('Persian', 'Flat Cardboard'),
    ('Persian', 'Chocolate Tart Bed'),
    ('Persian', 'Scratching Post'),

    ('Ragdoll', 'Small Shopping Box'),
    ('Ragdoll', 'Burger Cushion'),
    ('Ragdoll', 'Lucky Cushion'),

    ('Siamese', 'Yellow Hamock'),
    ('Siamese', 'Red Bucket'),
    ('Siamese', 'Grass Cushion');

INSERT INTO Placed_Furniture(player_id, position_placed, furniture_name)
VALUES
    (1, 2, 'Luxury Treasure Box'),
    (2, 5, 'Bamboo House'),
    (3, 1, 'Sakura Pillow'),
    (4, 3, 'Chocolate Tart Bed'),
    (5, 4, 'Cat Condo Complex'),
    (6, 2, 'Lucky Cushion'),
    (7, 1, 'FLuffy Bed'),
    (8, 5, 'Bamboo House'),
    (9, 3, 'Small Shopping Box'),
    (10, 4, 'Egg Bed');

INSERT INTO Food_Eaten(owned_cat_id, food_name, time_fed)
VALUES
    (1, 'Beef_Kibble', '2025-11-03 08:30:00'),
    (1, 'Chicken_Shreds', '2025-11-03 12:00:00'),
    (1, 'Fish_Biscuits', '2025-11-04 09:15:00'),

    (2, 'Mackerel_Stew', '2025-10-18 10:00:00'),
    (2, 'Milk_Pudding', '2025-10-19 18:45:00'),

    (3, 'Tuna_Bites', '2025-10-22 07:50:00'),
    (3, 'Ocean_Treats', '2025-10-22 13:30:00'),
    (3, 'Salmon_Jelly', '2025-10-23 19:10:00'),

    (4, 'Shrimp_Bowl', '2025-10-09 09:00:00'),
    (4, 'Seafood_Deluxe', '2025-10-10 14:20:00'),

    (5, 'Beef_Kibble', '2025-10-27 08:15:00'),
    (5, 'Tuna_Bites', '2025-10-27 12:40:00'),
    (5, 'Fish_Biscuits', '2025-10-28 16:05:00'),

    (6, 'Milk_Pudding', '2025-11-03 09:30:00'),
    (6, 'Mackerel_Stew', '2025-11-03 13:00:00'),

    (7, 'Chicken_Shreds', '2025-11-06 08:45:00'),

    (8, 'Fish_Biscuits', '2025-11-06 07:50:00'),
    (8, 'Tuna_Bites', '2025-11-07 12:20:00'),

    (9, 'Beef_Kibble', '2025-11-12 08:00:00'),
    (9, 'Milk_Pudding', '2025-11-12 14:00:00'),
    (9, 'Fish_Biscuits', '2025-11-13 09:30:00'),
    (9, 'Chicken_Shreds', '2025-11-14 18:15:00'),

    (10, 'Ocean_Treats', '2025-11-27 10:10:00'),
    (10, 'Salmon_Jelly', '2025-11-28 12:45:00');

INSERT INTO Furniture_Occupancy(placed_furniture_id, owned_cat_id, time_occupied)
VALUES
    (1, 1, '2025-11-03'),
    (1, 1, '2025-11-06'),

    (2, 2, '2025-10-18'),
    (2, 2, '2025-10-22'),
    (2, 2, '2025-11-01'),

    (3, 3, '2025-10-22'),
    (3, 3, '2025-10-25'),

    (4, 4, '2025-10-09'),
    (4, 4, '2025-10-14'),

    (5, 5, '2025-10-27'),
    (5, 5, '2025-11-02'),
    (5, 5, '2025-11-06'),

    (6, 6, '2025-11-03'),
    (6, 6, '2025-11-08'),

    (7, 7, '2025-11-06'),
    (7, 7, '2025-11-10'),
    (7, 7, '2025-11-14'),

    (8, 8, '2025-11-06'),
    (8, 8, '2025-11-11'),

    (9, 9, '2025-11-12'),
    (9, 9, '2025-11-16'),
    (9, 9, '2025-11-20'),

    (10, 10, '2025-11-27'),
    (10, 10, '2025-12-01');


-- Player Report
CREATE OR REPLACE VIEW player_report AS
SELECT
    p.id,
    p.username,
    pi.money,
    COUNT(DISTINCT oc.id) AS total_cats,
    COALESCE(SUM(ofd.quantity), 0) AS total_food_items,
    COALESCE(SUM(ofu.quantity), 0) AS total_furniture_items
FROM Player p
         LEFT JOIN Player_Inventory pi ON p.id = pi.player_id
         LEFT JOIN Owned_cat oc ON p.id = oc.player_id
         LEFT JOIN Owned_Food ofd ON p.id = ofd.player_id
         LEFT JOIN Owned_Furniture ofu ON p.id = ofu.player_id
GROUP BY p.id, p.username, pi.money;


-- Cat Report

CREATE OR REPLACE VIEW Cat_Report AS
SELECT
    oc.id AS owned_cat_id,
    c.name AS cat_name,
    c.breed,
    DAY(COALESCE(fe.time_fed, fo.time_occupied)) AS day,
    CONCAT(MONTH(COALESCE(fe.time_fed, fo.time_occupied)), '/', YEAR(COALESCE(fe.time_fed, fo.time_occupied))) AS month_year,
    COUNT(DISTINCT fe.food_name) AS distinct_foods_eaten,
    COUNT(fe.food_name) AS total_food_consumptions,
    COUNT(DISTINCT f.name) AS distinct_furniture_used,
    COUNT(fo.placed_furniture_id) AS total_furniture_uses
FROM Owned_cat oc
    JOIN Cat c ON oc.cat_id = c.id
    LEFT JOIN Food_eaten fe ON oc.id = fe.owned_cat_id
    AND YEAR(fe.time_fed) = 2025 AND MONTH(fe.time_fed) = 11
    LEFT JOIN Furniture_Occupancy fo ON oc.id = fo.owned_cat_id
    AND YEAR(fo.time_occupied) = 2025 AND MONTH(fo.time_occupied) = 11
    LEFT JOIN Placed_Furniture pf ON fo.placed_furniture_id = pf.id
    LEFT JOIN Furniture f ON pf.furniture_name = f.name
WHERE (fe.owned_cat_id IS NOT NULL OR fo.owned_cat_id IS NOT NULL)
GROUP BY oc.id, c.name, c.breed, day, month_year
ORDER BY month_year DESC, day DESC, owned_cat_id;


-- Food Report View
CREATE OR REPLACE VIEW Food_Report AS
SELECT
    f.name AS food_name,
    f.price,
    f.description,
    COALESCE(SUM(ofd.quantity), 0) AS total_owned,
    COALESCE(COUNT(fe.food_name), 0) AS total_times_eaten
FROM Food f
         LEFT JOIN Owned_Food ofd ON f.name = ofd.food_name
         LEFT JOIN Food_eaten fe ON f.name = fe.food_name
GROUP BY f.name, f.price, f.description;

-- Generate Reports based on data inputs --

-- Furniture Report View
CREATE OR REPLACE VIEW Furniture_Report AS
SELECT
    fn.name AS furniture_name,
    fn.description,
    COALESCE(SUM(ofu.quantity), 0) AS total_owned,
    COALESCE(COUNT(pf.id), 0) AS total_times_placed,
    COALESCE(COUNT(fo.owned_cat_id), 0) AS total_cat_occupancy
FROM Furniture fn
         LEFT JOIN Owned_Furniture ofu ON fn.name = ofu.furniture_name
         LEFT JOIN Placed_Furniture pf ON fn.name = pf.furniture_name
         LEFT JOIN Furniture_Occupancy fo ON pf.id = fo.placed_furniture_id
GROUP BY fn.name, fn.description;
-- challenge ratings
INSERT INTO ChallengeRating(id, cr, xp, proficiencyBonus) VALUES
    (1, '0', 0, 2),
    (2, '0', 10, 2),
    (3, '1/8', 25, 2),
    (4, '1/4', 50, 2),
    (5, '1/2', 100, 2),
    (6, '1', 200, 2),
    (7, '2', 450, 2),
    (8, '3', 700, 2),
    (9, '4', 1100, 2),
    (10, '5', 1800, 3),
    (11, '6', 2300, 3),
    (12, '7', 2900, 3),
    (13, '8', 3900, 3),
    (14, '9', 5000, 4),
    (15, '10', 5900, 4),
    (16, '11', 7200, 4),
    (17, '12', 8400, 4),
    (18, '13', 10000, 5),
    (19, '14', 11500, 5),
    (20, '15', 13000, 5),
    (21, '16', 15000, 5),
    (22, '17', 18000, 6),
    (23, '18', 20000, 6),
    (24, '19', 22000, 6),
    (25, '20', 25000, 6),
    (26, '21', 33000, 7),
    (27, '22', 41000, 7),
    (28, '23', 50000, 7),
    (29, '24', 62000, 7),
    (30, '25', 75000, 8),
    (31, '26', 90000, 8),
    (32, '27', 105000, 8),
    (33, '28', 120000, 8),
    (34, '29', 135000, 9),
    (35, '30', 155000, 9);

-- campaign
INSERT INTO Campaign(id, name, madness, active) VALUES
    (1, "Darkest Dungeons & Dragons", 1, 1);
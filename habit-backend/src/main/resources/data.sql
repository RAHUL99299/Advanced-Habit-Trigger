-- =============================================================================
-- Habit Trigger Analyzer — Seed Data
-- 2 demo users, 3 habits each, 60 days of varied logs
-- Password for both users: "password123" (BCrypt hashed)
-- =============================================================================

-- Demo User 1: Alice Johnson
INSERT INTO users (name, email, password, created_at) VALUES
('Alice Johnson', 'alice@demo.com', '$2a$10$XuVi./d8bnp59Jfbz0MqN.vLZGRJ69Kkj7sT8q9I7xjkxkXdDxQO', NOW() - INTERVAL 90 DAY);

-- Demo User 2: Bob Smith
INSERT INTO users (name, email, password, created_at) VALUES
('Bob Smith', 'bob@demo.com', '$2a$10$XuVi./d8bnp59Jfbz0MqN.vLZGRJ69Kkj7sT8q9I7xjkxkXdDxQO', NOW() - INTERVAL 90 DAY);

-- =============================================================================
-- Alice's Habits
-- =============================================================================
INSERT INTO habits (user_id, name, category, target_frequency, created_at) VALUES
(1, 'Morning Run', 'Fitness', 'daily', NOW() - INTERVAL 80 DAY),
(1, 'Meditation', 'Mindfulness', 'daily', NOW() - INTERVAL 75 DAY),
(1, 'Read 30 Minutes', 'Learning', 'daily', NOW() - INTERVAL 70 DAY);

-- =============================================================================
-- Bob's Habits
-- =============================================================================
INSERT INTO habits (user_id, name, category, target_frequency, created_at) VALUES
(2, 'Evening Workout', 'Health', 'daily', NOW() - INTERVAL 80 DAY),
(2, 'Journal Writing', 'Mindfulness', 'daily', NOW() - INTERVAL 75 DAY),
(2, 'Learn Guitar', 'Creativity', 'daily', NOW() - INTERVAL 70 DAY);

-- =============================================================================
-- Alice: Morning Run Logs (habit_id=1) — 60 days
-- Pattern: completes 85% on sunny mornings at home, only 40% on rainy days
-- =============================================================================
INSERT INTO habit_logs (habit_id, log_date, completed, mood, weather, location, time_of_day, notes) VALUES
(1, CURDATE() - INTERVAL 60 DAY, true,  'motivated', 'sunny',  'home',    'morning',   'Great run!'),
(1, CURDATE() - INTERVAL 59 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 58 DAY, false, 'tired',     'rainy',  'home',    'morning',   'Too tired, skipped'),
(1, CURDATE() - INTERVAL 57 DAY, true,  'calm',      'sunny',  'outdoors','morning',   'Good pace'),
(1, CURDATE() - INTERVAL 56 DAY, true,  'motivated', 'cloudy', 'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 55 DAY, false, 'stressed',  'rainy',  'home',    'morning',   'Work stress'),
(1, CURDATE() - INTERVAL 54 DAY, true,  'happy',     'sunny',  'outdoors','morning',   'Personal best!'),
(1, CURDATE() - INTERVAL 53 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 52 DAY, false, 'tired',     'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 51 DAY, true,  'calm',      'cloudy', 'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 50 DAY, true,  'happy',     'sunny',  'outdoors','morning',   'Sunrise run!'),
(1, CURDATE() - INTERVAL 49 DAY, false, 'stressed',  'rainy',  'home',    'morning',   'Rained heavily'),
(1, CURDATE() - INTERVAL 48 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 47 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 46 DAY, false, 'tired',     'rainy',  'home',    'morning',   'Skipped'),
(1, CURDATE() - INTERVAL 45 DAY, true,  'calm',      'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 44 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 43 DAY, false, 'tired',     'rainy',  'home',    'evening',   'Too late'),
(1, CURDATE() - INTERVAL 42 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 41 DAY, true,  'motivated', 'cloudy', 'home',    'morning',   'Cloudy but went anyway'),
(1, CURDATE() - INTERVAL 40 DAY, true,  'calm',      'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 39 DAY, false, 'stressed',  'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 38 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 37 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 36 DAY, false, 'tired',     'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 35 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 34 DAY, true,  'calm',      'cloudy', 'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 33 DAY, false, 'stressed',  'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 32 DAY, true,  'motivated', 'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 31 DAY, true,  'happy',     'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 30 DAY, false, 'tired',     'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 29 DAY, true,  'calm',      'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 28 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 27 DAY, false, 'stressed',  'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 26 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 25 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 24 DAY, false, 'tired',     'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 23 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 22 DAY, true,  'calm',      'cloudy', 'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 21 DAY, false, 'stressed',  'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 20 DAY, true,  'motivated', 'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 19 DAY, true,  'happy',     'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 18 DAY, false, 'tired',     'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 17 DAY, true,  'calm',      'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 16 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 15 DAY, false, 'stressed',  'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 14 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 13 DAY, true,  'motivated', 'cloudy', 'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 12 DAY, false, 'tired',     'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 11 DAY, true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 10 DAY, true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 9 DAY,  true,  'calm',      'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 8 DAY,  true,  'happy',     'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 7 DAY,  true,  'motivated', 'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 6 DAY,  true,  'calm',      'cloudy', 'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 5 DAY,  true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 4 DAY,  true,  'motivated', 'sunny',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 3 DAY,  false, 'tired',     'rainy',  'home',    'morning',   NULL),
(1, CURDATE() - INTERVAL 2 DAY,  true,  'happy',     'sunny',  'outdoors','morning',   NULL),
(1, CURDATE() - INTERVAL 1 DAY,  true,  'motivated', 'sunny',  'home',    'morning',   NULL);

-- =============================================================================
-- Alice: Meditation Logs (habit_id=2)
-- Pattern: best in evenings at home, worst when stressed
-- =============================================================================
INSERT INTO habit_logs (habit_id, log_date, completed, mood, weather, location, time_of_day, notes) VALUES
(2, CURDATE() - INTERVAL 55 DAY, true,  'calm',     'sunny',  'home',  'evening', '10 min session'),
(2, CURDATE() - INTERVAL 54 DAY, true,  'happy',    'cloudy', 'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 53 DAY, false, 'stressed', 'rainy',  'office','morning', 'Too distracted'),
(2, CURDATE() - INTERVAL 52 DAY, true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 51 DAY, true,  'happy',    'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 50 DAY, false, 'stressed', 'rainy',  'office','morning', NULL),
(2, CURDATE() - INTERVAL 49 DAY, true,  'calm',     'cloudy', 'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 48 DAY, true,  'motivated','sunny',  'home',  'evening', 'Long session'),
(2, CURDATE() - INTERVAL 47 DAY, false, 'stressed', 'rainy',  'office','morning', NULL),
(2, CURDATE() - INTERVAL 46 DAY, true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 45 DAY, true,  'happy',    'cloudy', 'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 44 DAY, false, 'tired',    'rainy',  'home',  'night',   'Too tired'),
(2, CURDATE() - INTERVAL 43 DAY, true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 42 DAY, true,  'happy',    'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 41 DAY, false, 'stressed', 'rainy',  'office','morning', NULL),
(2, CURDATE() - INTERVAL 40 DAY, true,  'calm',     'cloudy', 'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 39 DAY, true,  'motivated','sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 38 DAY, false, 'tired',    'rainy',  'home',  'morning', NULL),
(2, CURDATE() - INTERVAL 37 DAY, true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 36 DAY, true,  'happy',    'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 35 DAY, true,  'calm',     'cloudy', 'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 34 DAY, false, 'stressed', 'rainy',  'office','morning', NULL),
(2, CURDATE() - INTERVAL 33 DAY, true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 32 DAY, true,  'happy',    'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 31 DAY, false, 'tired',    'rainy',  'home',  'night',   NULL),
(2, CURDATE() - INTERVAL 30 DAY, true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 7 DAY,  true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 6 DAY,  true,  'happy',    'cloudy', 'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 5 DAY,  true,  'calm',     'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 4 DAY,  true,  'motivated','sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 3 DAY,  true,  'calm',     'cloudy', 'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 2 DAY,  true,  'happy',    'sunny',  'home',  'evening', NULL),
(2, CURDATE() - INTERVAL 1 DAY,  true,  'calm',     'sunny',  'home',  'evening', NULL);

-- =============================================================================
-- Alice: Read 30 Minutes (habit_id=3)
-- Pattern: reads best at night in calm mood, skips when stressed/busy
-- =============================================================================
INSERT INTO habit_logs (habit_id, log_date, completed, mood, weather, location, time_of_day, notes) VALUES
(3, CURDATE() - INTERVAL 40 DAY, true,  'calm',     'cloudy', 'home',    'night',    'Great chapter'),
(3, CURDATE() - INTERVAL 39 DAY, true,  'happy',    'sunny',  'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 38 DAY, false, 'stressed', 'rainy',  'office',  'afternoon',NULL),
(3, CURDATE() - INTERVAL 37 DAY, true,  'calm',     'cloudy', 'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 36 DAY, true,  'happy',    'sunny',  'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 35 DAY, false, 'tired',    'rainy',  'home',    'evening',  'Fell asleep'),
(3, CURDATE() - INTERVAL 34 DAY, true,  'calm',     'cloudy', 'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 33 DAY, true,  'happy',    'sunny',  'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 32 DAY, false, 'stressed', 'rainy',  'office',  'afternoon',NULL),
(3, CURDATE() - INTERVAL 31 DAY, true,  'calm',     'cloudy', 'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 30 DAY, true,  'motivated','sunny',  'outdoors','afternoon', NULL),
(3, CURDATE() - INTERVAL 7 DAY,  true,  'calm',     'cloudy', 'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 6 DAY,  true,  'happy',    'sunny',  'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 5 DAY,  true,  'calm',     'cloudy', 'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 4 DAY,  false, 'stressed', 'rainy',  'home',    'evening',  NULL),
(3, CURDATE() - INTERVAL 3 DAY,  true,  'calm',     'sunny',  'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 2 DAY,  true,  'happy',    'cloudy', 'home',    'night',    NULL),
(3, CURDATE() - INTERVAL 1 DAY,  true,  'calm',     'sunny',  'home',    'night',    NULL);

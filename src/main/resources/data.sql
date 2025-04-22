-- 1. Clear the data
--DELETE FROM user_events;
--DELETE FROM app_users;
--DELETE FROM events;

-- 2. Reset ID sequences
--ALTER SEQUENCE app_users_id_seq RESTART WITH 1;
--ALTER SEQUENCE events_id_seq RESTART WITH 1;

-- 3. Insert new data
INSERT INTO app_users (email, username) VALUES
('john.doe@example.com', 'john_doe'),
('jane.smith@example.com', 'jane_smith'),
('mike.jones@example.com', 'mike_jones');

INSERT INTO events (title, description, location, event_date) VALUES
('Spring Festival', 'A fun-filled festival celebrating the arrival of spring!', 'City Park', '2025-05-01 10:00:00'),
('Tech Conference', 'A conference focusing on the latest in technology and innovation.', 'Convention Center', '2025-06-15 09:00:00'),
('Charity Gala', 'An elegant event to raise funds for local charities.', 'Grand Ballroom', '2025-07-20 19:00:00');

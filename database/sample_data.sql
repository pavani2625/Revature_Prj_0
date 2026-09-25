CREATE DATABASE movie_ticket_db;
USE movie_ticket_db;
INSERT INTO users
(name, email, phone, password, role)
VALUES
('Admin User', 'admin@movieticket.com', '9876543210', 'admin123', 'ADMIN'),
('Rahul Kumar', 'rahul@gmail.com', '9876543211', 'rahul123', 'CUSTOMER'),
('Priya Sharma', 'priya@gmail.com', '9876543212', 'priya123', 'CUSTOMER');

INSERT INTO movies
(title, language, genre, duration, release_date)
VALUES
('Interstellar', 'English', 'Science Fiction', 169, '2014-11-07'),
('RRR', 'Telugu', 'Action', 182, '2022-03-25'),
('The Dark Knight', 'English', 'Action', 152, '2008-07-18'),
('Pushpa 2', 'Telugu', 'Action', 200, '2024-12-05');
 
INSERT INTO theatres
(name, city, address, total_seats)
VALUES
('PVR Cinemas', 'Hyderabad', 'Banjara Hills', 100),
('INOX', 'Hyderabad', 'Gachibowli', 80),
('AMB Cinemas', 'Hyderabad', 'Gachibowli', 120);

INSERT INTO seats
(theatre_id, seat_number, seat_type, price)
VALUES
(1, 'A1', 'REGULAR', 150.00),
(1, 'A2', 'REGULAR', 150.00),
(1, 'A3', 'PREMIUM', 200.00),
(1, 'A4', 'PREMIUM', 200.00),

(2, 'B1', 'REGULAR', 120.00),
(2, 'B2', 'REGULAR', 120.00),
(2, 'B3', 'PREMIUM', 180.00),
(2, 'B4', 'PREMIUM', 180.00),

(3, 'C1', 'REGULAR', 150.00),
(3, 'C2', 'REGULAR', 150.00),
(3, 'C3', 'PREMIUM', 220.00),
(3, 'C4', 'PREMIUM', 220.00);

INSERT INTO shows
(theatre_id, movie_id, show_date, start_time, end_time)
VALUES
(1, 1, '2026-09-25', '10:00:00', '13:00:00'),
(1, 2, '2026-09-25', '14:00:00', '17:00:00'),
(2, 3, '2026-09-25', '11:00:00', '13:30:00'),
(2, 4, '2026-09-25', '18:00:00', '21:20:00'),
(3, 1, '2026-09-26', '15:00:00', '18:00:00');

INSERT INTO bookings
(show_id, user_id, booking_date, total_amount, booking_status)
VALUES
(1, 2, NOW(), 300.00, 'CONFIRMED'),
(2, 3, NOW(), 400.00, 'CONFIRMED'),
(3, 2, NOW(), 240.00, 'CONFIRMED');
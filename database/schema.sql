USE movie_ticket_db;

CREATE TABLE users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       phone VARCHAR(15),
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL
);

CREATE TABLE movies (
                        movie_id INT PRIMARY KEY AUTO_INCREMENT,
                        title VARCHAR(200) NOT NULL,
                        language VARCHAR(50) NOT NULL,
                        genre VARCHAR(100),
                        duration INT NOT NULL,
                        release_date DATE
);

CREATE TABLE theatres (
                          theatre_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(150) NOT NULL,
                          city VARCHAR(100) NOT NULL,
                          address VARCHAR(255),
                          total_seats INT NOT NULL
);

CREATE TABLE seats (
                       seat_id INT PRIMARY KEY AUTO_INCREMENT,
                       theatre_id INT NOT NULL,
                       seat_number VARCHAR(20) NOT NULL,
                       seat_type VARCHAR(30) NOT NULL,
                       price DECIMAL(10,2) NOT NULL,

                       CONSTRAINT fk_seats_theatre
                           FOREIGN KEY (theatre_id)
                               REFERENCES theatres(theatre_id)
);

CREATE TABLE shows (
                       show_id INT PRIMARY KEY AUTO_INCREMENT,
                       theatre_id INT NOT NULL,
                       movie_id INT NOT NULL,
                       show_date DATE NOT NULL,
                       start_time TIME NOT NULL,
                       end_time TIME NOT NULL,

                       CONSTRAINT fk_shows_theatre
                           FOREIGN KEY (theatre_id)
                               REFERENCES theatres(theatre_id),

                       CONSTRAINT fk_shows_movie
                           FOREIGN KEY (movie_id)
                               REFERENCES movies(movie_id)
);

CREATE TABLE bookings (
                          booking_id INT PRIMARY KEY AUTO_INCREMENT,
                          show_id INT NOT NULL,
                          user_id INT NOT NULL,
                          booking_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                          total_amount DECIMAL(10,2) NOT NULL,
                          booking_status VARCHAR(30) NOT NULL,

                          CONSTRAINT fk_bookings_show
                              FOREIGN KEY (show_id)
                                  REFERENCES shows(show_id),

                          CONSTRAINT fk_bookings_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(user_id)
);

CREATE TABLE booked_seats (
                              booked_seat_id INT PRIMARY KEY AUTO_INCREMENT,
                              seat_id INT NOT NULL,
                              booking_id INT NOT NULL,

                              CONSTRAINT fk_booked_seats_seat
                                  FOREIGN KEY (seat_id)
                                      REFERENCES seats(seat_id),

                              CONSTRAINT fk_booked_seats_booking
                                  FOREIGN KEY (booking_id)
                                      REFERENCES bookings(booking_id)
);

CREATE TABLE payments (
                          payment_id INT PRIMARY KEY AUTO_INCREMENT,
                          booking_id INT NOT NULL UNIQUE,
                          amount DECIMAL(10,2) NOT NULL,
                          payment_method VARCHAR(30),
                          payment_status VARCHAR(30),
                          payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_payments_booking
                              FOREIGN KEY (booking_id)
                                  REFERENCES bookings(booking_id)
);
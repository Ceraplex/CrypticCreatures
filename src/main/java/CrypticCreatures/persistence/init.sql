-- Create Profile Page Table
CREATE TABLE profile_pages
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    about_me TEXT
);

-- Create Users Table
CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_page_id INT UNIQUE,
    CONSTRAINT fk_profile_page FOREIGN KEY (profile_page_id)
        REFERENCES profile_pages (id)
        ON DELETE SET NULL
);

-- Create Cards Table
CREATE TABLE cards
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dmg INT NOT NULL CHECK (dmg >= 0),
    element_type VARCHAR(50),
    monster_type VARCHAR(50)
);

-- Create User_Cards Table to link Users and Cards (Stack system)
CREATE TABLE user_cards
(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    card_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE
);

-- Create Decks Table
CREATE TABLE decks
(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create Deck_Cards Table to associate cards with decks
CREATE TABLE deck_cards
(
    id SERIAL PRIMARY KEY,
    deck_id INT NOT NULL,
    card_id INT NOT NULL,
    position INT NOT NULL, -- Optional: position in the deck

    FOREIGN KEY (deck_id) REFERENCES decks (id) ON DELETE CASCADE,
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE
);

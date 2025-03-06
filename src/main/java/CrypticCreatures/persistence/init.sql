-- Create Users Table
CREATE TABLE users
(
    uid SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    money INT NOT NULL CHECK (money >= 0),
    elo INT NOT NULL CHECK (elo >= 0)
);

-- Create Cards Table
CREATE TABLE cards
(
    cid VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dmg INT NOT NULL CHECK (dmg >= 0),
    element_type VARCHAR(50) NOT NULL ,
    monster_type VARCHAR(50) NOT NULL
);

-- Create Profile Page Table
CREATE TABLE profile_pages
(
    pid INT PRIMARY KEY REFERENCES users(uid) ON DELETE CASCADE,
    firstname VARCHAR(255) NOT NULL ,
    lastname VARCHAR(255) NOT NULL ,
    image BYTEA
);

CREATE TABLE decks (
    uid INT REFERENCES users(uid) ON DELETE CASCADE,
    cid VARCHAR(255) REFERENCES cards(cid) ON DELETE CASCADE,
    PRIMARY KEY (uid, cid)
);

CREATE TABLE stacks (
    uid INT REFERENCES users(uid) ON DELETE CASCADE,
    cid VARCHAR(255) REFERENCES cards(cid) ON DELETE CASCADE,
    PRIMARY KEY (uid, cid)
);

-- Create Packages Table
CREATE TABLE packages
(
    pid SERIAL PRIMARY KEY,
    price INT NOT NULL CHECK (price >= 0)
);

-- Associate Cards with Packages
CREATE TABLE package_cards
(
    package_id INT NOT NULL,
    card_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (package_id, card_id),
    FOREIGN KEY (package_id) REFERENCES packages(pid) ON DELETE CASCADE,
    FOREIGN KEY (card_id) REFERENCES cards(cid) ON DELETE CASCADE
);

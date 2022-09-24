CREATE TABLE heroes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    save_path VARCHAR(255) NOT NULL,
    experience INTEGER NOT NULL DEFAULT 0,
    class VARCHAR(255) NOT NULL
);

CREATE TABLE hero_current_artefact (
    id BIGSERIAL PRIMARY KEY,
    hero_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    rarity VARCHAR(255) NOT NULL,
    attack INTEGER NOT NULL,
    defense INTEGER NOT NULL,
    hitpoints INTEGER NOT NULL,
    slot VARCHAR(255) NOT NULL,
    CONSTRAINT forein_key_hero_current_artefact FOREIGN KEY (hero_id) REFERENCES heroes(id) ON DELETE CASCADE
);

CREATE TABLE hero_current_enemies(
    id BIGSERIAL PRIMARY KEY,
    hero_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    rank VARCHAR(255) NOT NULL,
    level INTEGER NOT NULL,
    attack INTEGER NOT NULL,
    defense INTEGER NOT NULL,
    hitpoints INTEGER NOT NULL,
    CONSTRAINT forein_key_hero_current_enemy FOREIGN KEY (hero_id) REFERENCES heroes(id) ON DELETE CASCADE
);

CREATE TABLE hero_artefacts (
    id BIGSERIAL PRIMARY KEY,
    hero_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    rarity VARCHAR(255) NOT NULL,
    attack INTEGER NOT NULL,
    defense INTEGER NOT NULL,
    hitpoints INTEGER NOT NULL,
    slot VARCHAR(255) NOT NULL,
    CONSTRAINT forein_key_hero_artefact FOREIGN KEY (hero_id) REFERENCES heroes(id) ON DELETE CASCADE
);


CREATE TABLE hero_map (
    id BIGSERIAL PRIMARY KEY,
    hero_id INTEGER NOT NULL,
    level INTEGER NOT NULL,
    size INTEGER NOT NULL,
    CONSTRAINT forein_key_hero_map FOREIGN KEY (hero_id) REFERENCES heroes(id) ON DELETE CASCADE
);

CREATE TABLE hero_map_locations (
    id BIGSERIAL PRIMARY KEY,
    hero_map_id INTEGER NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL,
    biome VARCHAR(255) NOT NULL,
    enemies_visible BOOLEAN DEFAULT 'false',
    visible BOOLEAN DEFAULT 'false',
    CONSTRAINT forein_key_hero_map_location FOREIGN KEY (hero_map_id) REFERENCES hero_map(id) ON DELETE CASCADE
);

CREATE TABLE hero_map_location_enemies (
    hero_map_location_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    rank VARCHAR(255) NOT NULL,
    level INTEGER NOT NULL,
    attack INTEGER NOT NULL,
    defense INTEGER NOT NULL,
    hitpoints INTEGER NOT NULL,
    CONSTRAINT forein_key_hero_map_location_enemy FOREIGN KEY (hero_map_location_id) REFERENCES hero_map_locations(id) ON DELETE CASCADE
);

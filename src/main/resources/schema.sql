CREATE TABLE IF NOT EXISTS users (
  id int8 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar,
  email varchar
);

CREATE TABLE IF NOT EXISTS requests (
  id int8 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  description varchar,
  requester_id int8 REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items (
	id int8 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar,
	description varchar,
	is_available boolean,
	owner_id int8 REFERENCES users (id),
	request_id int8 REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings (
	id int8 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	start_date TIMESTAMP WITHOUT TIME ZONE,
	end_date TIMESTAMP WITHOUT TIME ZONE,
	item_id int8 REFERENCES items (id),
	booker_id int8 REFERENCES users (id),
	status varchar
);

CREATE TABLE IF NOT EXISTS comments (
	id int8 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	text varchar,
	item_id int8 REFERENCES items (id),
	author_id int8 REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS todo (
	id IDENTITY
	,title TEXT NOT NULL
	,details TEXT
	,finished BOOLEAN NOT NULL
);
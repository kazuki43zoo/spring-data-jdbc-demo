CREATE TABLE IF NOT EXISTS todo (
	id IDENTITY
	,title TEXT NOT NULL
	,details TEXT
	,finished BOOLEAN NOT NULL
	,created_at TIMESTAMP
	,created_by VARCHAR(64)
	,last_updated_at TIMESTAMP
	,last_updated_by VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS activity (
	id IDENTITY
	,todo_id INTEGER NOT NULL
	,sort_order INTEGER NOT NULL
	,content TEXT NOT NULL
	,at TIMESTAMP NOT NULL
);


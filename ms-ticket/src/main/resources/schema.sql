CREATE TABLE IF NOT EXISTS tickets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    
    owner_id INTEGER NOT NULL, 
    recipient_id INTEGER NOT NULL,
    manager_id INTEGER,
    
    object TEXT NOT NULL,
    action TEXT NOT NULL,
    details TEXT NOT NULL,
    local TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'TODO',
    cancel_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ticket_observers (
    ticket_id  INTEGER NOT NULL REFERENCES tickets(id),
    user_id  INTEGER NOT NULL, 
    PRIMARY KEY(ticket_id, user_id)
);
INSERT INTO bank_account (owner, balance) VALUES
('Alice', 1000.00),
('Bob', 2500.50),
('Charlie', 0.00)
ON CONFLICT DO NOTHING;
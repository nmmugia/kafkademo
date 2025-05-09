CREATE TABLE IF NOT EXISTS insurance_policy (
    id SERIAL PRIMARY KEY,
    policy_holder_name VARCHAR(255) NOT NULL,
    coverage_amount NUMERIC(19,2) NOT NULL,
    premium NUMERIC(19,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);
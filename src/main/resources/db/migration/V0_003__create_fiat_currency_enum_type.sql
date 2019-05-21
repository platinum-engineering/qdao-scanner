DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'fiat_currency') THEN
        CREATE TYPE fiat_currency AS ENUM ('USD', 'KRW');
    END IF;
END$$;


ALTER TABLE contracts ADD COLUMN fiat_currency fiat_currency DEFAULT 'USD';
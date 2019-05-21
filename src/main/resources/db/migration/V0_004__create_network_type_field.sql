DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'network_type') THEN
        CREATE TYPE network_type AS ENUM ('MAINNET', 'ROPSTEN', 'RINKEBY', 'KOVAN', 'GOERLI');
    END IF;
END$$;


ALTER TABLE contracts ADD COLUMN network_type network_type DEFAULT 'MAINNET';
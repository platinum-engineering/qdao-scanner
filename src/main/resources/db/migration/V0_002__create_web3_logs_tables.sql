DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'currency') THEN
        CREATE TYPE currency AS ENUM ('BTC');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loan_status') THEN
        CREATE TYPE loan_status AS ENUM ('WAITING_MINTING', 'STABLE', 'WAITING_BURNING', 'BURNED', 'WAITING_BUYOUT', 'BUYOUT', 'UNKNOWN');
    END IF;
END$$;

-- Information about smart-contract
CREATE TABLE contracts (
    uid bigserial PRIMARY KEY,

    address varchar(256) NOT NULL,
    name varchar(256),
    symbol varchar(10),

    total_supply numeric DEFAULT 0,
    decimals integer default 18,

    created_at timestamp without time zone default (now() at time zone 'utc'),
	updated_at timestamp without time zone default (now() at time zone 'utc')
);

-- Contract transfer log
CREATE TABLE transfers_log (
    uid bigserial PRIMARY KEY,

    contract_uid bigint references contracts(uid),

    address_from varchar(256) NOT NULL,
    address_to varchar(256) NOT NULL,
    value numeric DEFAULT 0,
    hash varchar(256),

    created_at timestamp without time zone default (now() at time zone 'utc'),
	updated_at timestamp without time zone default (now() at time zone 'utc')
);

-- Loans log
CREATE TABLE loans (
    uid bigserial PRIMARY KEY,
    transfers_log_uid bigint references transfers_log(uid),

    eth_address varchar(256),
    token_value numeric,

    collateralized_currency currency,
    collateralized_address varchar(256),
    collateralized_value numeric,
    collateralized_rate numeric,
    collateralized_start_index real, -- Start of percent

    liquidation_index real,
    liquidation_rate numeric,
    liquidation_fee numeric, -- In currency

    difference_to_customer numeric, -- In currency

    status loan_status DEFAULT 'UNKNOWN',

    created_at timestamp without time zone default (now() at time zone 'utc'),
	updated_at timestamp without time zone default (now() at time zone 'utc')
);


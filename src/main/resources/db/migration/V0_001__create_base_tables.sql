DO $$
BEGIN
    -- Create user role (permission) type.
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'app_roles') THEN
        CREATE TYPE app_roles AS ENUM ('ROLE_GUEST', 'ROLE_CLIENT', 'ROLE_MANAGER', 'ROLE_DEVELOPER');
    END IF;
END$$;

CREATE TABLE users (
    uid bigserial PRIMARY KEY,

    display_name varchar(256),
    email varchar(256) UNIQUE,

    password varchar(256),

    roles app_roles[] DEFAULT '{"ROLE_CLIENT"}',

    created_at timestamp without time zone default (now() at time zone 'utc'),
	updated_at timestamp without time zone default (now() at time zone 'utc')
);

INSERT INTO users (display_name, email, password, roles) VALUES ('Nikolay', 'contact@nskrash.ru', '$2a$10$7ZYaVEQ0Iqt/WmhViRhNmuiQIxRrOYp7gfPdHOa/sMfNK90LHFtbO', '{ "ROLE_DEVELOPER" }');
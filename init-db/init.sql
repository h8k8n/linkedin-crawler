-- Veritabanı oluşturma
CREATE DATABASE linkedin_crawler;

-- Bağlanma
\c linkedin_crawler

-- Şema oluşturma (daha detaylı)
DO $$
BEGIN
    IF NOT EXISTS (SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'crwm') THEN
        CREATE SCHEMA crwm;
    END IF;

    IF NOT EXISTS (SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'infm') THEN
        CREATE SCHEMA infm;
    END IF;
END $$;

-- Yetkilendirme
GRANT ALL PRIVILEGES ON SCHEMA crwm TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA infm TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA crwm TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA infm TO postgres;
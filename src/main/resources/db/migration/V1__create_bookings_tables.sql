CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE bookings(
    uuid UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID NOT NULL,
    poi_id UUID NOT NULL,
    booking_time TIMESTAMPTZ NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
)

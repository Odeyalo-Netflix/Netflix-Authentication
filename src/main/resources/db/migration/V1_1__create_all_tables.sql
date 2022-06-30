create table qr_codes
(
    id            serial  not null,
    client_id     varchar(32),
    expiry_time   timestamp,
    is_activated  boolean not null,
    qr_code_value varchar(255),
    primary key (id)
);
create table refresh_tokens
(
    id            int4      not null,
    expire_date   timestamp not null,
    refresh_token varchar(50),
    user_id       int4,
    primary key (id)
);
create table user_roles
(
    user_id int4 not null,
    roles   varchar(255)
);
create table users
(
    id                   serial not null,
    auth_provider        varchar(25),
    email                varchar(40),
    image                varchar(3000),
    is_account_activated boolean,
    is_user_banned       boolean,
    nickname             varchar(20),
    password             varchar(3000),
    phone_number         varchar(40),
    primary key (id)
);
create table verification_codes
(
    id           serial  not null,
    code_value   varchar(20),
    expired      timestamp,
    is_activated boolean not null,
    user_id      int4,
    primary key (id)
);

alter table if exists refresh_tokens
    add constraint unique_refresh_token_value unique (refresh_token);

alter table if exists users
    add constraint unique_user_email unique (email);

alter table if exists users
    add constraint unique_user_nickname unique (nickname);

alter table if exists users
    add constraint user_phone_fk unique (phone_number);

alter table if exists refresh_tokens
    add constraint user_refresh_token_fk foreign key (user_id) references users on delete cascade;

alter table if exists user_roles
    add constraint user_role_fk foreign key (user_id) references users;

alter table if exists verification_codes
    add constraint user_verification_code_fk foreign key (user_id) references users

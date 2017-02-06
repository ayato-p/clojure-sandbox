create table users
  ( id uuid primary key default uuid_generate_v4(),
    username text not null,
    password text not null );

--

create table blogs
  ( id uuid primary key default uuid_generate_v4(),
    name text not null,
    user_id uuid constraint user_id_fk references users(id) );

--

create table posts
  ( id uuid primary key default uuid_generate_v4(),
    title text not null,
    content text not null,
    blog_id uuid constraint blog_id_fk references blogs(id) );

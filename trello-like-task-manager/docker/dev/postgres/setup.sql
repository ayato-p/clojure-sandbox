create role demo with createdb login;
create database demo owner demo;

\connect demo
create schema extensions;

grant usage on schema extensions to public;
grant execute on all functions in schema extensions to public;

alter default privileges in schema extensions
  grant execute on functions to public;

alter default privileges in schema extensions
  grant usage on types to public;

create extension if not exists "uuid-ossp" schema extensions;

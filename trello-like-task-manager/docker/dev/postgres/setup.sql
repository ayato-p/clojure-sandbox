create role demo with createdb login;
create database demo owner demo;
create database demo_test owner demo;

\connect demo
create extension if not exists "uuid-ossp";

\connect demo_test
create extension if not exists "uuid-ossp";

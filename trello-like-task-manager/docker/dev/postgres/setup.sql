create role demo with createdb login;
create database demo owner demo;

\connect demo
create extension if not exists "uuid-ossp";

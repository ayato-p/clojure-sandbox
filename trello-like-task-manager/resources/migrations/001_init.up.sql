create table tasks(
  task_uuid uuid primary key default uuid_generate_v4()
  ,task_title text not null
);

--;;

create table task_orders(
  task_uuid uuid unique not null references tasks(task_uuid)
  ,task_order int not null
);

--;;

create table boards(
  board_uuid uuid primary key default uuid_generate_v4()
  ,board_name text not null
);

--;;

create table board_orders(
  board_uuid uuid unique not null references boards(board_uuid)
  ,board_order int not null
);

--;;

create table task_groups(
  task_uuid uuid unique not null references tasks(task_uuid)
  ,board_uuid uuid not null references boards(board_uuid)
);

(ns demo.query
  (:require [demo.db :as db]
            [clojure.string :as str]))

(def ^:private find-tasks*
  "select t.task_uuid, t.task_title, t_pos.task_order, b.board_uuid, b.board_name, b_ord.board_order
  from tasks as t
  left join task_positions as t_pos
  on t_pos.task_uuid = t.task_uuid
  left join boards as b
  on b.board_uuid = t_pos.board_uuid
  left join board_orders as b_ord
  on b_ord.board_uuid = b.board_uuid
  order by board_order, task_order")

(defn find-tasks [db]
  (db/fetch db find-tasks*))

(defn add-board [db board-name]
  (db/execute db ["insert into boards (board_name) values (?)" board-name]))

(defn remove-board [db board-uuid]
  (db/execute db ["delete from boards where board_uuid = ?" board-uuid]))

(defn add-task [db task-title]
  (db/execute db ["insert into tasks (task_title) values (?)" task-title]))

(defn remove-task [db task-uuid]
  (db/execute db ["delete from tasks where task_uuid = ?" task-uuid]))

(defn- reorder-tasks [db task-uuid inc-or-dec]
  (db/execute db [(str "update task_positions set task_order = task_order "
                       (if (= :inc inc-or-dec)
                         "+ 1"
                         "- 1")
                       " where board_uuid = (select board_uuid from task_positions where task_uuid = ?)"
                       " and task_order > (select task_order from task_positions where task_uuid = ?)")
                  task-uuid task-uuid]))

(defn peel-task-off [db task-uuid]
  (db/with-db-transaction [db (db/->db-spec db) {:isolation :serializable}]
    (reorder-tasks db task-uuid :dec)
    (db/execute db ["delete from task_positions where task_uuid = ?" task-uuid])))

(defn put-task-on-board
  ([db task-uuid board-uuid]
   (put-task-on-board db task-uuid board-uuid nil))
  ([db task-uuid board-uuid to-task-uuid]
   (db/with-db-transaction [db (db/->db-spec db) {:isolation :serializable}]
     (if to-task-uuid
       (do (reorder-tasks db to-task-uuid :inc)
           (db/execute db [(str
                            "insert into task_positions (task_uuid, board_uuid, task_order) values"
                            " (?, ?, (select count(task_uuid) from task_positions where board_uuid = ? "
                            " and task_order <= (select task_order from task_positions where task_uuid = ?)))") task-uuid board-uuid board-uuid to-task-uuid]))
       (db/execute db [(str
                        "insert into task_positions (task_uuid, board_uuid, task_order) values"
                        " (?, ?, (select count(task_uuid) from task_positions where board_uuid = ?))") task-uuid board-uuid board-uuid])))))

(defn reput-task-on-board
  ([db task-uuid board-uuid]
   (reput-task-on-board db task-uuid board-uuid nil))
  ([db task-uuid board-uuid to-task-uuid]
   (db/with-db-transaction [db (db/->db-spec db) {:isolation :serializable}]
     (peel-task-off db task-uuid)
     (put-task-on-board db task-uuid board-uuid to-task-uuid))))

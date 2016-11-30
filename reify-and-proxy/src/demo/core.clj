(ns demo.core
  (:import demo.example.Greeting))

(import demo.example.Greeting)

(.hello (Greeting. "ayato_p"))
;; "Hello, ayato_p"

(let [user-name "ayato-p"]
  (.hello
   (proxy [Greeting] [user-name]
     (hello []))))

(proxy [Greeting] ["ayato-p"]
  (hello [] (str "Hi, "(.name this))))
;; "Hi, ayato-p"

(.hello
 (proxy [Greeting] ["ayato-p"]
   (hello [] (str (proxy-super hello) "!!"))))
;; "Hello, ayato-p!!"

(ancestors
 (class
  (proxy [Greeting] ["ayato-p"]
    (hello [] (str "Hi, "(.name this))))))
;; #{clojure.lang.IProxy demo.example.Greeting java.lang.Object}

(defprotocol IByeBye
  (bye-bye [this]))

(extend-protocol IByeBye
  Object
  (bye-bye [this]
    "Bye-bye, anonymous")

  Greeting
  (bye-bye [this]
    (str "Bye-bye, " (.name this))))

(bye-bye (Object.))
;; "Bye-bye, anonymous"

(bye-bye (Greeting. "ayato_p"))
;; "Bye-bye, ayato_p"

(bye-bye (proxy [Greeting] ["ayato_p"]))
;; "Bye-bye, ayato_p"

(bye-bye
 (reify IByeBye
   (bye-bye [this] "Bye Bye 👋")))
;; "Bye Bye 👋"

(ancestors
 (class (reify
          IByeBye
          (bye-bye [this] "Bye Bye 👋")

          clojure.lang.IFn
          (invoke [this] ))))


(reify
  IByeBye
  (bye-bye [this] "Bye Bye 👋")

  clojure.lang.IFn
  (invoke [this] "INVOKED!!"))

(meta ^{:foo true} (Greeting. "ayato-p"))
;; nil

(meta ^{:foo true} (reify IByeBye))
;; {:line 63, :column 7, :foo true}


(def r
  (reify
    IByeBye
    (bye-bye [this] "Bye Bye 👋")

    clojure.lang.IFn
    (invoke [this] "INVOKED!!")

    Object
    (toString [this] "reified")))

(bye-bye r)
;; "Bye Bye 👋"

(r)
;; "INVOKED!!"

(str r)
;; "reified"

(def r
  (reify
    IByeBye
    (bye-bye [this] "Bye Bye 👋")

    clojure.lang.IFn
    (invoke [this] "INVOKED!!")

    Object
    (toString [this] "reified")))


(defprotocol Marker)

(def marked (reify Marker))

(satisfies? Marker marked)


(defprotocol IMailer
  (send-mail [mailer title content]))

(defrecord Mailer []
  IMailer
  (send-mail [_ title content]
    ;; ...
    ))

(defn new-mailer-mock []
  (reify IMailer
    (send-mail [_ title content])))

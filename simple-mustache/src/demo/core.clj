(ns demo.core
  (:require [instaparse.core :as insta]
            [clojure.string :as str]))

(defn escape-delimiter [delimiters]
  (str/replace delimiters #"([\{\}\[\]\<\>\|])" "\\\\$1"))

(defn gen-spec [open-tag close-tag]
  (str
   "
mustache         ::= (variable | change-tag | text | space)*
variable         ::= open-tag space? name space? close-tag
open-tag         ::= #'" (escape-delimiter open-tag) "'
close-tag        ::= #'" (escape-delimiter close-tag) "'
name             ::= #'[\\w&$\\|=]+' ('.' name)*
change-tag       ::= open-change-tag space? ' ' space? close-change-tag #'(?:.|\\r?\\n)*'
open-change-tag  ::= #'" (escape-delimiter open-tag) "=(?:\\s*)?[^\\s=]+'
close-change-tag ::= #'[^\\s=]+(?:\\s*)?=" (escape-delimiter close-tag) "'
<text>           ::= #'(?![^\\s]*(?:" (escape-delimiter open-tag) "|" (escape-delimiter close-tag) "))[^\\s]*'
<space>          ::= #'\\s*'
"))

(def default-parser
  (insta/parser (gen-spec "{{" "}}")))

(defprotocol MustacheRenderer
  (-render [this data]))

(defmulti render-node (fn [node data] (first node)))

(defmethod render-node :default
  [node data]
  "")

(defmethod render-node :mustache
  [node data]
  (some-> (reduce (fn [sb tree]
                    (.append sb (-render tree data)))
                  (StringBuilder. (dec (count node)))
                  (next node))
          (.toString)))

(defn- find-name [v]
  (->> v
       (filter vector?)
       (filter (comp #(= :name %) first))
       first))

(defn- name-node->kw-vec [node]
  (reduce (fn [v [_ x]]
            (conj v (keyword x)))
          [(keyword (second node))]
          (filter vector? (next node))))

(defmethod render-node :variable
  [node data]
  (str (some->> node
                next
                find-name
                name-node->kw-vec
                (get-in data))))

(defmethod render-node :change-tag
  [node data]
  (let [[_ [_ open] _ [_ close] txt] node
        open-tag (str/trim (second (str/split open #"=")))
        close-tag (str/trim (first (str/split close #"=")))]
    (-> (gen-spec open-tag close-tag)
        insta/parser
        (insta/parse txt)
        (-render data))))

(extend-protocol MustacheRenderer
  java.lang.String
  (-render [s _] s)

  clojure.lang.IPersistentVector
  (-render [v data]
    (render-node v data)))

(defn render [txt data]
  (-> (insta/parse default-parser txt)
      (-render data)))

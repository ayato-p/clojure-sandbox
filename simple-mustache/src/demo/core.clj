(ns demo.core
  (:require [instaparse.core :as insta]
            [clojure.string :as str]))

(defn escape-delimiter [delimiters]
  (str/replace delimiters #"([\{\}])" "\\\\$1"))

(defn gen-spec [open-tag close-tag]
  (str
   "
mustache      ::= (variable | change-tag | text | space)*
variable      ::= open-tag name close-tag
open-tag      ::= '" open-tag "'
close-tag     ::= '" close-tag "'
name          ::= #'[^\\s{}=]+'
<text>        ::= #'(?![^\\s]*(?:" (escape-delimiter open-tag) "|" (escape-delimiter close-tag) "))[^\\s]*'
<space>       ::= #'\\s*'
change-tag    ::= #'\\{\\{=' new-open-tag ' ' new-close-tag #'=\\}\\}' #'.*\\z'
new-open-tag  ::= #'[^\\s=]+'
new-close-tag ::= #'[^\\s=]+'
"))

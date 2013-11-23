(ns destruct.core)

; (use 'destruct.core :reload-all)

; Explanation of abbreviations:
;
;   * 'd'  means 'destructuring example'
;   * 'd1' means 'destructuring example #1'
;   * 'k'  means 'the example uses :keys'
;   * 'a'  means 'the example uses :as'
;   * 'aa' means 'the example uses :as twice'
;   * 'kk' means 'the example uses :keys twice'
;     (but such examples do not work)

; Let's start with a simple example.
(defn d1
  [{a :a
    b :b}]
  (prn "a" a)
  (prn "b" b))
; (d1 {:a 1 :b 2})
; "a" 1
; "b" 2
; nil

; Let's add `:keys`.
(defn d1k
  [{:keys [a b]}]
  (prn "a" a)
  (prn "b" b))
; (d1k {:a 1 :b 2})
; "a" 1
; "b" 2
; nil

; Let's modify `d1` to use `:as`.
(defn d1a
  [{a :a
    b :b
    :as m}]
  (prn "m" m)
  (prn "a" a)
  (prn "b" b))
; (d1a {:a 1 :b 2})
; "m" {:a 1, :b 2}
; "a" 1
; "b" 2

; Let's modify `d1k` to use `:as`.
(defn d1ka
  [{:keys [a b] :as m}]
  (prn "m" m)
  (prn "a" a)
  (prn "b" b))
; (d1ka {:a 1 :b 2})
; "m" {:a 1, :b 2}
; "a" 1
; "b" 2

; Let's add nesting to `d1`.
(defn d2
  [{a :a
    {b1 :b1 b2 :b2} :b}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2 {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b1" 3
; "b2" 4

; Let's use `:as`.
(defn d2a
  [{a :a
    {b1 :b1 b2 :b2} :b
   :as m}]
  (prn "m" m)
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2a {:a 1 :b {:b1 3 :b2 4}})
; "m" {:a 1, :b {:b2 4, :b1 3}}
; "a" 1
; "b1" 3
; "b2" 4

; Let's use `:as` twice.
(defn d2aa
  [{a :a
    {b1 :b1 b2 :b2 :as b} :b
   :as m}]
  (prn "m" m)
  (prn "a" a)
  (prn "b" b)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2aa {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b1" 3
; "b2" 4
; "b" {:b2 4, :b1 3}
; "m" {:a 1, :b {:b2 4, :b1 3}}

; Let's change `d2` to use `:keys`.
(defn d2k
  [{a :a
    {:keys [b1 b2]} :b}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2k {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b1" 3
; "b2" 4

; Let's change `d2k` to use `:as`.
(defn d2ka
  [{a :a
    {:keys [b1 b2] :as b} :b}]
  (prn "a" a)
  (prn "b" b)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2ka {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b" {:b2 4, :b1 3}
; "b1" 3
; "b2" 4

; Let's use `:as` twice.
(defn d2kaa
  [{a :a
    {:keys [b1 b2] :as b} :b
    :as m}]
  (prn "a" a)
  (prn "b" b)
  (prn "b1" b1)
  (prn "b2" b2)
  (prn "m" m))
; (d2kaa {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b" {:b2 4, :b1 3}
; "b1" 3
; "b2" 4
; "m" {:a 1, :b {:b2 4, :b1 3}}

; Can we nest `:keys` inside `:keys`?
(defn d2kk!
  [{:keys [a {:keys [b1 b2]}]}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2kk! {:a 1 :b {:b1 3 :b2 4}})
;
; It does not work:
; "a" 1
; "b1" nil
; "b2" nil
;
; But how could it work? It has no way of knowing about `:b`.

; Next, let's try to add `:b` somehow.
(defn d2kka!
  [{:keys [a {:keys [b1 b2] :as b}]}]
  (prn "a" a)
  (prn "b" b)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2kka! {:a 1 :b {:b1 3 :b2 4}})
;
; It does not work:
; "a" 1
; "b" nil
; "b1" nil
; "b2" nil
;
; You can see that we didn't really tell it about `:b`.
; We told it about `b`, hoping that it could figure out `:b` too.
;
; Clojure had enough information (right?) but didn't do it.
; Perhaps there is room for improvement in Clojure destructuring?

; Or perhaps there is another way?

; There is, thanks to a suggestion from Karsten Schmidt on the Clojure
; mailing list:
; https://groups.google.com/forum/#!topic/clojure/t8qAzzhRuos

; This is how you nest `:keys` multiple times:
(defn d2kka
  [{:keys [a]
    {:keys [b1 b2] :as b} :b}]
  (prn "a" a)
  (prn "b" b)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2kka {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b" {:b2 4, :b1 3}
; "b1" 3
; "b2" 4

; Note that you don't literally nest `:keys` inside the vector.

; Let's use `:as` twice to get the entire data structure as well:
(defn d2kkaa
  [{:keys [a]
    {:keys [b1 b2] :as b} :b
   :as m}]
  (prn "m" m)
  (prn "a" a)
  (prn "b" b)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2kkaa {:a 1 :b {:b1 3 :b2 4}})
; "m" {:a 1, :b {:b2 4, :b1 3}}
; "a" 1
; "b" {:b2 4, :b1 3}
; "b1" 3
; "b2" 4

; I think that walks through map destructuring in plenty of detail.
; Let me know if you have questions or suggestions.

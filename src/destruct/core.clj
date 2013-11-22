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

(defn d1
  [{a :a
    b :b}]
  (prn "a" a)
  (prn "b" b))
; (d1 {:a 1 :b 2})
; "a" 1
; "b" 2
; nil

(defn d1k
  [{:keys [a b]}]
  (prn "a" a)
  (prn "b" b))
; (d1k {:a 1 :b 2})
; "a" 1
; "b" 2
; nil

(defn d1a
  [{a :a
    b :b
    :as m}]
  (prn "a" a)
  (prn "b" b)
  (prn "m" m))
; (d1a {:a 1 :b 2})
; "a" 1
; "b" 2
; "m" {:a 1, :b 2}

(defn d1ka
  [{:keys [a b] :as m}]
  (prn "a" a)
  (prn "b" b)
  (prn "m" m))
; (d1ka {:a 1 :b 2})
; "a" 1
; "b" 2
; "m" {:a 1, :b 2}

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

(defn d2a
  [{a :a
    {b1 :b1 b2 :b2} :b
   :as m}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2)
  (prn "m" m))
; (d2a {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b1" 3
; "b2" 4
; "m" {:a 1, :b {:b2 4, :b1 3}}

(defn d2aa
  [{a :a
    {b1 :b1 b2 :b2 :as b} :b
   :as m}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2)
  (prn "b" b)
  (prn "m" m))
; (d2aa {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b1" 3
; "b2" 4
; "b" {:b2 4, :b1 3}
; "m" {:a 1, :b {:b2 4, :b1 3}}

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

(defn d2ka
  [{a :a
    {:keys [b1 b2] :as b} :b}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2)
  (prn "b" b))
; (d2ka {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b1" 3
; "b2" 4
; "b" {:b2 4, :b1 3}

(defn d2kaa
  [{a :a
    {:keys [b1 b2] :as b} :b
    :as m}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2)
  (prn "b" b)
  (prn "m" m))
; (d2kaa {:a 1 :b {:b1 3 :b2 4}})
; "a" 1
; "b1" 3
; "b2" 4
; "b" {:b2 4, :b1 3}
; "m" {:a 1, :b {:b2 4, :b1 3}}

(defn d2kk1!
  [{:keys [a {:keys [b1 b2]}]}]
  (prn "a" a)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2kk1! {:a 1 :b {:b1 3 :b2 4}})
;
; It does not work:
; "a" 1
; "b1" nil
; "b2" nil
;
; But how could it work? It has no way of knowing about `:b`.
; So, next, let's try to add `:b` somehow.

(defn d2kk2!
  [{:keys [a {:keys [b1 b2] :as b}]}]
  (prn "a" a)
  (prn "b" b)
  (prn "b1" b1)
  (prn "b2" b2))
; (d2kk2! {:a 1 :b {:b1 3 :b2 4}})
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

; ----------

; To compare and conclude:
;
; 1. Both `d2aa` and `d2kaa` show how to destructure while keeping
;    all intermediate data structures.
; 2. `d2kaa` is only slightly more compact than `d2aa`.

; ----------

; To generalize and comment about nested destructuring:
;
; * You cannot nest inside the `:keys` destructuring form.
; * You *can* nest inside the other destructuring forms.

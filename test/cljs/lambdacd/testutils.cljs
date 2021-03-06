(ns lambdacd.testutils
  (:require [reagent.core :as reagent :refer [atom]]))

(def isClient (not (nil? (try (.-document js/window)
                              (catch js/Object e nil)))))

(defn add-test-div [name]
  (let [doc     js/document
        body    (.-body js/document)
        div     (.createElement doc "div")]
    (.appendChild body div)
    div))

(defn with-mounted-component [comp f]
  (when isClient
    (let [div (add-test-div "_testreagent")]
      (let [comp (reagent/render-component comp div #(f comp div))]
        (reagent/unmount-component-at-node div)
        (reagent/flush)
        (.removeChild (.-body js/document) div)))))

(defn path []
  (.-pathname (.-location js/window)))

(defn query []
  (.-search (.-location js/window)))

(defn contains-value? [v coll]
  (some #(= % v) coll))

; mocking

(defn mock-subscriptions [values]
  (fn [[q] _]
    (atom (q values))))
(ns mastermind.core-test
  (:require [clojure.test :refer :all]
            [mastermind.core :refer :all]))

(deftest color-and-position-matches-exact-matches-only
	(is (= 0 (color-and-position-matches [1 1 1 1] [2 3 4 5])) "Different numbers entirely")
  (is (= 0 (color-and-position-matches [1 2 3 4] [3 4 2 1])) "Same numbers but wrong positions")
  (is (= 1 (color-and-position-matches [1 1 1 1] [1 2 3 4])) "One match for simple secret")
  (is (= 1 (color-and-position-matches [1 2 3 4] [1 3 4 2])) "One match for another secret")
  (is (= 2 (color-and-position-matches [1 1 1 1] [1 1 3 4])) "Two matches for simple secret")
  (is (= 2 (color-and-position-matches [1 2 3 4] [4 2 3 1])) "Two matches for another secret")
  (is (= 3 (color-and-position-matches [1 1 1 1] [1 1 1 4])) "Three matches for simple secret")
  (is (= 3 (color-and-position-matches [1 2 3 4] [5 2 3 4])) "Three matches for another secret")
  (is (= 4 (color-and-position-matches [1 1 1 1] [1 1 1 1])) "Four matches for a simple secret - a winning guess")
  (is (= 4 (color-and-position-matches [1 2 3 4] [1 2 3 4])) "Four matches for a another secret - a winning guess")
)

(deftest color-only-matches-same-color-different-positions
  (is (= 0 (color-only-matches [1 2 3 4] [5 6 5 6])) "No matches")
  (is (= 1 (color-only-matches [1 2 3 4] [5 5 5 1])) "One match")
  (is (= 1 (color-only-matches [1 2 3 4] [5 1 1 1])) "Surplus guessed")
  (is (= 1 (color-only-matches [1 1 2 2] [5 5 3 1])) "More colors in secret than guessed")
  (is (= 2 (color-only-matches [1 2 3 4] [5 1 6 2])) "Two matches")
  (is (= 3 (color-only-matches [1 2 3 4] [5 3 2 1])) "Three matches")
  (is (= 4 (color-only-matches [1 1 2 2] [2 2 1 1])) "Double colors, all matched in wrong position")
)

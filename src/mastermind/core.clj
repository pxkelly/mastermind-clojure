(ns mastermind.core)

(defn color-and-position-matches [secret guess]
  "Returns the number of elements in both the color and position"
  (let [position-pairs (map vector secret guess)]
    (reduce 
    	(fn [count [secret-value guess-value]] (+ count (if (= secret-value guess-value) 1 0))) 
    	0 position-pairs)))

(defn transpose [m]
  (if (seq m)
    (apply mapv vector m)
    (empty m)))

(defn remove-matches [a b]
  "Returns a tuple of the inputs after removing exact position and color matches"
  (let [position-pairs (map vector a b)]
    (transpose (filter (fn [[x y]] (not (= x y))) position-pairs))))

(defn color-only-matches [secret guess]
  "Returns the number of pegs with correct color but wrong position."
  (let [[secret-without-matches guess-without-matches] (remove-matches secret guess)
        secret-frequencies (frequencies secret-without-matches)
        guess-frequencies (frequencies guess-without-matches)]
    (reduce (fn [count [element frequency]] (+ count (min frequency (get guess-frequencies element 0))))
      0 secret-frequencies)))

(defn correct-guess? [secret guess]
  "True if the guess matches the secret, false otherwise"
  (= secret guess))

(defn score-guess [secret guess]
  "Returns the guess along with the score as two components - exact-match and color-only-match"
  { :guess guess
    :exact-match? (correct-guess? secret guess)
    :color-and-position-matches (color-and-position-matches secret guess)
    :color-only-matches (color-only-matches secret guess) })

(defn game-status [guessed-secret? turns-taken max-game-turns]
  "Indicate if the game was won, lost, or still playing"
  (cond (true? guessed-secret?) :won
        (< turns-taken max-game-turns) :playing
        :else :lost))

; Game Public API

(defn init-game-state [secret]
  "Creates the game data structure"
  { :max-turns 12
    :secret secret
    :turns []
    :state :playing})

(defn guess [guess game-state]
  "Return a new game applying the guess to the given game"
  (let [{:keys [secret turns max-turns]} game-state]
    (-> game-state 
      (update :turns #(conj % (score-guess secret guess)))
      (assoc :state (game-status (correct-guess? secret guess) (inc (count turns)) max-turns))
    )
  )
)

(ns app.schema )

(def router {:name nil, :title nil, :data {}, :router nil})

(def session
  {:user-id nil,
   :id nil,
   :nickname nil,
   :router (do router {:name :home, :data nil, :router nil}),
   :messages {}})

(def user {:name nil, :id nil, :nickname nil, :avatar nil, :password nil})

(def database {:sessions (do session {}), :users (do user {}), :lines {}})

(def line-config
  {:points {"0" [(rand-int 10) (rand-int 10)], "1" [(+ 100 (rand-int 10)) (rand-int 10)]},
   :width 2,
   :base [200 10]})

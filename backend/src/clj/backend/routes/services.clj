(ns backend.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [backend.controllers :as c]
    [backend.middleware.formats :as formats]
    [ring.util.http-response :refer :all]
    [clojure.java.io :as io]))

(defn service-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
    :swagger {:id ::api}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 coercion/coerce-exceptions-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc true
        :swagger {:info {:title "my-api"
                         :description "https://cljdoc.org/d/metosin/reitit"}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ["/ping"
    {:get (constantly (ok {:message "pong"}))}]
   

   ["/math"
    {:swagger {:tags ["math"]}}

    ["/plus"
     {:get {:summary "plus with spec query parameters"
            :parameters {:query {:x int?, :y int?}}
            :responses {200 {:body {:total pos-int?}}}
            :handler (fn [{{{:keys [x y]} :query} :parameters}]
                       {:status 200
                        :body {:total (+ x y)}})}
      :post {:summary "plus with spec body parameters"
             :parameters {:body {:x int?, :y int?}}
             :responses {200 {:body {:total pos-int?}}}
             :handler (fn [{{{:keys [x y]} :body} :parameters}]
                        {:status 200
                         :body {:total (+ x y)}})}}]]
    ["/login"
     {:post {:responses {200 {:body {:user-id int?}}}
             :parameters {:body {:pass string?, :username string?}}
             :handler (fn [{{{:keys [pass username]} :body} :parameters}]
                        {:status 200
                         :body {:user-id
                                (:id (c/user-in-system username pass))}})}}]
    ["/user"
     {:post {:responses {200 {:body {:user-id int? :text string?}}}
             :summary "Create a user"
             :parameters {:body {:pass string?, :username string?}}
             :handler (fn [{{{:keys [pass username]} :body} :parameters}]
                          {:status 200
                           :body (c/create-user username pass)})}}]
    ["/check-username"
     {:get {:summary "Check availability of username"
            :parameters {:query {:username string?}}
            :responses {200 {:body {:taken boolean?}}}
            :handler (fn [{{{:keys [username]} :query} :parameters}]
                       {:status 200
                        :body {:taken (c/username-available? username)}})}}]

    ["/check-password"
     {:post {:summary "Check to see if password is strong"
             :parameters {:body {:username string?}}
             :responses {200 {:body {:strong boolean?}}}
             :handler (fn [{{{:keys [pass]} :query} :parameters}]
                        {:status 200
                         :body {:strong (c/strong-password? pass)}})}}]
    ["/text"
     {:get {:summary "Get text associated with a user"
            :responses {200 {:body {:user-id int?
                                    :text    string?}}}
            :parameters {:query {:user-id int?}}
            :handler (fn [{{{:keys [user-id]} :query} :parameters}]
                       {:status 200
                        :body (-> user-id
                                  c/get-user-text
                                  (dissoc :id))})}
      :post {:summary "Update text associated with a user"
             :responses {200 {:body {:user-id int?
                                     :text    string?}}}
             :parameters {:body {:user-id int? :text string?}}
             :handler (fn [{{{:keys [user-id text]} :body} :parameters}]
                        {:status 200
                         :body (-> user-id
                                   (c/update-user-text text)
                                   (dissoc :id))})}}]

   ["/files"
    {:swagger {:tags ["files"]}}

    ["/upload"
     {:post {:summary "upload a file"
             :parameters {:multipart {:file multipart/temp-file-part}}
             :responses {200 {:body {:name string?, :size int?}}}
             :handler (fn [{{{:keys [file]} :multipart} :parameters}]
                        {:status 200
                         :body {:name (:filename file)
                                :size (:size file)}})}}]

    ["/download"
     {:get {:summary "downloads a file"
            :swagger {:produces ["image/png"]}
            :handler (fn [_]
                       {:status 200
                        :headers {"Content-Type" "image/png"}
                        :body (-> "public/img/warning_clojure.png"
                                  (io/resource)
                                  (io/input-stream))})}}]]])

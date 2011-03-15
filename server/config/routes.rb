Huntr::Application.routes.draw do
  get "quests/index"

  get "quests/show"
  
  get "js_api_key" => "users#js_api"

  get "login" => 'users#login', as: 'login'
  post 'login' => 'users#do_login'

  get "logout" => 'users#logout'
  
  match "register" => 'users#register'
  
  resources :quests
  
  # API
  namespace 'api' do
    get "login" => 'users#login'
    get "logout" => 'users#logout'
    get "register" => 'users#register'
    get "ping" => 'users#ping'
    resources :user_quests do
      resources :user_steps, as: 'steps'
    end
    resources :quests do
      resources :steps do
        get :next_seq, :on => :collection
      end
      resources :comments
      get 'nearby', :on => :collection
    end
  end

  root :to => "quests#index"

  # See how all your routes lay out with "rake routes"
end

Huntr::Application.routes.draw do
  get "login" => 'users#login', as: 'login'
  post 'login' => 'users#do_login'

  get "logout" => 'users#logout'
  
  match "register" => 'users#register'
  
  # API
  namespace 'api' do
    get "login" => 'users#login'
    get "logout" => 'users#logout'
    get "register" => 'users#register'
    get "ping" => 'users#ping'
    resources :user_quests
    resources :quests do
      resources :steps
      resources :comments
    end
  end

  # root :to => "welcome#index"

  # See how all your routes lay out with "rake routes"
end

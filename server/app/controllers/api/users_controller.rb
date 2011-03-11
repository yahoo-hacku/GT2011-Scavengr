require 'uuidtools'

module Api
  class UsersController < ApiController
    skip_before_filter :require_user, only: [:login, :register]
  
    def login
      user = User.find_by_name(params[:name])
      if user and user.pass == params[:password]
        send_token user
      else
        respond_with error: 'Invalid Username or Password'
      end
    end

    def ping
      respond_with "pong"
    end

    def logout
      @token.delete
      respond_with ''
    end
    
    def register
      user = User.new({
        name: params[:name],
        password: params[:password],
        password_confirmation: params[:password], # Don't require confirmation
        email: params[:email]
      })
      if user.save
        send_token user
      else
        respond_with({error: 'Could not create user.', validations: user.errors})
      end
    end

    private
    def send_token(user)
      token = AuthToken.create(user: user, token: UUIDTools::UUID.timestamp_create.to_s)
      token.save
      respond_with token: token.token
    end
  end
end
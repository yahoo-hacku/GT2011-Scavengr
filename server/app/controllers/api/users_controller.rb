require 'uuidtools'

module Api
  class UsersController < ApiController
    skip_before_filter :require_user, only: [:login]
  
    def login
      @user = User.find_by_name(params[:name])
      if @user and @user.pass == params[:password]
        token = AuthToken.create(user: @user, token: UUIDTools::UUID.timestamp_create.to_s)
        token.save
        respond_with token: token.token
      else
        respond_with error: 'Invalid Username or Password'
      end
    end

    def logout
      @token.delete
      respond_with ''
    end
  end
end
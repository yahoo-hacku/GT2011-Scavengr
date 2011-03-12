require 'uuidtools'
class Api::UsersController < Api::ApplicationController
  skip_before_filter :require_user, only: [:login, :register]

  def login
    cred = params[:user] || {}
    user = User.find_by_name(cred[:name])
    if user and user.pass == cred[:password]
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
    if params[:user]
      regData = params[:user]
      regData[:password_confirmation] = regData[:password] # Skip Password Verification
    end
    user = User.new(regData)
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
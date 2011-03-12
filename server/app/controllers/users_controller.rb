class UsersController < ApplicationController
  skip_before_filter :require_user, only: [:login, :do_login, :register]
  
  def login
  end
  
  def js_api
    token = AuthToken.create(user: @user, token: UUIDTools::UUID.timestamp_create.to_s)
    token.save
    respond_to do |format|
      object = {user: @user.name, key: token.token}
      format.json { render json: object }
    end
  end
  
  def do_login
    @user = User.find_by_name(params[:name])
    if @user and @user.pass == params[:password]
      session[:user_id] = @user.id
      redirect_to root_url, notice: "Login Successful"
    else
      flash[:notice] = 'Invalid Username or Password'
      redirect_to login_url
    end
  end

  def logout
    session.delete :user_id
  end

  def register
    if request.post? and params[:user]
      @user = User.new(params[:user])
      
      if @user.save
        session[:user_id] = @user.id
        
        redirect_to root_url, notice: "User created."
      end
    else
      @user = User.new
    end
  end

end

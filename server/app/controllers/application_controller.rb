class ApplicationController < ActionController::Base
  protect_from_forgery
  before_filter :require_user
  
  
  private
  def require_user
    if session[:user_id]
      @user = User.find(session[:user_id])
    else
      flash[:notice] = 'Login Required'
      redirect_to login_url
    end
  end
end

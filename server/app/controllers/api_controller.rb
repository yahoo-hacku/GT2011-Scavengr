class ApiController < ActionController::Base
  before_filter :require_user
  respond_to :json
  
  private
  def require_user
    authenticate_or_request_with_http_basic do |username, key|
      @token = AuthToken.find_by_token(key)
      if @token and @token.user.name == username
        @user = @token.user
      end
      @token and @token.user.name == username
    end
  end
end

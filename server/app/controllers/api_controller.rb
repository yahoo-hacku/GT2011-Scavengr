class ApiController < ActionController::Base
  before_filter :require_user, :map_params
  respond_to :json
  
  private
  
  def map_params
    newParams = ActiveSupport::HashWithIndifferentAccess.new
    params.each do |param, val|
      if param['.']
        subHash = newParams
        parent = subHash
        lastKey = nil
        param.split('.').each do |k|
          subHash[k] = ActiveSupport::HashWithIndifferentAccess.new if not subHash[k]
          parent = subHash
          subHash = subHash[k]
          lastKey = k
        end
        parent[lastKey] = val
      end
    end
    params.merge! newParams
  end
  
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

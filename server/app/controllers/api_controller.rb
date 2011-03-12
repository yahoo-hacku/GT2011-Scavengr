class ApiController < ActionController::Base
  before_filter :require_user, :map_params
  rescue_from ActiveRecord::RecordNotFound, :with => :record_not_found
  respond_to :json
  
  private
  
  # CRUD Helpers
  
  def create_object(object, type)
    if object.save
      respond_with object
    else
      logger.debug type.inspect
      logger.debug object.inspect
      respond_with({error: "I am a pony."})
    end
  end
  
  def show_object(object, type)
    if object
      respond_with object
    else
      respond_with error: "No Such #{type}"
    end
  end
  
  def update_object(object, type, p)
    if not object
      respond_with error: "No Such #{type}"
    elsif object.update_attributes(p)
      respond_with object
    else
      respond_to do |format|
        format.json { }
        format.xml { }
        format.html { }
      end
    end
  end
  
  def delete_object(object, type)
    if object 
      object.destroy
      respond_with ""
    else
      respond_with error: "No Such #{type}"
    end
  end
  
  # Error Handling
  
  def record_not_found
    render text: {error: "Entry does not exist."}.to_json
  end
  
  # filters
  
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

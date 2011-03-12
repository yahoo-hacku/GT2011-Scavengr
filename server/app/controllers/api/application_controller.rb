class Api::ApplicationController < ActionController::Base
  before_filter :require_user, :map_params

  rescue_from ActiveRecord::RecordNotFound, :with => :smart_error_response
  rescue_from ActiveRecord::RecordInvalid, :with => :smart_error_response

  respond_to :json, :xml

  def show
    respond_with(@active_object)
  end

  def update
    @active_object.attributes = @active_params
    @active_object.save!
    respond_to do |format| 
      format.json { render json: @active_object }
      format.xml { render xml: @active_object }
    end
  end
  alias :create :update

  def destroy
    @active_object.destroy
    respond_with(@active_object)
  end

  private

  # Error Handling

  def smart_error_response(e)
    message = { error: '' }
    status = 400
    if e.respond_to?(:record) && e.record.errors
      message[:validations] = e.record.errors.full_messages
      message[:error] = 'Record Invalid'
      status = 422
    else
      message[:error] = 'Record Not Found'
      status = 404
    end
    respond_to do |format| 
      format.json { render :json => message, :status => status }
      format.xml { render :xml => message, :status => status }
    end    
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
class Api::ApplicationController < ActionController::Base
  before_filter :require_user, :map_params

  rescue_from ActiveRecord::RecordNotFound, :with => :record_not_found
  rescue_from ActiveRecord::RecordInvalid, :with => :record_invalid

  respond_to :json, :xml

  private

  # CRUD Helpers

  def show_object(object)
    logger.debug("Showing #{@object_type}")
    return respond_with(object)
  end

  def update_object(object, p)
    logger.debug("#{object.new_record? ? 'Creating' : 'Updating'} #{@object_type}")
    object.attributes = p
    object.save!
    return respond_with(object)
  end

  def delete_object(object, type)
    logger.debug("Showing #{@object_type}")
    object.destroy
    return respond_with(object)
  end

  # Error Handling

  def record_not_found
    respond_with({error: "Entry does not exist."})
  end

  def record_invalid(e)
    respond_with(e.record)
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
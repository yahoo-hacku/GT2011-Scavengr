class Api::QuestsController < Api::ApplicationController
  
  def index
    respond_with @user.quests
  end

  def nearby
    args = [:lat, :lon, :within].map do |s|
      params[s].to_f
    end
    respond_with Quest.nearby(*args)
  end

  private

  def setup_api
    @active_params = params[:quest]
    if params[:action] == 'create'
      @active_object = @user.quests.build
    else
      @active_object = @user.quests.find(params[:id]) 
    end
  end
end

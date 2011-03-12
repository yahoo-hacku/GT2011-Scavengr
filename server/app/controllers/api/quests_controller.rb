class Api::QuestsController < Api::ApplicationController
  before_filter :setup_api, :except => [ :index ]

  def index
    respond_with @user.quests
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
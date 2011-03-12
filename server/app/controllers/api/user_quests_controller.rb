class Api::UserQuestsController < Api::ApplicationController
  before_filter :setup_api, :except => [ :index ]

  def index
    respond_with @user.user_quests
  end

  private

  def setup_api
    @active_params = params[:quest]
    case params[:action]
    when 'create'
      @active_object = @user.user_quests.build
    else
      @active_object = @user.user_quests.find(params[:id])
    end
  end
end
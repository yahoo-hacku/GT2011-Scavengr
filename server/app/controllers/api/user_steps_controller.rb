class Api::UserStepsController < Api::ApplicationController
  before_filter :setup_api, :except => [ :index ]

  def index
    @quest = @user.user_quests.find(params[:user_quest_id])
    respond_with @quest.user_steps
  end

  private

  def setup_api
    @quest = @user.user_quests.find(params[:user_quest_id])
    @active_params = params[:step]
    if params[:action] == 'create'
      @active_object = @quest.user_steps.build
    else
      @active_object = @quest.user_steps.find(params[:id])
    end
  end
end
class Api::StepsController < Api::ApplicationController
  before_filter :setup_api, :except => [ :index ]

  def index
    @quest = @user.quests.find(params[:quest_id])
    respond_with @quest.steps
  end

  private

  def setup_api
    @quest = @user.quests.find(params[:quest_id])
    @active_params = params[:step]
    if params[:action] == 'create'
      @active_object = @quest.steps.build
    else
      @active_object = @quest.steps.find(params[:id])
    end
  end
end
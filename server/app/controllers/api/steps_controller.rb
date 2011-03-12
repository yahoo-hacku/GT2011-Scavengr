class Api::StepsController < Api::ApplicationController
  before_filter :setup_api, :except => [ :index ]

  def index
    @quest = @user.quests.find(params[:quest_id])
    respond_with @quest.steps
  end

  private

  def setup_api
    @quest = @user.quests.find(params[:quest_id])
    @active_object = params[:id] ? @quest.find(params[:id]) : @quest.steps.build
    @active_params = params[:step]
  end
end
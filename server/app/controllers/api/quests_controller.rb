class Api::QuestsController < Api::ApplicationController
  before_filter :setup_api, :except => [ :index ]

  def index
    respond_with @user.quests
  end

  private

  def setup_api
    @active_object = params[:id] ? @user.quests.find(params[:id]) : @user.quests.build
    @active_params = params[:quest]
  end
end
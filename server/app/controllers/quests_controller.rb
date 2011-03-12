class QuestsController < ApplicationController
  def index
    @quests = @user.quests
  end

  def show
    @quest = @user.quests.find(params[:id])
  end
end

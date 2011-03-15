class Api::CommentsController < Api::ApplicationController

  def index
    @quest = @user.quests.find(params[:quest_id])
    respond_with @quest.comments
  end

  private

  def setup_api
    @quest = @user.quests.find(params[:quest_id])
    @active_params = params[:comment]
    if params[:action] == 'create'
      @active_object = @quest.comments.build(:user => @user)
    else
      @active_object = @quest.comments.find(params[:id])
    end
  end
end
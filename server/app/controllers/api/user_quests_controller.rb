class Api::UserQuestsController < Api::ApplicationController
  before_filter :set_object_type
  before_filter :load_user_quest, :only => [ :create, :show, :update, :destroy ]

  def index
    respond_with @user.user_quests
  end

  def show
    respond_with @user_quest
  end

  def update
    update_object @user_quest, params[:quest]
  end
  alias :create :update

  def destroy
    delete_object @user_quest
  end

  private

  def set_object_type
    @object_type = 'User Quest'
  end

  def load_user_quest
    @user_quest = params[:id] ? @user.user_quests.find(params[:id]) : @user.user_quests.build
  end
end
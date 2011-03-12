require 'uuidtools'

class Api::QuestsController < Api::ApplicationController
  before_filter :load_quest, :only => [ :create, :show, :update, :destroy ]

  def index
    respond_with @user.quests
  end

  def show
    show_object @quest
  end

  def update
    update_object @quest, params[:quest]
  end
  alias :create :update

  def destroy
    delete_object @quest
  end

  private

  def load_quest
    @quest = params[:id] ? @user.quests.find(params[:id]) : @user.quests.build
  end  
end
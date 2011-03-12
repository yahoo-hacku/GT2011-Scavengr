require 'uuidtools'

module Api
  class QuestsController < ApiController
    def index
      respond_with @user.quests
    end
    def create
      create_object @user.quests.create(params[:quest]), 'quest'
    end
    def show
      show_object @user.quests.find(params[:id]), 'quest'
    end
    def update
      update_object @user.quests.find(params[:id]), 'quest', params[:quest]
    end
    def destroy
      delete_object @user.quests.find(params[:id]), 'quest'
    end
  end
end
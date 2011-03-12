module Api
  class UserQuestsController < ApiController
    def index
      respond_with @user.user_quests
    end
    def create
      create_object @user.user_quests.create(params[:quest]), 'User Quest'
    end
    def show
      show_object @user.user_quests.find(params[:id]), 'User Quest'
    end
    def update
      update_object @user.user_quests.find(params[:id]), 'User Quest', params[:quest]
    end
    def destroy
      delete_object @user.user_quests.find(params[:id]), 'User Quest'
    end
  end
end
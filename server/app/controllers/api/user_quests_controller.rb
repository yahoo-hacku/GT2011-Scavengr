require 'uuidtools'

module Api
  class UserQuestsController < ApiController
    def index
      respond_with @user.user_quests
    end
    def create
      respond_with ""
    end
    def show
      respond_with @user.user_quests.find(params[:id])
    end
    def update
      respond_with ""
    end
    def destroy
      quest = @user.user_quests.find(params[:id])
      if quest 
        quest.destroy
        respond_with ""
      else
        respond_with error: "No Such Quest"
      end
    end
  end
end
class UserStep < ActiveRecord::Base
  belongs_to :user_quest
  belongs_to :step
end

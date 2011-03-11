class UserQuest < ActiveRecord::Base
  belongs_to :user
  belongs_to :quest
  has_many :user_steps
end

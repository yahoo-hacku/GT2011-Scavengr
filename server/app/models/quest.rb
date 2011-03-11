class Quest < ActiveRecord::Base
  belongs_to :created_by
  has_many :steps
  has_many :user_quests
  has_many :comments
end

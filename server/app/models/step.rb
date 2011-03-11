class Step < ActiveRecord::Base
  belongs_to :quest
  has_many :user_steps
end

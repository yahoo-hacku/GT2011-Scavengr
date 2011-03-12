class Comment < ActiveRecord::Base
  belongs_to :quest
  belongs_to :user
  
  validates_presence_of :text
  validates_numericality_of :rating, only_integer: true, greater_than: 0, less_than: 11, allow_nil: true
  
end

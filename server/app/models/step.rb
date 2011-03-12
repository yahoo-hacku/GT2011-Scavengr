class Step < ActiveRecord::Base
  belongs_to :quest
  has_many :user_steps
  
  validates_presence_of :clue
  validates_uniqueness_of :clue, scope: 'quest_id'
  validates_numericality_of :seq, only_integer: true, greater_than: 0
  [:lat, :long, :error_radius].each do |n|
    validates_numericality_of n, greater_than: 0
  end
end

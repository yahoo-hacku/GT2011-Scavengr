class UserQuest < ActiveRecord::Base
  
  belongs_to :user
  belongs_to :quest
  has_many :user_steps
  
  json_include :quest
  
  validates_presence_of :quest, :user
  
  before_save :default_values

  private
  
  def default_values
    self.started = Time.now unless self.started
  end
end

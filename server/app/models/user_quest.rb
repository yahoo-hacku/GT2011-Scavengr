class UserQuest < ActiveRecord::Base
  belongs_to :user
  belongs_to :quest
  has_many :user_steps
  
  
  
  validates_presence_of :quest, :user
  
  before_save :default_values

  def as_json(args)
    args ||= {}
    args[:include] = :quest
    super(args)
  end

  private
  
  def default_values
    self.started = Time.now unless self.started
  end
end

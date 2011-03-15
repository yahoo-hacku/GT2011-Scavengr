class Quest < ActiveRecord::Base
  belongs_to :created_by
  has_many :steps
  has_many :user_quests
  has_many :comments
  
  validates_presence_of :name, :description
  validates_numericality_of :time_limit, greater_than: 0, allow_nil: true
  
  before_save :default_values
  
  def to_s
    self.name
  end

  def near(lat,lon,r)
    
  end
  
  private
  
  def default_values
    self.active = true if self.active.nil?
  end
end

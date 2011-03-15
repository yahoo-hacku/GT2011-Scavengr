class Step < ActiveRecord::Base
  belongs_to :quest
  has_many :user_steps

  acts_as_mappable default_unit: :feet, lng_column_name: :lon

  default_scope order(:seq)
  
  validates_presence_of :clue
  validates_uniqueness_of :clue, scope: 'quest_id'
  validates_uniqueness_of :seq, scope: 'quest_id'
  validates_numericality_of :seq, only_integer: true, greater_than: 0, allow_nil: true
  validates_numericality_of :lat
  validates_numericality_of :lon
  validates_numericality_of :error_radius, greater_than: 0
  before_save :default_values
  
  def next_seq
    count = self.quest.steps.maximum(:seq)
    return (count || 0) + 1
  end
    
  private

  def default_values
    self.seq = next_seq unless self.seq
  end
end

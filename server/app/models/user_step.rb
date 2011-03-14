class UserStep < ActiveRecord::Base
  belongs_to :user_quest
  belongs_to :step

  json_include :step

  validate :is_valid_step
  validates_uniqueness_of :step_id, scope: 'user_quest_id', message: "already given for this quest"

  private

  def is_valid_step
    errors.add_to_base("invalid step") unless self.user_quest.quest.steps.exists?(self.step_id)
  end
  
  def complete
    self.complete = Time.now 
  end  
end
class UserStep < ActiveRecord::Base
  self.include_root_in_json = false
  belongs_to :user_quest
  belongs_to :step

  validate :is_valid_step
  validates_uniqueness_of :step_id, scope: 'user_quest_id', message: "already given for this quest"

  private

  def is_valid_step
    errors.add_to_base("invalid step") unless self.user_quest.quest.steps.exists?(self.step_id)
  end
  
end
class CreateUserSteps < ActiveRecord::Migration
  def self.up
    create_table :user_steps do |t|
      t.references :user_quest
      t.references :step
      t.datetime :datecompleted

      t.timestamps
    end
  end

  def self.down
    drop_table :user_steps
  end
end

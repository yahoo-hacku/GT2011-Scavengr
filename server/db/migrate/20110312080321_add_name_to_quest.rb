class AddNameToQuest < ActiveRecord::Migration
  def self.up
    add_column :quests, :name, :string
  end

  def self.down
    remove_column :quests, :name
  end
end

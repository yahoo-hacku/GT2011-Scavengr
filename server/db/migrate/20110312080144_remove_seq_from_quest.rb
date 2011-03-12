class RemoveSeqFromQuest < ActiveRecord::Migration
  def self.up
    remove_column :quests, :seq
  end

  def self.down
    add_column :quests, :seq, :integer
  end
end

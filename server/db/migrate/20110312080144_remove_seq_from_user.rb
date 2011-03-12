class RemoveSeqFromUser < ActiveRecord::Migration
  def self.up
    remove_column :users, :seq
  end

  def self.down
    add_column :users, :seq, :integer
  end
end

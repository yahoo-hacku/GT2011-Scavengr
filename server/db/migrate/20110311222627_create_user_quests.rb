class CreateUserQuests < ActiveRecord::Migration
  def self.up
    create_table :user_quests do |t|
      t.references :user
      t.references :quest
      t.datetime :started
      t.datetime :completed

      t.timestamps
    end
  end

  def self.down
    drop_table :user_quests
  end
end

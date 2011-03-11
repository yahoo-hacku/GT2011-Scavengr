class CreateQuests < ActiveRecord::Migration
  def self.up
    create_table :quests do |t|
      t.integer :seq
      t.references :created_by
      t.text :description
      t.integer :time_limit
      t.boolean :active

      t.timestamps
    end
  end

  def self.down
    drop_table :quests
  end
end

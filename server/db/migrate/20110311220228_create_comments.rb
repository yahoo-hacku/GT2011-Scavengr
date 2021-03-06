class CreateComments < ActiveRecord::Migration
  def self.up
    create_table :comments do |t|
      t.references :quest
      t.references :user
      t.text :text
      t.integer :rating

      t.timestamps
    end
  end

  def self.down
    drop_table :comments
  end
end

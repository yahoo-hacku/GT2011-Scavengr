class CreateSteps < ActiveRecord::Migration
  def self.up
    create_table :steps do |t|
      t.references :quest
      t.integer :seq
      t.decimal :lat
      t.decimal :long
      t.text :clue
      t.decimal :error_radius

      t.timestamps
    end
  end

  def self.down
    drop_table :steps
  end
end

class AddIndexToStepLatAndLon < ActiveRecord::Migration
  def self.up
    add_index :steps, [:lat, :lon]
  end

  def self.down
    remove_index :steps, [:lat, :lon]
  end
end

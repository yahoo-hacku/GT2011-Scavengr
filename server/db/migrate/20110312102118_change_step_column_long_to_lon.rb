class ChangeStepColumnLongToLon < ActiveRecord::Migration
  def self.up
    rename_column :steps, :long, :lon
  end

  def self.down
    rename_column :steps, :lon, :long
  end
end

class CreateAuthTokens < ActiveRecord::Migration
  def self.up
    create_table :auth_tokens do |t|
      t.string :token
      t.references :user

      t.timestamps
    end
  end

  def self.down
    drop_table :auth_tokens
  end
end

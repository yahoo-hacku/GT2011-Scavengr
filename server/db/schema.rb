# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20110312102118) do

  create_table "auth_tokens", :force => true do |t|
    t.string   "token"
    t.integer  "user_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "comments", :force => true do |t|
    t.integer  "quest_id"
    t.integer  "user_id"
    t.text     "text"
    t.integer  "rating"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "quests", :force => true do |t|
    t.integer  "seq"
    t.integer  "created_by_id"
    t.text     "description"
    t.integer  "time_limit"
    t.boolean  "active"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
  end

  create_table "steps", :force => true do |t|
    t.integer  "quest_id"
    t.integer  "seq"
    t.decimal  "lat"
    t.decimal  "lon"
    t.text     "clue"
    t.decimal  "error_radius"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "user_quests", :force => true do |t|
    t.integer  "user_id"
    t.integer  "quest_id"
    t.datetime "started"
    t.datetime "completed"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "user_steps", :force => true do |t|
    t.integer  "user_quest_id"
    t.integer  "step_id"
    t.datetime "datecompleted"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "users", :force => true do |t|
    t.string   "name"
    t.string   "password_hash"
    t.string   "email"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end

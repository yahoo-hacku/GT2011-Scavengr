require 'bcrypt'

class User < ActiveRecord::Base
  has_many :quests
  has_many :user_quests
  has_many :comments
  
  attr_accessor :password
  
  validates_uniqueness_of :name, :email
  validates_confirmation_of :password  
  validates_presence_of :password, :on => :create
  
  before_save :encrypt_password
  
  def pass
    @pass ||= BCrypt::Password.new(password_hash) if password_hash.present?
  end
  
  def encrypt_password  
    if password.present?
      self.password_hash = BCrypt::Password.create(password)  
      self.password = nil
    end  
  end
end

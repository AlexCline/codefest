class User < ActiveRecord::Base
  attr_accessible :home, :adv_card, :points, :hood, :serial

  validates :home, :presence => true
  validates :serial, :presence => true
end

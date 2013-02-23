class CreateAchieves < ActiveRecord::Migration
  def change
    create_table :achieves do |t|
      t.string :name
      t.integer :points

      t.timestamps
    end
  end
end

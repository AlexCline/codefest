class CreateHoods < ActiveRecord::Migration
  def change
    create_table :hoods do |t|
      t.string :name

      t.timestamps
    end
  end
end

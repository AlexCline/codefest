require 'test_helper'

class AchievesControllerTest < ActionController::TestCase
  setup do
    @achiefe = achieves(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:achieves)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create achiefe" do
    assert_difference('Achiefe.count') do
      post :create, achiefe: { name: @achiefe.name, points: @achiefe.points }
    end

    assert_redirected_to achiefe_path(assigns(:achiefe))
  end

  test "should show achiefe" do
    get :show, id: @achiefe
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @achiefe
    assert_response :success
  end

  test "should update achiefe" do
    put :update, id: @achiefe, achiefe: { name: @achiefe.name, points: @achiefe.points }
    assert_redirected_to achiefe_path(assigns(:achiefe))
  end

  test "should destroy achiefe" do
    assert_difference('Achiefe.count', -1) do
      delete :destroy, id: @achiefe
    end

    assert_redirected_to achieves_path
  end
end

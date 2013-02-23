class AchievesController < ApplicationController
  # GET /achieves
  # GET /achieves.json
  def index
    @achieves = Achiefe.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @achieves }
    end
  end

  # GET /achieves/1
  # GET /achieves/1.json
  def show
    @achiefe = Achiefe.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @achiefe }
    end
  end

  # GET /achieves/new
  # GET /achieves/new.json
  def new
    @achiefe = Achiefe.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @achiefe }
    end
  end

  # GET /achieves/1/edit
  def edit
    @achiefe = Achiefe.find(params[:id])
  end

  # POST /achieves
  # POST /achieves.json
  def create
    @achiefe = Achiefe.new(params[:achiefe])

    respond_to do |format|
      if @achiefe.save
        format.html { redirect_to @achiefe, notice: 'Achiefe was successfully created.' }
        format.json { render json: @achiefe, status: :created, location: @achiefe }
      else
        format.html { render action: "new" }
        format.json { render json: @achiefe.errors, status: :unprocessable_entity }
      end
    end
  end

  # PUT /achieves/1
  # PUT /achieves/1.json
  def update
    @achiefe = Achiefe.find(params[:id])

    respond_to do |format|
      if @achiefe.update_attributes(params[:achiefe])
        format.html { redirect_to @achiefe, notice: 'Achiefe was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @achiefe.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /achieves/1
  # DELETE /achieves/1.json
  def destroy
    @achiefe = Achiefe.find(params[:id])
    @achiefe.destroy

    respond_to do |format|
      format.html { redirect_to achieves_url }
      format.json { head :no_content }
    end
  end
end

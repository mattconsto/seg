/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package AuctionTool;

import java.io.File;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.controlsfx.control.CheckComboBox;

import dashboard.Preferences;
import dashboard.controller.BounceGraphConstructor;
import dashboard.controller.ClicksGraphConstructor;
import dashboard.controller.ConversionGraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.controller.UniqueClicksGraphConstructor;
import dashboard.controller.UniqueImpressionsGraphConstructor;

/**
 * Login Controller.
 */
public class AuctionController extends AnchorPane {
	private Main application;
	@FXML
	private MenuBar menu;
	@FXML
	private MenuItem importCampaign;
	@FXML
	private MenuItem openCampaign;
	@FXML
	private MenuItem close;
	@FXML
	private ToggleGroup viewGroup;
	@FXML
	private ToggleGroup viewGroup1;
	@FXML
	private MenuItem importCampaign1;
	@FXML
	private Menu prefMenu;
	@FXML
	private RadioMenuItem pref1;
	@FXML
	private RadioMenuItem pref2;
	@FXML
	private RadioMenuItem pref3;
	@FXML
	private RadioMenuItem pref4;
	@FXML
	private RadioMenuItem pref5;
	@FXML
	private CheckComboBox<String> filterGender;
	@FXML
	private CheckComboBox<String> filterAge;
	@FXML
	private CheckComboBox<String> filterIncome;
	@FXML
	private CheckComboBox<String> filterContext;
	@FXML
	private CheckComboBox<String> filterMetrics;
	@FXML
	private DatePicker filterDateFrom;
	@FXML
	private DatePicker filterDateTo;
	@FXML
	private Button generateGraph;
	@FXML
	private LineChart<String, Number> graphDisplay;

	public void setApp(Main application) {
		this.application = application;
	}

	@SuppressWarnings("unchecked")
	void initialize() {
		filterGender.getItems().addAll("Female", "Male");
		filterAge.getItems().addAll("Less than 25", "25 to 34", "35 to 44", "45 to 54", "Greater than 55");
		filterIncome.getItems().addAll("Low", "Medium", "High");
		filterContext.getItems().addAll("News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel");
		filterMetrics.getItems().addAll("Hours", "Days", "Weeks", "Months");
		
		try {
			application.getStage().setTitle(Preferences.productName + " - Impressions");
			graphDisplay.getData().addAll(new BounceGraphConstructor().fetchGraph());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void importCampaignAction(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select folder to import campaign");

		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if (!userDirectory.canRead()) {
			userDirectory = new File("c:/");
		}
		dirChooser.setInitialDirectory(userDirectory);

		File f = dirChooser.showDialog(application.getStage());
		if (f != null) {
			CSVReader importCsv = new CSVReader();
			if (importCsv.checkFilesExist(f.getAbsolutePath())) {
				FileChooser fChooser = new FileChooser();
				fChooser.setTitle("jk");
				File s = fChooser.showSaveDialog(application.getStage());
				if (s != null) {
					if (importCsv.readCsvs(f.getAbsolutePath(), s.getAbsolutePath())) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Campaign imported successfully");
						alert.setHeaderText(null);
						alert.setContentText("The files were imported successfully");
						alert.showAndWait();
					}
				}
			}
		}
	}
	
    @FXML
    private void openCampaignAction(ActionEvent event) {}

    @FXML
    private void closeAction(ActionEvent event) {
    	System.exit(0);
    }

    @SuppressWarnings("unchecked")
	@FXML
    private void preference_impressions(ActionEvent event) {
		try {
			application.getStage().setTitle(Preferences.productName + " - Impressions");
			graphDisplay.getData().clear();
			graphDisplay.getData().addAll(new ImpressionsGraphConstructor().fetchGraph());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    @SuppressWarnings("unchecked")
	@FXML
    private void preference_impressions_unique(ActionEvent event) {
		try {
			application.getStage().setTitle(Preferences.productName + " - Impressions (Unique)");
			graphDisplay.getData().clear();
			graphDisplay.getData().addAll(new UniqueImpressionsGraphConstructor().fetchGraph());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    @SuppressWarnings("unchecked")
	@FXML
    private void preference_clicks(ActionEvent event) {
		try {
			application.getStage().setTitle(Preferences.productName + " - Clicks");
			graphDisplay.getData().clear();
			graphDisplay.getData().addAll(new ClicksGraphConstructor().fetchGraph());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    @SuppressWarnings("unchecked")
	@FXML
    private void preference_clicks_unique(ActionEvent event) {
		try {
			application.getStage().setTitle(Preferences.productName + " - Clicks (Unique)");
			graphDisplay.getData().clear();
			graphDisplay.getData().addAll(new UniqueClicksGraphConstructor().fetchGraph());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    @SuppressWarnings("unchecked")
	@FXML
    private void preference_conversions(ActionEvent event) {
		try {
			application.getStage().setTitle(Preferences.productName + " - Conversions");
			graphDisplay.getData().clear();
			graphDisplay.getData().addAll(new ConversionGraphConstructor().fetchGraph());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    @FXML
    private void prefOnAction(ActionEvent event) {}
}

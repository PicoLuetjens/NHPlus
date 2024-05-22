package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The <code>NewTreatmentController</code> contains the entire logic of the NewTreatmentView. It determines which data is displayed and how to react to events.
 */
public class NewTreatmentController {

	@FXML
	private Label labelFirstName;

	@FXML
	private Label labelSurname;

	@FXML
	private TextField textFieldBegin;

	@FXML
	private TextField textFieldEnd;

	@FXML
	private TextField textFieldDescription;

	@FXML
	private TextArea textAreaRemarks;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Button buttonAdd;

	@FXML
	private ComboBox<String> comboBoxNurseSelection;
	private final ObservableList<String> nurseSelection = FXCollections.observableArrayList();
	private ArrayList<Nurse> nurseList;
	private AllTreatmentController controller;
	private Patient patient;
	private Nurse nurse;
	private Stage stage;

	/**
	 * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
	 * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
	 * configured.
	 */
	public void initialize(AllTreatmentController controller, Stage stage, Patient patient) {
		this.controller = controller;
		this.patient = patient;
		comboBoxNurseSelection.setItems(nurseSelection);
		this.createComboBoxData();
		comboBoxNurseSelection.getSelectionModel().select(0);
		this.stage = stage;
		this.buttonAdd.setDisable(true);
		ChangeListener<String> inputNewPatientListener = (observableValue, oldText, newText) ->
			NewTreatmentController.this.buttonAdd.setDisable(NewTreatmentController.this.areInputDataInvalid());
		this.textFieldBegin.textProperty().addListener(inputNewPatientListener);
		this.textFieldEnd.textProperty().addListener(inputNewPatientListener);
		this.textFieldDescription.textProperty().addListener(inputNewPatientListener);
		this.textAreaRemarks.textProperty().addListener(inputNewPatientListener);
		this.datePicker.valueProperty().addListener((observableValue, localDate, t1) -> NewTreatmentController.this.buttonAdd.setDisable(NewTreatmentController.this.areInputDataInvalid()));
		this.datePicker.setConverter(new StringConverter<>() {
			@Override
			public String toString(LocalDate localDate) {
				return (localDate == null) ? "" : DateConverter.convertLocalDateToString(localDate);
			}
			@Override
			public LocalDate fromString(String localDate) {
				return DateConverter.convertStringToLocalDate(localDate);
			}
		});
		this.showPatientData();
	}

	/**
	 * Reads in the data of the selected patient.
	 */
	private void showPatientData() {
		this.labelFirstName.setText(patient.getFirstName());
		this.labelSurname.setText(patient.getSurname());
	}

	/**
	 * Handles the gui interaction for the creation of a new treatment
	 */
	@FXML
	public void handleAdd() {
		LocalDate date = this.datePicker.getValue();
		LocalTime begin = DateConverter.convertStringToLocalTime(textFieldBegin.getText());
		LocalTime end = DateConverter.convertStringToLocalTime(textFieldEnd.getText());
		String description = textFieldDescription.getText();
		String remarks = textAreaRemarks.getText();
		String selectedNurse = this.comboBoxNurseSelection.getSelectionModel().getSelectedItem();
		Nurse nurse = searchInList(selectedNurse);
		Treatment treatment = new Treatment(patient.getPid(), nurse.getNid(), date, begin, end, description, remarks, nurse.getSurname(), nurse.getFirstName(), nurse.getPhoneNumber(), "false");
		createTreatment(treatment);
		controller.readAllAndShowInTableView();
		stage.close();
	}

	/**
	 * Creates the actual treatment via the Dao object.
	 * @param treatment The treatment that should be created.
	 */
	private void createTreatment(Treatment treatment) {
		TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
		try {
			dao.create(treatment);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Handles the gui interaction when canceling the window.
	 */
	@FXML
	public void handleCancel() {
		stage.close();
	}

	/**
	 * Validates that all Inputfield data is valid.
	 * @return <code>boolean</code> to determine if the input data is valid.
	 */
	private boolean areInputDataInvalid() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (this.textFieldBegin.getText() == null || this.textFieldEnd.getText() == null) {
			return true;
		}
		try {
			LocalTime begin = DateConverter.convertStringToLocalTime(this.textFieldBegin.getText());
			LocalTime end = DateConverter.convertStringToLocalTime(this.textFieldEnd.getText());
			if (!end.isAfter(begin)) {
				return true;
			}
		} catch (Exception exception) {
			return true;
		}
		try {
			format.parse(DateConverter.convertLocalDateToString(this.datePicker.getValue()));
		} catch (ParseException e) {
			return true;
		}
		if (!textFieldBegin.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$") || !textFieldEnd.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
			return true;
		}
		return this.textFieldDescription.getText().isBlank() || this.datePicker.getValue() == null;
	}

	/**
	 * Create the combobox data for selectable nurses.
	 */
	private void createComboBoxData() {
		NurseDao dao = DaoFactory.getDaoFactory().createNurseDAO();
		try {
			nurseList = (ArrayList<Nurse>) dao.readAll();
			this.nurseSelection.add("alle");
			for (Nurse nurse : nurseList) {
				this.nurseSelection.add(nurse.getSurname());
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Searches for a given name in a list.
	 * @param surname The given name.
	 * @return <code>Nurse</code> The nurse that was searched for.
	 */
	private Nurse searchInList(String surname) {
		for (Nurse nurse : this.nurseList) {
			if (nurse.getSurname().equals(surname)) {
				return nurse;
			}
		}
		return null;
	}

	/**
	 * Handles the combobox events
	 * @param event The event that triggered this action.
	 */
	public void handleComboBox(ActionEvent event) {
	}
}
package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The <code>AllCaregiverController</code> contains the entire logic of the nurse view. It determines which data is displayed and how to react to events.
 */
public class AllCaregiverController {

	@FXML
	private TableView<Nurse> tableView;

	@FXML
	private TableColumn<Nurse, Integer> colID;

	@FXML
	private TableColumn<Nurse, String> colFirstName;

	@FXML
	private TableColumn<Nurse, String> colSurname;

	@FXML
	private TableColumn<Nurse, String> colTelephone;

	@FXML
	private TableColumn<Nurse, String> islocked;

	@FXML
	private Button buttonDelete;

	@FXML
	private Button buttonAdd;

	@FXML
	private TextField textFieldSurname;

	@FXML
	private TextField textFieldFirstName;

	@FXML
	private TextField textFieldTelephone;

	private final ObservableList<Nurse> nurses = FXCollections.observableArrayList();
	private NurseDao dao;

	/**
	 * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
	 * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
	 * configured.
	 */
	public void initialize() {
		this.readAllAndShowInTableView();
		this.autoDeleteByAge();
		this.colID.setCellValueFactory(new PropertyValueFactory<>("nid"));

		// CellValueFactory to show property values in TableView
		this.colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		// CellFactory to write property values from with in the TableView
		this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

		this.colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
		this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

		this.colTelephone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		this.colTelephone.setCellFactory(TextFieldTableCell.forTableColumn());

		this.islocked.setCellValueFactory(new PropertyValueFactory<>("locked"));
		this.islocked.setCellFactory(TextFieldTableCell.forTableColumn());
		if(!MainWindowController.IS_ADMIN) {
			this.islocked.setVisible(false);
		}

		//Anzeigen der Daten
		this.tableView.setItems(this.nurses);

		this.buttonDelete.setDisable(true);
		this.tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Nurse>() {
			@Override
			public void changed(ObservableValue<? extends Nurse> observableValue, Nurse oldNurse, Nurse newNurse) {
				AllCaregiverController.this.buttonDelete.setDisable(newNurse == null);
			}
		});

		this.buttonAdd.setDisable(true);
		ChangeListener<String> inputNewNurseListener = (observableValue, oldText, newText) ->
			AllCaregiverController.this.buttonAdd.setDisable(!AllCaregiverController.this.areInputDataValid());
		this.textFieldSurname.textProperty().addListener(inputNewNurseListener);
		this.textFieldFirstName.textProperty().addListener(inputNewNurseListener);
		this.textFieldTelephone.textProperty().addListener(inputNewNurseListener);
	}

	public void autoDeleteByAge(){
		try {
			LocalDate tenYearsAgo = LocalDate.now().minusYears(10);
			TreatmentDao tdao = DaoFactory.getDaoFactory().createTreatmentDao();
			NurseDao ndao = DaoFactory.getDaoFactory().createNurseDAO();
			List<Treatment> treatments = tdao.readAll();
			List<Nurse> nurses = ndao.readAll();

			// Lists to store treatments and nurses to be deleted
			List<Treatment> treatmentsToDelete = new ArrayList<>();
			List<Nurse> nursesToDelete = new ArrayList<>();

			for (Nurse nurse : nurses) {
				boolean hasYoungerAssignments = false;
				for (Treatment treatment : treatments) {
					if (treatment.getNid() == nurse.getNid()) {
						if(DateConverter.convertStringToLocalDate(treatment.getDate()).isBefore(tenYearsAgo)){
							treatmentsToDelete.add(treatment);
						}
						else{
							hasYoungerAssignments = true;
						}
					}
				}
				if (!hasYoungerAssignments) {
					nursesToDelete.add(nurse);
				}
			}

			// Remove the collected treatments and nurses
			for (Treatment treatment : treatmentsToDelete) {
				treatments.remove(treatment);
				tdao.deleteById(treatment.getTid());
			}

			for (Nurse nurse : nursesToDelete) {
				nurses.remove(nurse);
				ndao.deleteById(nurse.getNid());
			}

		} catch (SQLException exception) {
			exception.printStackTrace();
		}

	}

	public void removeByLockedStatus() {
		Iterator<Nurse> iterator = this.nurses.iterator();
		while (iterator.hasNext()) {
			Nurse nurse = iterator.next();
			if ("true".equals(nurse.isLocked())) {
				iterator.remove();
			}
		}
	}

	/**
	 * When a cell of the column with first names was changed, this method will be called, to persist the change.
	 *
	 * @param event Event including the changed object and the change.
	 */
	@FXML
	public void handleOnEditFirstname(TableColumn.CellEditEvent<Nurse, String> event){
		event.getRowValue().setFirstName(event.getNewValue());
		TreatmentDao tdao = DaoFactory.getDaoFactory().createTreatmentDao();
        List<Treatment> treatments = null;
        try {
            treatments = tdao.readTreatmentsByNid(event.getRowValue().getNid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        treatments.forEach(treatment -> treatment.setNurseFirstname(event.getNewValue()));
		this.doUpdate(event);
	}

	/**
	 * When a cell of the column with surnames was changed, this method will be called, to persist the change.
	 *
	 * @param event Event including the changed object and the change.
	 */
	@FXML
	public void handleOnEditSurname(TableColumn.CellEditEvent<Nurse, String> event){
		event.getRowValue().setSurname(event.getNewValue());
		TreatmentDao tdao = DaoFactory.getDaoFactory().createTreatmentDao();
        List<Treatment> treatments = null;
        try {
            treatments = tdao.readTreatmentsByNid(event.getRowValue().getNid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        treatments.forEach(treatment -> treatment.setNurseSurname(event.getNewValue()));
		this.doUpdate(event);
	}

	/**
	 * When a cell of the column with care levels was changed, this method will be called, to persist the change.
	 *
	 * @param event Event including the changed object and the change.
	 */
	@FXML
	public void handleOnEditTelephone(TableColumn.CellEditEvent<Nurse, String> event){
		String oldValue = event.getOldValue();
		if (this.isPhoneNumberValid(event.getNewValue())) {
			event.getRowValue().setPhoneNumber(event.getNewValue());
			TreatmentDao tdao = DaoFactory.getDaoFactory().createTreatmentDao();
            List<Treatment> treatments = null;
            try {
                treatments = tdao.readTreatmentsByNid(event.getRowValue().getNid());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            treatments.forEach(treatment -> {treatment.setNursePhonenumber(event.getNewValue());
                try {
                    tdao.update(treatment);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

		} else {
			event.getRowValue().setPhoneNumber(oldValue);
			this.tableView.refresh();
		}
		this.doUpdate(event);
	}

	/**
	 * When a cell of the column with room numbers was changed, this method will be called, to persist the change.
	 *
	 * @param event Event including the changed object and the change.
	 */
	@FXML
	public void handleOnEditLocked(TableColumn.CellEditEvent<Nurse, String> event) {
		String oldValue = event.getOldValue();
		if ("true".equals(event.getNewValue()) || "false".equals(event.getNewValue())) {
			event.getRowValue().setLocked(event.getNewValue());
		} else {
			event.getRowValue().setLocked(oldValue);
		}
		this.tableView.refresh();
		this.doUpdate(event);
	}

	/**
	 * Updates a nurse by calling the method <code>update()</code> of {@link NurseDao}.
	 *
	 * @param event Event including the changed object and the change.
	 */
	private void doUpdate(TableColumn.CellEditEvent<Nurse, String> event) {
		try {
			this.dao.update(event.getRowValue());
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Reloads all nurses to the table by clearing the list of all nurses and filling it again by all persisted
	 * nurses, delivered by {@link NurseDao}.
	 */
	private void readAllAndShowInTableView() {
		this.nurses.clear();
		this.dao = DaoFactory.getDaoFactory().createNurseDAO();
		try {
			this.nurses.addAll(this.dao.readAll());
			if(!MainWindowController.IS_ADMIN) {
				this.removeByLockedStatus();
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * This method handles events fired by the button to delete nurses. It calls {@link NurseDao} to delete the
	 * nurse from the database and removes the object from the list, which is the data source of the
	 * <code>TableView</code>.
	 */
	@FXML
	public void handleDelete() {
		Nurse selectedItem = this.tableView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			try {
				DaoFactory.getDaoFactory().createNurseDAO().deleteById(selectedItem.getNid());
				this.tableView.getItems().remove(selectedItem);
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
	}

	/**
	 * This method handles the events fired by the button to add a nurse. It collects the data from the
	 * <code>TextField</code>s, creates an object of class <code>Nurse</code> of it and passes the object to
	 * {@link NurseDao} to persist the data.
	 */
	@FXML
	public void handleAdd() {
		String surname = this.textFieldSurname.getText();
		String firstName = this.textFieldFirstName.getText();
		String phonenumber = this.textFieldTelephone.getText();
		try {
			this.dao.create(new Nurse(firstName, surname, phonenumber, "false"));
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		readAllAndShowInTableView();
		clearTextfields();
	}

	/**
	 * Clears all contents from all <code>TextField</code>s.
	 */
	private void clearTextfields() {
		this.textFieldFirstName.clear();
		this.textFieldSurname.clear();
		this.textFieldTelephone.clear();
	}

	private boolean isPhoneNumberValid(String value) {
		String regex = "^[0-9+ \\-]+$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value).matches();
	}

	private boolean areInputDataValid() {

		return !this.textFieldFirstName.getText().isBlank() && !this.textFieldSurname.getText().isBlank() &&
			!this.textFieldTelephone.getText().isBlank() && this.isPhoneNumberValid(this.textFieldTelephone.getText());
	}
}

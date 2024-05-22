package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>AllTreatmentController</code> contains the entire logic of the treatment view. It determines which data is displayed and how to react to events.
 */
public class AllTreatmentController {

    @FXML
    private TableView<Treatment> tableView;

    @FXML
    private TableColumn<Treatment, Integer> columnId;

    @FXML
    private TableColumn<Treatment, Integer> columnPid;

    @FXML
    private TableColumn<Treatment, String> columnNid;

    @FXML
    private TableColumn<Treatment, String> columnDate;

    @FXML
    private TableColumn<Treatment, String> columnBegin;

    @FXML
    private TableColumn<Treatment, String> columnEnd;

    @FXML
    private TableColumn<Treatment, String> columnDescription;

    @FXML
    private TableColumn<Treatment, String> nurseName;

    @FXML
    private TableColumn<Treatment, String> nursePhone;

    @FXML
    private ComboBox<String> comboBoxPatientSelection;

    @FXML
    private Button buttonDelete;

    @FXML
    private TableColumn<Treatment, String> isLocked;

    private final ObservableList<Treatment> treatments = FXCollections.observableArrayList();
    private TreatmentDao dao;
    private final ObservableList<String> patientSelection = FXCollections.observableArrayList();
    private ArrayList<Patient> patientList;

    /**
     * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
     * configured.
     */
    public void initialize() {
        readAllAndShowInTableView();
        comboBoxPatientSelection.setItems(patientSelection);
        this.autoDeleteByAge();
        this.createComboBoxData();
        comboBoxPatientSelection.getSelectionModel().select(0);
        this.columnId.setCellValueFactory(new PropertyValueFactory<>("tid"));
        this.columnPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        this.columnNid.setCellValueFactory(new PropertyValueFactory<>("nid"));
        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        this.columnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        this.columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.nurseName.setCellValueFactory(patientSelection ->  Bindings.createStringBinding(() -> patientSelection.getValue().getNurseSurname() + ", " + patientSelection.getValue().getNurseFirstname()));
        this.nursePhone.setCellValueFactory(new PropertyValueFactory<>("nursePhonenumber"));
        this.isLocked.setCellValueFactory(new PropertyValueFactory<>("isLocked"));
        if(!MainWindowController.IS_ADMIN){
            this.isLocked.setVisible(false);
        }
        this.tableView.setItems(this.treatments);
        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldTreatment, newTreatment) ->
                        AllTreatmentController.this.buttonDelete.setDisable(newTreatment == null));
    }

    /**
     * When <code>autoDeleteByAge()</code> gets called, it ensures that Treatments that are
     * older than ten years will be deleted form the database.
     */
    public void autoDeleteByAge(){
        LocalDate tenYearsAgo = LocalDate.now().minusYears(10);
        Iterator<Treatment> iterator = this.treatments.iterator();
        while (iterator.hasNext()) {
            Treatment treatment = iterator.next();
            if (DateConverter.convertStringToLocalDate(treatment.getDate()).isBefore(tenYearsAgo)){
                iterator.remove();
                try {
                    DaoFactory.getDaoFactory().createTreatmentDao().deleteById(treatment.getTid());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * When <code>removeByLockedStatus()</code> gets called, it ensures that it removes locked Treatments from the view that is shown in the program.
     * However, this does not affect the database itself.
     */
    public void removeByLockedStatus() {
        Iterator<Treatment> iterator = this.treatments.iterator();
        while (iterator.hasNext()) {
            Treatment treatment = iterator.next();
            if ("true".equals(treatment.getIsLocked())) {
                iterator.remove();
            }
        }
    }

    /**
     * When <code>updateFieldsByNurseLockedStatus()</code> is called, it ensures that it updates the data of the locked nurse in the
     * treatment to not be visible anymore.
     * However, this does not effect the database itself.
     */
    public void updateFieldsByNurseLockedStatus(){
        try {
            NurseDao ndao = DaoFactory.getDaoFactory().createNurseDAO();
            List<Nurse> nurses = ndao.readAll();
            for(Treatment treatment : this.treatments){
                for(Nurse nurse : nurses){
                    if(treatment.getNid() == nurse.getNid() && "true".equals(nurse.isLocked())){
                        treatment.setNurseFirstname("gesperrt");
                        treatment.setNurseSurname("gesperrt");
                        treatment.setNursePhonenumber("gesperrt");
                    }
                }
            }
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all treatments to the table by clearing the list of all treatments and filling it again by all persisted
     * treatments, delivered by {@link TreatmentDao}.
     */
    public void readAllAndShowInTableView() {
        comboBoxPatientSelection.getSelectionModel().select(0);
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            this.treatments.setAll(dao.readAll());
            if(!MainWindowController.IS_ADMIN) {
                this.removeByLockedStatus();
            }
            this.updateFieldsByNurseLockedStatus();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Creates the combobox selections and sets the default selection to "alle".
     */
    private void createComboBoxData() {
        PatientDao dao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            patientList = (ArrayList<Patient>) dao.readAll();
            this.patientSelection.add("alle");
            for (Patient patient: patientList) {
                this.patientSelection.add(patient.getSurname());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the combobox selection.
     */
    @FXML
    public void handleComboBox() {
        String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
        this.treatments.clear();
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();

        if (selectedPatient.equals("alle")) {
            try {
                this.treatments.setAll(this.dao.readAll());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        Patient patient = searchInList(selectedPatient);
        if (patient !=null) {
            try {
                this.treatments.setAll(this.dao.readTreatmentsByPid(patient.getPid()));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Searches for a specific patient in the list.
     * @param surname The name to search for.
     * @return <code>Patient</code> the patient that was searched for.
     */
    private Patient searchInList(String surname) {
        for (Patient patient : this.patientList) {
            if (patient.getSurname().equals(surname)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Handles the deletion of treatments.
     */
    @FXML
    public void handleDelete() {
        int index = this.tableView.getSelectionModel().getSelectedIndex();
        Treatment t = this.treatments.remove(index);
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.deleteById(t.getTid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the creation of new treatments.
     */
    @FXML
    public void handleNewTreatment() {
        try{
            String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
            Patient patient = searchInList(selectedPatient);
            newTreatmentWindow(patient);
        } catch (NullPointerException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten aus!");
            alert.showAndWait();
        }
    }

    /**
     * Handles the selection of treatments.
     */
    @FXML
    public void handleMouseClick() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedItem() != null)) {
                int index = this.tableView.getSelectionModel().getSelectedIndex();
                Treatment treatment = this.treatments.get(index);
                treatmentWindow(treatment);
            }
        });
    }

    /**
     * Handles the window dialog for the creation of a new treatment.
     * @param patient The patient selected to create a new treatment for.
     */
    public void newTreatmentWindow(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/NewTreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            NewTreatmentController controller = loader.getController();
            controller.initialize(this, stage, patient);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the window dialog for the editing of an existing treatment.
     * @param treatment The treatment that should be edited.
     */
    public void treatmentWindow(Treatment treatment){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/TreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            TreatmentController controller = loader.getController();
            controller.initializeController(this, stage, treatment);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
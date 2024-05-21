package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;

public class TreatmentController {

    @FXML
    private Label labelPatientName;

    @FXML
    private Label labelCareLevel;

    @FXML
    private TextField textFieldBegin;

    @FXML
    private TextField textFieldEnd;

    @FXML
    private TextField textFieldDescription;

    @FXML
    private TextArea textAreaRemarks;

    @FXML
    private Label nurses;

    @FXML
    private Label nursePhonenumber;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField textfieldLocked;

    private AllTreatmentController controller;
    private Stage stage;
    private Patient patient;
    private Treatment treatment;
    private Nurse nurse;

    public void initializeController(AllTreatmentController controller, Stage stage, Treatment treatment) {
        this.stage = stage;
        this.controller= controller;
        PatientDao pDao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            this.patient = pDao.read((int) treatment.getPid());
            this.treatment = treatment;
            showData();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void showData(){
        this.labelPatientName.setText(patient.getSurname()+", "+patient.getFirstName());
        this.labelCareLevel.setText(patient.getCareLevel());
        this.nurses.setText(treatment.getNurseSurname() + ", " + treatment.getNurseFirstname());
        this.nursePhonenumber.setText(treatment.getNursePhonenumber());
        LocalDate date = DateConverter.convertStringToLocalDate(treatment.getDate());
        this.datePicker.setValue(date);
        this.textFieldBegin.setText(this.treatment.getBegin());
        this.textFieldEnd.setText(this.treatment.getEnd());
        this.textFieldDescription.setText(this.treatment.getDescription());
        this.textAreaRemarks.setText(this.treatment.getRemarks());
        this.textfieldLocked.setText(this.treatment.getIsLocked());
        if(!MainWindowController.IS_ADMIN) {
            this.textfieldLocked.setEditable(false);
        }
    }

    private void applyErrorStyle(Control control) {
        if (!control.getStylesheets().contains(getClass().getResource("/de/hitec/nhplus/Error.css").toExternalForm())) {
            control.getStylesheets().add(getClass().getResource("/de/hitec/nhplus/Error.css").toExternalForm());
        }
        if (!control.getStyleClass().contains("error")) {
            control.getStyleClass().add("error");
        }
    }

    private void removeErrorStyle(Control control) {
        if (!control.getStylesheets().contains(getClass().getResource("/de/hitec/nhplus/Error.css").toExternalForm())) {
            control.getStylesheets().remove(getClass().getResource("/de/hitec/nhplus/Error.css").toExternalForm());
        }
        if (!control.getStyleClass().contains("error")) {
            control.getStyleClass().remove("error");
        }
    }

    private boolean validateTimeFormat(String time) {
        try {
            String[] splits = time.split(":");
            if (splits.length != 2) {
                return false;
            }

            if (splits[0].length() != 2 || splits[1].length() != 2) {
                return false;
            }

            int hours = Integer.parseInt(splits[0]);
            int minutes = Integer.parseInt(splits[1]);

            if (hours < 0 || hours > 23) {
                return false;
            }
            if (minutes < 0 || minutes > 59) {
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    public void handleChange(){

        this.treatment.setDescription(textFieldDescription.getText());
        this.treatment.setRemarks(textAreaRemarks.getText());

        String input = this.textfieldLocked.getText();
        if("true".equals(input) || "false".equals(input)){
            this.treatment.setIsLocked(textfieldLocked.getText());
            this.removeErrorStyle(this.textfieldLocked);
        }
        else{
            this.applyErrorStyle(this.textfieldLocked);
            return;
        }

        // EDIT BY PICO: YES THEY ARE HIGHLIGHTED ON PURPOSE BOTH AT THE SAME TIME
        if(this.validateTimeFormat(this.textFieldBegin.getText()) && this.validateTimeFormat(this.textFieldEnd.getText())){
            this.treatment.setBegin(textFieldBegin.getText());
            this.treatment.setEnd(textFieldEnd.getText());
            this.removeErrorStyle(this.textFieldBegin);
            this.removeErrorStyle(this.textFieldEnd);
        }
        else{
            this.applyErrorStyle(this.textFieldBegin);
            this.applyErrorStyle(this.textFieldEnd);
            return;
        }

        doUpdate();
        controller.readAllAndShowInTableView();
        stage.close();
    }

    private void doUpdate(){
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.update(treatment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(){
        stage.close();
    }
}
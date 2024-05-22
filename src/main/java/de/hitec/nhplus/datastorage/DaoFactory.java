package de.hitec.nhplus.datastorage;

/**
 * The <code>DaoFactory</code> contains the logic create different types of Daos.
 */
public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    /**
     * Gets the DaoFactory instance.
     */
    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }
        return DaoFactory.instance;
    }

    /**
     * Creates a treatment dao.
     */
    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates a patient dao.
     */
    public PatientDao createPatientDAO() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates a nurse dao.
     */
    public NurseDao createNurseDAO() { return new NurseDao(ConnectionBuilder.getConnection());}
}

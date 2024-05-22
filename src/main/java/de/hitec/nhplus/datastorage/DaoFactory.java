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
     * @return <code>DaoFactory</code> singleton instance.
     */
    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }
        return DaoFactory.instance;
    }

    /**
     * Creates a treatment dao.
     * @return <code>TreatmentDao</code> to transfer data.
     */
    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates a patient dao.
     * @return <code>PatientDao</code> to transfer data.
     */
    public PatientDao createPatientDAO() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates a nurse dao.
     * @return <code>NurseDao</code> to transfer data.
     */
    public NurseDao createNurseDAO() { return new NurseDao(ConnectionBuilder.getConnection());}
}

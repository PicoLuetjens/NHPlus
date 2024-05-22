package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
	private long tid;
	private final long pid;
	private final long nid;
	private LocalDate date;
	private LocalTime begin;
	private LocalTime end;
	private String description;
	private String remarks;
	private String nurseSurname;
	private String nurseFirstname;
	private String nursePhonenumber;
	private String locked;

	/**
	 * Constructor to initiate an object of class <code>Treatment</code> with the given parameter. Use this constructor
	 * to initiate objects, which are not persisted yet, because it will not have a treatment id (tid).
	 *
	 * @param pid         Id of the treated patient.
	 * @param date        Date of the Treatment.
	 * @param begin       Time of the start of the treatment in format "hh:MM"
	 * @param end         Time of the end of the treatment in format "hh:MM".
	 * @param description Description of the treatment.
	 * @param remarks     Remarks to the treatment.
	 */
	public Treatment(long pid, long nid, LocalDate date, LocalTime begin,
		LocalTime end, String description, String remarks, String nurseSurname, String nurseFirstname, String nursePhonenumber, String locked) {
		this.pid = pid;
		this.nid = nid;
		this.date = date;
		this.begin = begin;
		this.end = end;
		this.description = description;
		this.remarks = remarks;
		this.nurseSurname = nurseSurname;
		this.nurseFirstname = nurseFirstname;
		this.nursePhonenumber = nursePhonenumber;
		this.locked = locked;
	}

	/**
	 * Constructor to initiate an object of class <code>Treatment</code> with the given parameter. Use this constructor
	 * to initiate objects, which are already persisted and have a treatment id (tid).
	 *
	 * @param tid         Id of the treatment.
	 * @param pid         Id of the treated patient.
	 * @param date        Date of the Treatment.
	 * @param begin       Time of the start of the treatment in format "hh:MM"
	 * @param end         Time of the end of the treatment in format "hh:MM".
	 * @param description Description of the treatment.
	 * @param remarks     Remarks to the treatment.
	 */
	public Treatment(long tid, long pid, long nid, LocalDate date, LocalTime begin,
		LocalTime end, String description, String remarks, String nurseSurname, String nurseFirstname, String nursePhonenumber, String locked) {
		this.tid = tid;
		this.pid = pid;
		this.nid = nid;
		this.date = date;
		this.begin = begin;
		this.end = end;
		this.description = description;
		this.remarks = remarks;
		this.nurseSurname = nurseSurname;
		this.nurseFirstname = nurseFirstname;
		this.nursePhonenumber = nursePhonenumber;
		this.locked = locked;
	}

	public long getTid() {
		return tid;
	}

	public long getPid() {
		return this.pid;
	}

	public long getNid() {
		return this.nid;
	}

	public String getDate() {
		return date.toString();
	}

	public String getBegin() {
		return begin.toString();
	}

	public String getEnd() {
		return end.toString();
	}

	public void setDate(String date) {
		this.date = DateConverter.convertStringToLocalDate(date);
	}

	public void setBegin(String begin) {
		this.begin = DateConverter.convertStringToLocalTime(begin);
		;
	}

	public void setEnd(String end) {
		this.end = DateConverter.convertStringToLocalTime(end);
		;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNurseSurname() {
		return nurseSurname;
	}

	public void setNurseSurname(String nurseSurname) {
		this.nurseSurname = nurseSurname;
	}

	public String getNurseFirstname() {
		return nurseFirstname;
	}

	public void setNurseFirstname(String nurseFirstname) {
		this.nurseFirstname = nurseFirstname;
	}

	public String getNursePhonenumber() {
		return nursePhonenumber;
	}

	public String getIsLocked() {
		return locked;
	}

	public void setIsLocked(String locked) {
		this.locked = locked;
	}

	public void setNursePhonenumber(String nursePhonenumber) {
		this.nursePhonenumber = nursePhonenumber;
	}

	public String toString() {
		return "\nBehandlung" + "\nTID: " + this.tid +
			"\nPID: " + this.pid +
			"\nDate: " + this.date +
			"\nBegin: " + this.begin +
			"\nEnd: " + this.end +
			"\nDescription: " + this.description +
			"\nNurseFirstname: " + this.nurseFirstname +
			"\nNurseSurname: " + this.nurseSurname +
			"\nPhonenumber: " + this.nursePhonenumber +
			"\nRemarks: " + this.remarks + "\n";
	}
}

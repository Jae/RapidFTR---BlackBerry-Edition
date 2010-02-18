package com.rapidftr.services.impl;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.services.RecordService;
import com.rapidftr.services.ServiceException;
import com.rapidftr.utilities.HttpServer;
import com.rapidftr.utilities.LocalStore;
import com.rapidftr.utilities.impl.LocalStoreImpl;

public class RecordServiceImpl implements RecordService {
	private static RecordService instance;

	private LocalStore localStore;

	public static synchronized RecordService getInstance() {
		if (instance == null) {
			instance = new RecordServiceImpl();
		}

		return instance;
	}

	private RecordServiceImpl() {
		localStore = LocalStoreImpl.getInstance();
	}

	public ChildRecordItem[] getMatches(String searchCriteria)
			throws ServiceException {
		try {
			String id = "31bf2c074aa06488a3fb7b243328ade2";
			String responseFromServer = (new HttpServer())
					.getFromServer("children/" + id);

			System.out.println("From Server: " + responseFromServer);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}

		return localStore.retrieveMatching(searchCriteria);
	}

	public ChildRecord getRecord(String recordId) throws ServiceException {
		ChildRecord records[] = (ChildRecord[]) localStore.retrieveAll();

		ChildRecord match = null;

		for (int i = 0; i < records.length; i++) {
			if (records[i].getRecordId().equals(recordId)) {
				match = records[i];
				break;
			}
		}

		return match;
	}

	public String getRecordId() throws ServiceException {
		Random random = new Random();

		int id = random.nextInt();
		id = (id < 0) ? -id : id;

		return String.valueOf(id);
	}

	public void save(ChildRecord record) throws ServiceException {
		localStore.persist(record);

		try {
			if (!persistToServer(record, record.getPhoto())) {
				throw new ServiceException("Failed to save child record");
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private boolean persistToServer(ChildRecord record, byte[] photoData)
			throws Exception {
		Hashtable params = new Hashtable();
		params.put("commit", "Create");
		params.put("child[name]", record.getName());
		params.put("child[age]", String.valueOf(record.getIdentification()
				.getAge()));

		String isExact = (record.getIdentification().isExactAge()) ? "exact"
				: "approximate";

		params.put("child[isAgeExact]", isExact);
		String gender = (record.getIdentification().isMale()) ? "male"
				: "female";

		params.put("child[gender]", gender);
		params.put("child[origin]", record.getIdentification().getOrigin());

		params.put("child[lastKnownLocation]", record.getIdentification()
				.getLastKnownLocation());

		params.put("child[date_of_separation]", record.getIdentification()
				.getFormattedSeparationDate());

		return (new HttpServer()).persistToServer(params, "child[photo]",
				photoData);
	}
}

package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.Dialog;

import com.rapidftr.Main;
import com.rapidftr.controls.Button;
import com.rapidftr.controls.ImageButton;
import com.rapidftr.layouts.BorderManager;
import com.rapidftr.services.PhotoServiceListener;
import com.rapidftr.services.ServiceManager;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class HomeScreen extends DisplayPage {
	public static final int CREATE_RECORD_ACTION = 1;
	public static final int SEARCH_ACTION = 2;

	private static final String DEFAULT_IMAGE_NAME = "img/head.png";

	private String user;
	private String headerText;
	private BorderManager manager;

	public HomeScreen() {
		this.user = ServiceManager.getLoginService().getLoggedInFullName();

		headerText = "logged in: " + user;

		Font defaultFont = Styles.getDefaultFont();

		Button footerButton = new Button("Search", 150);

		FieldChangeListener searchEditListener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSearchAndEdit();
			}
		};

		footerButton.setChangeListener(searchEditListener);

		footerButton.setFont(defaultFont);

		ImageButton imageButton = new ImageButton(DEFAULT_IMAGE_NAME, Display
				.getHeight() - 80);

		FieldChangeListener takePhotoListener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onTakePhoto();
			}
		};

		imageButton.setChangeListener(takePhotoListener);

		manager = new BorderManager(headerText, imageButton, footerButton);

		add(manager);

	}

	public void updatePage(Object userInfo, DisplayPage source) {
		String recordId = (String) userInfo;

		manager.headerField.setText("Status: saved record " + recordId);

		invalidate();
	}

	public boolean onClose() {
		Dialog.alert("Closing " + Main.APPLICATION_NAME);
		System.exit(0);
		return true;
	}

	private void onTakePhoto() {
		ServiceManager.getPhotoService().startCamera(
				new PhotoServiceListener() {

					public void handlePhoto(EncodedImage encodedImage) {
						EncodedImage photo = Utilities.getScaledImage(
								encodedImage, 100);

						createNewRecord(photo);
					}

				});

	}

	private void createNewRecord(EncodedImage photo) {
		String recordId = "[ - ]";

		Hashtable userInfo = new Hashtable();

		userInfo.put("photo", photo);
		userInfo.put("id", recordId);
		userInfo.put("user", user);

		pushScreen(CREATE_RECORD_ACTION, userInfo);
	}

	private void onSearchAndEdit() {
		pushScreen(SEARCH_ACTION, null);
	}

}

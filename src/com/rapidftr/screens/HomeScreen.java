package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.Main;
import com.rapidftr.NavigationController;
import com.rapidftr.controls.Button;
import com.rapidftr.controls.ImageButton;
import com.rapidftr.layouts.BorderManager;
import com.rapidftr.services.ServiceException;
import com.rapidftr.services.ServiceManager;
import com.rapidftr.utilities.Styles;

public class HomeScreen extends MainScreen implements Page {
	private static final String DEFAULT_IMAGE_NAME = "img/head.png";

	private String user;
	private String headerText;
	private BorderManager manager;

	public HomeScreen() {
		this.user = ServiceManager.getLoginService().getLoggedInUser();

		headerText = "logged in: " + user;

		Font defaultFont = Styles.getDefaultFont();

		Button footerButton = new Button("Search + Edit", 150);

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

	public void setUserInfo(Object userInfo) {
	}
	
	private MenuItem _takePhoto = new MenuItem("Take Photo", 110, 10) {
		public void run() {
			onTakePhoto();
		}
	};

	private MenuItem _searchAndEdit = new MenuItem("Search + Edit", 110, 10) {
		public void run() {
			onSearchAndEdit();
		}
	};

	private MenuItem _close = new MenuItem("Close " + Main.APPLICATION_NAME,
			110, 10) {
		public void run() {
			onClose();
		}
	};

	protected void makeMenu(Menu menu, int instance) {
		menu.add(_takePhoto);
		menu.add(_searchAndEdit);
		menu.add(_close);
	}

	public boolean onClose() {
		Dialog.alert("Closing " + Main.APPLICATION_NAME);
		System.exit(0);
		return true;
	}

	private void onTakePhoto() {
		EncodedImage photo = ServiceManager.getPhotoService().getPhoto();

		String recordId = null;

		try {
			recordId = ServiceManager.getRecordService().getRecordId();
		} catch (ServiceException se) {
			System.out.println("Service Exception " + se);
		}

//		RecordCreationScreen screen = new RecordCreationScreen(photo, recordId, user);
//
//		final HomeScreen thisScreen = this;
//
//		screen.addScreenManager(new ScreenManager() {
//			public void closeScreen(int status, Object userInfo) {
//				String recordId = (String) userInfo;
//
//				manager.headerField.setText("Status: saved record " + recordId);
//
//				thisScreen.invalidate();
//			}
//		});

		NavigationController controller = NavigationController.getInstance(this.getUiEngine());
		
		Hashtable userInfo = new Hashtable();
		
		userInfo.put("photo", photo);
		userInfo.put("id", recordId);
		userInfo.put("user", user);
		
		controller.pushScreen(NavigationController.HOME_SCREEN, 1, userInfo);
	}

	private void onSearchAndEdit() {
		NavigationController controller = NavigationController.getInstance(this.getUiEngine());
		
		controller.pushScreen(NavigationController.HOME_SCREEN, 2, null);
	}
}
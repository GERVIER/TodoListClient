package fr.ensim.controller;

import model.Tache;

public class TaskEditorHandler {

	private static Tache tacheToEdit;

	
	
	public static Tache getTacheToEdit() {
		return tacheToEdit;
	}

	public static void setTacheToEdit(Tache tacheToEdit) {
		TaskEditorHandler.tacheToEdit = tacheToEdit;
	}

}

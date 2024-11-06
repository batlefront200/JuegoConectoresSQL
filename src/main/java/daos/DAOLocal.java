package daos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Vespertino
 */
public interface DAOLocal {

    public void saveConfig();

    public void updateConfig();

    public void getConfig();

    public void saveProgress();

    public void updateProgress();

    public void getProgress();
}

package daos;

import clases.Player;
import pojo.ConfigXML;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Vespertino
 */
public interface XmlDAO {

    public void saveConfig(ConfigXML datos);

    public String[] getConfig();

}

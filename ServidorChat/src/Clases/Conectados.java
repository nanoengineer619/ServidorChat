/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author Nano
 */
public class Conectados {
    private String idEmisor;
    private String ipEmisor;
    
    public Conectados(String idEmisor, String ipEmisor)
    {
        this.idEmisor=idEmisor;
        this.ipEmisor=ipEmisor;
    }
    public String getIdEmisor()
    {
        return this.idEmisor;
    }
    public String getIpEmisor()
    {
        return this.ipEmisor;
    }
}

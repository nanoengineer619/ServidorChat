/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;


/**
 *
 * @author Nano
 */
public class Mensaje{
    private String idEmisor;
    private String idReceptor;
    private String mensaje;
    
    public Mensaje(String idEmisor, String idReceptor, String mensaje)
    {
        this.idEmisor=idEmisor;
        this.idReceptor=idReceptor;
        this.mensaje=mensaje;
    }
    public String getidEmisor()
    {
        return this.idEmisor;
    }
    public String getidReceptor()
    {
        return this.idReceptor;
    }
    public String getmensaje()
    {
        return this.mensaje;
    }
}
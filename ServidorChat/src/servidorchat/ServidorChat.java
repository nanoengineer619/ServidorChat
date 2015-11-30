/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat;

import Clases.Conectados;
import Clases.Mensaje;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Nano
 */
class ServidorChat extends Thread
{
    private ArrayList<Mensaje> mensajeClientes=new ArrayList<Mensaje>();
    private String emisor, receptor, mensaje;
    private String xMensaje;
    private List<Integer> i=new ArrayList<Integer>();
    private int[] estado=new int[500];
    long timeNow = System.currentTimeMillis();
    private ArrayList<Conectados> ConectadosX=new ArrayList<Conectados>();
    private boolean identificadorMensaje;
    
    @Override
    public void run()
    {
        ServerSocket SSocket=null;
        Socket Cliente = null;
        try
        {
            SSocket = new ServerSocket(7777);
            System.out.println("Servidor iniciado en el puerto 7777");
            JFrame V=new JFrame("Servidor CHAT");
            V.setSize(300, 100);
            JButton btnSalir=new JButton();
            btnSalir.setText("Detener Servidor y Salir");
            V.add(btnSalir);
            V.show();
            
            V.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);            
            btnSalir.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        catch(IOException | HeadlessException ex)
        {
            System.out.println("Error Abriendo el Socket "+ex.getMessage());
        }
            while(true) 
            {
                try
                {
                    Cliente = SSocket.accept();
                    
                    DataInputStream in=new DataInputStream(Cliente.getInputStream());
                    String inMensaje=in.readUTF();
                    emisor=inMensaje.substring(inMensaje.indexOf(
                            "<|emisor:|>")+11, inMensaje.indexOf("</|emisor:|>")
                            );
                    
                    if(emisor.equals("KAccesoServidorK"))
                    {
                        if((System.currentTimeMillis()-timeNow)>3000)
                        {
                            int retroceso=0;
                            int tamanioListaConect=ConectadosX.size();
                            for(int contador=0;contador<tamanioListaConect;contador++)
                            {
                                if(estado[contador]!=1)
                                {
                                    ConectadosX.remove(contador-retroceso);
                                    retroceso++;
                                }
                                estado[contador]=0;
                            }
                            timeNow=System.currentTimeMillis();
                        }
                        String pruebaUsuario=inMensaje.substring(inMensaje.indexOf("</|emisor:|>")+12);
                        int vueltasSalir=0;
                        for(Conectados enLinea:ConectadosX)
                        {
                            if(pruebaUsuario.toUpperCase().equals(enLinea.getIdEmisor().toUpperCase()))
                            {
                                DataOutputStream out=new DataOutputStream(Cliente.getOutputStream());
                                out.writeUTF("NO");
                                break;
                            }
                            vueltasSalir++;
                        }
                        if(vueltasSalir<ConectadosX.size())
                        {
                            Cliente.close();
                            continue;
                        }
                        DataOutputStream out=new DataOutputStream(Cliente.getOutputStream());
                        out.writeUTF("SI");
                        Cliente.close();
                        continue;
                    }
                    boolean Contect=false;
                    String CenLinea="";
                    int currentContacto=0;
                    for(Conectados enLinea:ConectadosX)
                    {
                        if(emisor.toUpperCase().equals(enLinea.getIdEmisor().toUpperCase()))
                        {
                            Contect=true;
                            estado[currentContacto]=1;
                        }
                        CenLinea+=enLinea.getIdEmisor()+";";
                        currentContacto++;
                    }
                    if(!Contect)
                    {
                        String auxAdress=Cliente.getInetAddress().toString();
                        ConectadosX.add(new Conectados(emisor, auxAdress.substring(1, auxAdress.length())));
                        CenLinea+=emisor;
                    }
                    if((System.currentTimeMillis()-timeNow)>3000)
                    {
                        int retroceso=0;
                        int tamanioListaConect=ConectadosX.size();
                        for(int contador=0;contador<tamanioListaConect;contador++)
                        {
                            if(estado[contador]!=1)
                            {
                                ConectadosX.remove(contador-retroceso);
                                retroceso++;
                            }
                            estado[contador]=0;
                        }
                        timeNow=System.currentTimeMillis();
                    }
                    
                    receptor=inMensaje.substring(inMensaje.indexOf(
                            "<|receptor:|>")+13, inMensaje.indexOf("</|receptor:|>")
                            );
                    mensaje=inMensaje.substring(inMensaje.indexOf(
                            "<|mensaje:|>")+12,inMensaje.length()
                            );                    
                    
                    if(mensaje.equals("</VerArchivosCompartidos>"))
                    {
                        boolean identi=false;
                        for(Conectados valor:ConectadosX)
                        {
                            if(valor.getIdEmisor().equals(receptor))
                            {
                                DataOutputStream outx=new DataOutputStream(Cliente.getOutputStream());
                                outx.writeUTF(valor.getIpEmisor());
                                outx.close();
                                identi=true;
                                break;
                            }
                        }
                        if(!identi)
                        {
                            DataOutputStream outx=new DataOutputStream(Cliente.getOutputStream());
                            outx.writeUTF("Contacto Desconectado");
                            outx.close();
                        }
                        in.close();
                        Cliente.close();
                        continue;
                    }
                    
                    identificadorMensaje=false;
                    if(!mensaje.equals(""))
                    {
                        mensajeClientes.add(new Mensaje(emisor, receptor, mensaje));
                    }
                    else
                    {
                        xMensaje="";
                        int contador=0;
                        for(Mensaje xMensajeCliente:mensajeClientes)
                        {
                            if(emisor.toUpperCase().equals(xMensajeCliente.getidReceptor().toUpperCase()))
                            {
                                xMensaje+="<|CenLinea|>"+CenLinea+"</|CenLinea|>";
                                xMensaje+="|::::::::::"+xMensajeCliente.getidEmisor()+"::::::::::| dice:</sepatadorArray>";
                                xMensaje+=xMensajeCliente.getmensaje()+"</sepatadorArray>";
                                i.add(contador);
                            }
                            contador+=1;
                        }
                    
                        int contadorRetroceso=0;
                        for(Integer posicion:i)
                        {
                            mensajeClientes.remove(posicion-contadorRetroceso);
                            contadorRetroceso++;
                        }
                        i.clear();
                        
                        if(!xMensaje.equals(""))
                        {
                            DataOutputStream out=new DataOutputStream(Cliente.getOutputStream());
                            out.writeUTF(xMensaje);
                            identificadorMensaje=true;
                        }
                    }
                    if(!identificadorMensaje)
                    {
                        xMensaje="";
                        xMensaje+="<|CenLinea|>"+CenLinea+"</|CenLinea|>";
                        
                        DataOutputStream out=new DataOutputStream(Cliente.getOutputStream());
                        out.writeUTF(xMensaje);
                    }
                        
                    Cliente.close();
                }
                catch(UnknownHostException ex)
                {
                    if(!Cliente.isClosed())
                    {
                        try
                        {
                            Cliente.close();
                        }
                        catch(Exception exx){}
                    }
                    System.out.println("Error InknowHostException "+ex.getMessage());
                }
                catch(Exception ex)
                {
                    if(!Cliente.isClosed())
                    {
                        try
                        {
                            Cliente.close();
                        }
                        catch(Exception exx){}
                    }
                    System.out.println("Error Exception "+ex.getMessage());
                }
            }
    }
    public static void main(String[] args){
        new ServidorChat().start();
    }
}
